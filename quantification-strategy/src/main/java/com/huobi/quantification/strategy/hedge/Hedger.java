package com.huobi.quantification.strategy.hedge;

import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import com.huobi.quantification.entity.StrategyTradeFee;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.enums.HedgerActionEnum;
import com.huobi.quantification.strategy.enums.OrderActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class Hedger {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommContext commContext;
    @Autowired
    private HedgerContext hedgerContext;

    private AtomicBoolean hedgePhase1Enable = new AtomicBoolean(true);
    private AtomicBoolean hedgePhase2Enable = new AtomicBoolean(true);

    private Thread hedgePhase1Thread;
    private Thread hedgePhase2Thread;

    private AtomicLong counter = new AtomicLong(0);

    public void init(StrategyProperties.ConfigGroup group) {
        hedgerContext.init(group);
        startHedgeCtrlThread();
    }

    public void startHedgePhase1() {
        hedgePhase1Thread = new Thread(() -> {
            while (!hedgePhase1Thread.isInterrupted() && hedgePhase1Enable.get()) {
                try {
                    boolean b = hedgePhase1();
                    if (!b) {
                        ThreadUtils.sleep(10 * 1000);
                    }
                } catch (Throwable e) {
                    logger.error("对冲1阶段出现异常,", e);
                    ThreadUtils.sleep(10 * 1000);
                }
            }
        });
        hedgePhase1Thread.setDaemon(true);
        hedgePhase1Thread.setName("1阶段对冲线程");
        hedgePhase1Thread.start();
    }

    private boolean hedgePhase1() {
        Stopwatch started = Stopwatch.createStarted();
        long startTime = System.currentTimeMillis();
        logger.info("========>合约对冲第{}轮 开始", counter.incrementAndGet());
        HedgerActionEnum hedgeAction = commContext.getHedgeAction();
        switch (hedgeAction) {
            case STOP_HEDGER:
                commContext.cancelAllSpotOrder();
                logger.error("风控已经发出停止摆单指令，本轮对冲结束并撤销所有订单");
                return true;
        }
        StrategyTradeFee tradeFeeConfig = commContext.getStrategyTradeFeeConfig();
        if (tradeFeeConfig == null) {
            logger.error("交易手续费配置获取失败，方法退出");
            return false;
        }
        StrategyHedgeConfig hedgeConfig = commContext.getStrategyHedgeConfig();
        if (hedgeConfig == null) {
            logger.error("对冲参数配置获取失败，方法退出");
            return false;
        }
        hedgerContext.setHedgeConfig(hedgeConfig);
        hedgerContext.setTradeFeeConfig(tradeFeeConfig);
        // 撤掉币币账户所有未成交订单
        boolean b = commContext.cancelAllSpotOrder();
        if (!b) {
            logger.error("取消现货所有订单失败，方法退出");
            return false;
        }
        // 2.计算当前的两个账户总的净头寸USDT
        BigDecimal netPosition = commContext.getNetPositionUsdt();
        // 3. 下单
        hedgerContext.placeHedgeOrder(netPosition);
        logger.info("========>合约对冲第{}轮 结束，耗时：{}", counter.get(), started);
        ThreadUtils.sleep(startTime, hedgeConfig.getHedgeInterval());
        return true;
    }


    public void startHedgePhase2() {
        hedgePhase2Thread = new Thread(() -> {
            while (!hedgePhase1Thread.isInterrupted() && hedgePhase2Enable.get()) {
                try {
                    boolean b = hedgePhase2();
                    if (!b) {
                        ThreadUtils.sleep(10 * 1000);
                    }
                } catch (Exception e) {
                    logger.error("对冲2阶段出现异常,", e);
                    ThreadUtils.sleep(10 * 1000);
                }
            }
        });
        hedgePhase2Thread.setDaemon(true);
        hedgePhase2Thread.setName("2阶段对冲线程");
        hedgePhase2Thread.start();
    }

    private boolean hedgePhase2() {
        long startTime = System.currentTimeMillis();
        StrategyHedgeConfig hedgeConfig = commContext.getStrategyHedgeConfig();
        if (hedgeConfig == null) {
            logger.error("对冲参数配置获取失败，方法退出");
            return false;
        }
        String stopTime2 = hedgeConfig.getStopTime2();
        Integer deliveryInterval = hedgeConfig.getDeliveryInterval();
        long count = DateUtils.getSecond(LocalTime.now(), LocalTime.parse(stopTime2, DateTimeFormatter.ISO_LOCAL_TIME)) / deliveryInterval;
        // 撤掉币币账户所有未成交订单
        boolean success = commContext.cancelAllSpotOrder();
        if (!success) {
            logger.error("取消现货所有订单失败，方法退出");
            return false;
        }
        // 2.计算当前的两个账户总的净头寸USDT
        BigDecimal m1 = commContext.getNetPositionUsdt();
        BigDecimal m2 = commContext.getCurrFutureUsdt();
        // 3.需要在币币账户对冲的金额
        BigDecimal m = m2.subtract(m1);

        BigDecimal netPosition = m.divide(BigDecimal.valueOf(count), 18, BigDecimal.ROUND_DOWN);
        hedgerContext.placeDeliveryHedgeOrder(netPosition);

        ThreadUtils.sleep(startTime, deliveryInterval);
        return true;
    }

    private void stopHedgePhase1() {
        hedgePhase1Enable.set(false);
        hedgePhase1Thread.interrupt();
        startHedgePhase2();
    }

    private void stopHedgePhase2() {
        hedgePhase2Enable.set(false);
        hedgePhase2Thread.interrupt();
    }

    private void startHedgeCtrlThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    StrategyHedgeConfig hedgeConfig = commContext.getStrategyHedgeConfig();
                    if (commContext.isThisWeek()) {
                        LocalDateTime now = LocalDateTime.now();
                        if (now.isAfter(DateUtils.getFriday(hedgeConfig.getStopTime1())) && hedgePhase1Enable.get()) {
                            stopHedgePhase1();
                        }
                        if (now.isAfter(DateUtils.getFriday(hedgeConfig.getStopTime2())) && hedgePhase2Enable.get()) {
                            stopHedgePhase2();
                        }
                    }
                    ThreadUtils.sleep(1000);
                } catch (Exception e) {
                    logger.error("对冲监控线程出现异常", e);
                    ThreadUtils.sleep(1000);
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("对冲控制线程");
        thread.start();
    }


}
