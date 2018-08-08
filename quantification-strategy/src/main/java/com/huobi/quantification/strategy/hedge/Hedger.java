package com.huobi.quantification.strategy.hedge;

import com.google.common.base.Stopwatch;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.StrategyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class Hedger {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    FutureContractService futureContractService;
    @Autowired
    StrategyRiskConfigMapper strategyRiskConfigMapper;
    @Autowired
    private CommContext commContext;
    @Autowired
    private HedgerContext hedgerContext;

    private ScheduledExecutorService scheduledExecutor;

    private AtomicBoolean hedgePhase1Enable = new AtomicBoolean(true);
    private AtomicBoolean hedgePhase2Enable = new AtomicBoolean(true);

    private String stopTime1 = "15:00:00";
    private String stopTime2 = "15:55:00";

    private Thread hedgePhase1Thread;

    public void init(StrategyProperties.ConfigGroup group) {
        hedgerContext.init(group);
        startHedgeCtrlThread(stopTime1, stopTime2);
    }

    public void hedgePhase1() {
        AtomicLong counter = new AtomicLong(0);
        hedgePhase1Thread = new Thread(() -> {
            while (!hedgePhase1Thread.isInterrupted()) {
                try {
                    if (hedgePhase1Enable.get()) {
                        Stopwatch started = Stopwatch.createStarted();
                        logger.info("========>合约对冲第{}轮 开始", counter.incrementAndGet());
                        StrategyHedgeConfig hedgeConfig = hedgerContext.getStrategyHedgeConfig();
                        hedgerContext.setHedgeConfig(hedgeConfig);
                        // 1.撤掉币币账户所有未成交订单
                        boolean success = commContext.cancelAllSpotOrder();
                        if (!success) {
                            logger.error("取消现货所有订单失败，重新开始");
                            continue;
                        }
                        // 2.计算当前的两个账户总的净头寸USDT
                        BigDecimal netPosition = commContext.getNetPositionUsdt();
                        // 3. 下单
                        hedgerContext.placeHedgeOrder(netPosition);
                        logger.info("========>合约对冲第{}轮 结束，耗时：{}", counter.get(), started);
                    }
                } catch (Throwable e) {
                    logger.error("对冲期间出现异常,", e);
                    ThreadUtils.sleep(1000 * 3);
                }
                ThreadUtils.sleep(1000 * 3);
            }
        });
        hedgePhase1Thread.setDaemon(true);
        hedgePhase1Thread.setName("1阶段对冲线程");
        hedgePhase1Thread.start();
    }

    public void hedgePhase2() {
        StrategyHedgeConfig hedgeConfig = hedgerContext.getStrategyHedgeConfig();
        Integer interval = hedgeConfig.getPlaceOrderInterval();
        AtomicLong totalCount = new AtomicLong(DateUtils.getSecond(stopTime1, stopTime2) / interval);
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(() -> {
            if (hedgePhase2Enable.get() && totalCount.get() > 0) {
                // 1.撤掉币币账户所有未成交订单
                commContext.cancelAllSpotOrder();
                // 2.计算当前的两个账户总的净头寸USDT
                BigDecimal m1 = commContext.getNetPositionUsdt();
                BigDecimal m2 = commContext.getCurrFutureUsdt();
                // 3.需要在币币账户对冲的金额
                BigDecimal m = m2.subtract(m1);

                BigDecimal netPosition = m.divide(BigDecimal.valueOf(totalCount.get()), 18, BigDecimal.ROUND_DOWN);
                hedgerContext.placeHedgeOrder(netPosition);
                totalCount.decrementAndGet();
            }
        }, 0, Long.valueOf(interval), TimeUnit.SECONDS);
    }


    private void stopHedgePhase1() {
        hedgePhase1Enable.set(false);
        hedgePhase1Thread.interrupt();
        hedgePhase2();
    }

    private void stopHedgePhase2() {
        hedgePhase2Enable.set(false);
        scheduledExecutor.shutdown();
    }

    private void startHedgeCtrlThread(String stopTime1, String stopTime2) {
        Thread thread = new Thread(() -> {
            while (true) {
                if (commContext.isThisWeek()) {
                    LocalDateTime now = LocalDateTime.now();
                    if (now.isAfter(DateUtils.getFriday(stopTime1)) && hedgePhase1Enable.get()) {
                        stopHedgePhase1();
                    }
                    if (now.isAfter(DateUtils.getFriday(stopTime2)) && hedgePhase2Enable.get()) {
                        stopHedgePhase2();
                    }
                } else {
                    logger.info("合约类型不是当周");
                }
                ThreadUtils.sleep(1000);
            }
        });
        thread.setDaemon(true);
        thread.setName("对冲控制线程");
        thread.start();
    }


}
