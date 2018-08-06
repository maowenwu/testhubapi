package com.huobi.quantification.strategy.risk;

import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.entity.StrategyRiskConfig;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.StrategyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author lichenyang
 * @since 2018年7月26日
 */
@Component
public class RiskMonitor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RiskContext riskContext;
    @Autowired
    private CommContext commContext;

    private StrategyRiskConfig riskConfig;

    public void init(StrategyProperties.ConfigGroup group) {
        riskContext.init(group);
    }

    public void check() {
        this.riskConfig = riskContext.getStrategyRiskConfig();
        BigDecimal currentPrice = riskContext.getSpotCurrentPrice();
        if (currentPrice == null) {
            logger.error("未获取到现货当前价格，方法退出");
            return;
        }
        riskContext.setCurrPrice(currentPrice);
        checkRiskRate();
        checkNetPosition();
        checkProfit();
    }

    /**
     * 用于监控合约账户的保证金率
     */
    public void checkRiskRate() {
        //获取该合约账户的对应币种的保证金率
        BigDecimal level1 = riskConfig.getRiskRateLevel1();
        BigDecimal level2 = riskConfig.getRiskRateLevel2();
        BigDecimal level3 = riskConfig.getRiskRateLevel3();

        BigDecimal riskRate = riskContext.getRiskRate();

        if (BigDecimalUtils.lessThanOrEquals(riskRate, level3)) {
            // 停止摆盘，发出警报，撤销所有订单，强平，直至保证金率恢复正常
            riskContext.updateRiskCtrl(2);
            warn();
        } else if (BigDecimalUtils.lessThanOrEquals(riskRate, level2)) {
            // 停止下开仓单，只下平仓单，撤销当前开仓单，发出警报
            riskContext.updateRiskCtrl(1);
            warn();
        } else if (BigDecimalUtils.lessThanOrEquals(riskRate, level1)) {
            // 停止下开仓单，只下平仓单
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

        BigDecimal netPosition = commContext.getNetPosition().abs();
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
