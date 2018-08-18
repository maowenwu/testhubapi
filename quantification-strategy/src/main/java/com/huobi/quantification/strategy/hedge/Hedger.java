package com.huobi.quantification.strategy.hedge;

import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import com.huobi.quantification.entity.StrategyInstanceConfig;
import com.huobi.quantification.entity.StrategyTradeFee;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.enums.HedgerActionEnum;
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

    private AtomicBoolean hedgePhase1Enable;
    private AtomicBoolean hedgePhase2Enable;

    private Thread hedgePhase1Thread;
    private Thread hedgePhase2Thread;
    private Thread hedgePhase3Thread;
    private Thread hedgeCtrlThread;

    private AtomicLong counter;

    public void init(StrategyInstanceConfig config) {
        hedgerContext.init(config);
        counter = new AtomicLong(0);
        hedgePhase1Enable = new AtomicBoolean(true);
        hedgePhase2Enable = new AtomicBoolean(true);
        startHedgeCtrlThread();
    }

    public void stop() {
        if (hedgePhase1Thread != null) {
            hedgePhase1Thread.interrupt();
        }
        if (hedgePhase2Thread != null) {
            hedgePhase2Thread.interrupt();
        }
        if (hedgePhase3Thread != null) {
            hedgePhase3Thread.interrupt();
        }
        if (hedgeCtrlThread != null) {
            hedgeCtrlThread.interrupt();
        }
    }

    public void startHedgePhase1() {
        hedgePhase1Thread = new Thread(() -> {
            int failedCount = 0;
            while (!hedgePhase1Thread.isInterrupted() && hedgePhase1Enable.get()) {
                try {
                    hedgePhase1();
                } catch (Exception e) {
                    if (e instanceof RuntimeException && e.getCause() != null && e.getCause() instanceof InterruptedException) {
                        hedgePhase1Thread.interrupt();
                    }
                    logger.error("对冲1阶段出现异常,", e);
                    failedCount += 1;
                    if (failedCount > 3) {
                        try {
                            commContext.cancelAllSpotOrder();
                        } catch (Exception e1) {
                            logger.error("对冲1阶段取消所有现货订单异常", e1);
                        }
                        failedCount = 0;
                    }
                    ThreadUtils.sleep(1000);
                }
            }
            logger.info("对冲1阶段线程退出");
        });
        hedgePhase1Thread.setDaemon(true);
        hedgePhase1Thread.setName("1阶段对冲线程");
        hedgePhase1Thread.start();
    }

    private void hedgePhase1() {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("========>合约对冲第{}轮 开始", counter.incrementAndGet());
        HedgerActionEnum hedgeAction = commContext.getHedgeAction();
        switch (hedgeAction) {
            case STOP_HEDGER:
                commContext.cancelAllSpotOrder();
                logger.error("风控已经发出停止摆单指令，本轮对冲结束并撤销所有订单");
                ThreadUtils.sleep(1000);
                return;
        }
        // 撤掉币币账户所有未成交订单
        commContext.cancelAllSpotOrder();
        StrategyTradeFee tradeFeeConfig = commContext.getStrategyTradeFeeConfig();
        StrategyHedgeConfig hedgeConfig = commContext.getStrategyHedgeConfig();

        hedgerContext.setHedgeConfig(hedgeConfig);
        hedgerContext.setTradeFeeConfig(tradeFeeConfig);

        // 计算当前的两个账户总的净头寸USDT
        BigDecimal netPosition = commContext.getNetPositionUsdt();
        // 3. 下单
        hedgerContext.placeHedgeOrder(netPosition, false);
        logger.info("========>合约对冲第{}轮 结束，耗时：{}", counter.get(), started);
        ThreadUtils.sleep(hedgeConfig.getHedgeInterval() * 1000);
    }


    public void startHedgePhase2() {
        hedgePhase2Thread = new Thread(() -> {
            int failedCount = 0;
            while (!hedgePhase2Thread.isInterrupted() && hedgePhase2Enable.get()) {
                try {
                    hedgePhase2();
                } catch (Exception e) {
                    if (e instanceof RuntimeException && e.getCause() != null && e.getCause() instanceof InterruptedException) {
                        hedgePhase2Thread.interrupt();
                    }
                    logger.error("对冲2阶段出现异常,", e);
                    failedCount += 1;
                    if (failedCount > 3) {
                        try {
                            commContext.cancelAllSpotOrder();
                        } catch (Exception e1) {
                            logger.error("对冲2阶段取消所有现货订单异常", e1);
                        }
                        failedCount = 0;
                    }
                    ThreadUtils.sleep(1000);
                }
            }
            logger.info("对冲2阶段线程退出");
        });
        hedgePhase2Thread.setDaemon(true);
        hedgePhase2Thread.setName("2阶段对冲线程");
        hedgePhase2Thread.start();
    }

    private void hedgePhase2() {
        StrategyHedgeConfig hedgeConfig = commContext.getStrategyHedgeConfig();
        String stopTime2 = hedgeConfig.getStopTime2();
        Integer deliveryInterval = hedgeConfig.getDeliveryInterval();
        long count = DateUtils.getSecond(LocalTime.now(), LocalTime.parse(stopTime2, DateTimeFormatter.ISO_LOCAL_TIME)) / deliveryInterval;
        // 撤掉币币账户所有未成交订单
        commContext.cancelAllSpotOrder();

        // 2.计算当前的两个账户总的净头寸USDT
        BigDecimal m1 = commContext.getNetPositionUsdt();
        BigDecimal m2 = commContext.getCurrFutureUsdt();
        // 3.需要在币币账户对冲的金额
        BigDecimal m = m1.subtract(m2);

        BigDecimal netPosition = m.divide(BigDecimal.valueOf(count), 18, BigDecimal.ROUND_DOWN);
        hedgerContext.placeHedgeOrder(netPosition, true);
        ThreadUtils.sleep(deliveryInterval * 1000);
    }

    public void startHedgePhase3() {
        hedgePhase3Thread = new Thread(() -> {
            int failedCount = 0;
            while (!hedgePhase3Thread.isInterrupted()) {
                try {
                    hedgePhase3();
                } catch (Exception e) {
                    if (e instanceof RuntimeException && e.getCause() != null && e.getCause() instanceof InterruptedException) {
                        hedgePhase2Thread.interrupt();
                    }
                    logger.error("对冲3阶段出现异常,", e);
                    failedCount += 1;
                    if (failedCount > 3) {
                        try {
                            commContext.cancelAllSpotOrder();
                        } catch (Exception e1) {
                            logger.error("对冲3阶段取消所有现货订单异常", e1);
                        }
                        failedCount = 0;
                    }
                    ThreadUtils.sleep(1000);
                }
            }
            logger.info("对冲3阶段线程退出");
        });
        hedgePhase3Thread.setDaemon(true);
        hedgePhase3Thread.setName("3阶段对冲线程");
        hedgePhase3Thread.start();
    }

    private void hedgePhase3() {
        // todo
    }

    private void stopHedgePhase1() {
        hedgePhase1Enable.set(false);
        hedgePhase1Thread.interrupt();
        startHedgePhase2();
    }

    private void stopHedgePhase2() {
        hedgePhase2Enable.set(false);
        hedgePhase2Thread.interrupt();
        startHedgePhase3();
    }

    private void startHedgeCtrlThread() {
        hedgeCtrlThread = new Thread(() -> {
            while (!hedgeCtrlThread.isInterrupted()) {
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
                    if (e instanceof RuntimeException && e.getCause() != null && e.getCause() instanceof InterruptedException) {
                        hedgeCtrlThread.interrupt();
                    }
                    logger.error("对冲监控线程出现异常", e);
                    ThreadUtils.sleep(1000);
                }
            }
            logger.info("对冲控制线程退出");
        });
        hedgeCtrlThread.setDaemon(true);
        hedgeCtrlThread.setName("对冲控制线程");
        hedgeCtrlThread.start();
    }


}
