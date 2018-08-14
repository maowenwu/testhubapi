package com.huobi.quantification.strategy.risk;

import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.entity.StrategyRiskConfig;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lichenyang
 * @since 2018年7月26日
 */
@Component
public class RiskMonitor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicLong counter = new AtomicLong(0);

    @Autowired
    private RiskContext riskContext;
    @Autowired
    private CommContext commContext;

    private StrategyRiskConfig riskConfig;

    public void init(StrategyProperties.ConfigGroup group) {
        riskContext.init(group);
    }

    private Thread riskMonitorThread;

    public void start() {
        riskMonitorThread = new Thread(() -> {
            while (true) {
                boolean b = check();
                if (!b) {
                    ThreadUtils.sleep(1000);
                }
            }
        });
        riskMonitorThread.setDaemon(true);
        riskMonitorThread.setName("风控线程");
        riskMonitorThread.start();
    }

    public boolean check() {
        try {
            Stopwatch started = Stopwatch.createStarted();
            logger.info("========>合约监控第{}轮 开始", counter.incrementAndGet());
            this.riskConfig = commContext.getStrategyRiskConfig();
            if (riskConfig == null) {
                logger.error("未获取到风控配置参数，方法退出");
                return false;
            }
            BigDecimal currentPrice = commContext.getSpotCurrentPrice();
            if (currentPrice == null) {
                logger.error("未获取到现货当前价格，方法退出");
                return false;
            }
            riskContext.setCurrPrice(currentPrice);

            checkRiskRate();
            checkNetPosition();
            checkProfit();
            logger.info("========>合约监控第{}轮 结束，耗时：{}", counter.get(), started);
            ThreadUtils.sleep(riskConfig.getRiskInterval() * 1000);
            return true;
        } catch (Exception e) {
            logger.error("监控保证金率期间出现异常", e);
            ThreadUtils.sleep(10 * 1000);
            return false;
        }
    }

    /**
     * 用于监控合约账户的保证金率
     */
    public void checkRiskRate() {
        //获取该合约账户的对应币种的保证金率
        BigDecimal level1 = riskConfig.getRiskRateLevel1();
        BigDecimal level2 = riskConfig.getRiskRateLevel2();
        BigDecimal level3 = riskConfig.getRiskRateLevel3();

        BigDecimal riskRate = commContext.getRiskRate();
        logger.info("当前保证金率：{}", riskRate);
        if (BigDecimalUtils.lessThanOrEquals(riskRate, level3)) {
            // 停止摆盘，发出警报，撤销所有订单，强平，直至保证金率恢复正常
            riskContext.updateRiskCtrl(3);
            warn();
        } else if (BigDecimalUtils.lessThanOrEquals(riskRate, level2)) {
            // 撤销所有未成交订单，停止借深度策略，还会发出警报；
            riskContext.updateRiskCtrl(2);
            warn();
        } else if (BigDecimalUtils.lessThanOrEquals(riskRate, level1)) {
            // 借深度策略会停止下开仓单，只下平仓单，并撤销当前所有开仓订单；
            riskContext.updateRiskCtrl(1);
        } else {
            // 修改为正常状态
            riskContext.updateRiskCtrl(0);
        }
    }

    /**
     * 净头寸监控(合约+币币账户组)：
     * 1，如果净头寸的绝对值大于阈值1，会停止合约摆盘，撤销合约账户所有未成交订单，对冲程序继续执行，直至低于阈值，重新恢复合约摆盘；
     * 2，如果净头寸的绝对值大于阈值2，会停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报。
     */
    public void checkNetPosition() {
        BigDecimal level1 = riskConfig.getNetPositionLevel1();
        BigDecimal level2 = riskConfig.getNetPositionLevel2();

        BigDecimal netPosition = commContext.getNetPositionUsdt().abs();

        if (BigDecimalUtils.moreThanOrEquals(netPosition, level2)) {
            // 会停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报
            riskContext.updateNetCtrl(2, 2);
            warn();
        } else if (BigDecimalUtils.moreThanOrEquals(netPosition, level1)) {
            // 会停止合约摆盘，撤销合约账户所有未成交订单，对冲程序继续执行，直至低于阈值，重新恢复合约摆盘
            riskContext.updateNetCtrl(2, 0);
        } else {
            // 正常
            riskContext.updateNetCtrl(0, 0);
        }
    }

    /**
     * 盈亏监控
     * 本次盈亏（和流动性项目策略开始启动时比较）
     * 总盈亏（和最初的持有量比较）
     */
    public void checkProfit() {
        checkCurrProfit();
        checkTotalProfit();
    }

    private void checkCurrProfit() {
        BigDecimal level1 = riskConfig.getCurrProfitLevel1();
        BigDecimal level2 = riskConfig.getCurrProfitLevel2();

        BigDecimal currProfit = riskContext.getCurrProfit();
        logger.info("本次亏损：{}" + riskContext.getSpotBaseCoin(), currProfit);
        if (BigDecimalUtils.lessThanOrEquals(currProfit, level2)) {
            // 停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报
            riskContext.updateProfitCtrl(2, 2);
            warn();
        } else if (BigDecimalUtils.lessThanOrEquals(currProfit, level1)) {
            // 发出警报
            warn();
        } else {
            // 正常，忽略
            riskContext.updateProfitCtrl(0, 0);
        }
    }


    private void checkTotalProfit() {
        BigDecimal level1 = riskConfig.getTotalProfitLevel1();
        BigDecimal level2 = riskConfig.getTotalProfitLevel2();

        BigDecimal totalProfit = riskContext.getTotalProfit();
        logger.info("总亏损：{}" + riskContext.getSpotBaseCoin(), totalProfit);
        if (BigDecimalUtils.lessThanOrEquals(totalProfit, level2)) {
            // 停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报
            riskContext.updateProfitCtrl(2, 2);
            warn();
        } else if (BigDecimalUtils.lessThanOrEquals(totalProfit, level1)) {
            // 发出警报
            warn();
        } else {
            // 正常，忽略
            riskContext.updateProfitCtrl(0, 0);
        }
    }


    private void warn() {

    }
}
