package com.huobi.quantification.strategy.hedge;

import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.entity.DepthBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
        hedgePhase1Thread = new Thread(() -> {
            while (!hedgePhase1Thread.isInterrupted()) {
                try {
                    if (hedgePhase1Enable.get()) {
                        StrategyHedgeConfig hedgeConfig = hedgerContext.getStrategyHedgeConfig();
                        // 1.撤掉币币账户所有未成交订单
                        commContext.cancelAllSpotOrder();
                        // 2.计算当前的两个账户总的净头寸USDT
                        BigDecimal netPosition = commContext.getNetPosition();
                        // 3. 下单
                        placeHedgeOrder(hedgeConfig, netPosition);
                    }
                } catch (Throwable e) {
                    logger.error("对冲期间出现异常,", e);
                    ThreadUtils.sleep(1000 * 5);
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
        AtomicLong totalCount = new AtomicLong(getSecond(stopTime1, stopTime2) / interval);
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(() -> {
            if (hedgePhase2Enable.get() && totalCount.get() > 0) {
                // 1.撤掉币币账户所有未成交订单
                commContext.cancelAllSpotOrder();
                // 2.计算当前的两个账户总的净头寸USDT
                BigDecimal m1 = commContext.getNetPosition();
                BigDecimal m2 = commContext.getCurrFutureUsd();
                // 3.需要在币币账户对冲的金额
                BigDecimal m = m2.subtract(m1);

                BigDecimal netPosition = m.divide(BigDecimal.valueOf(totalCount.get()), 18, BigDecimal.ROUND_DOWN);
                placeHedgeOrder(hedgeConfig, netPosition);
                totalCount.decrementAndGet();
            }
        }, 0, Long.valueOf(interval), TimeUnit.SECONDS);
    }

    public void placeHedgeOrder(StrategyHedgeConfig hedgeConfig, BigDecimal netPosition) {
        // 3. 获取买一卖一价格
        DepthBook depthBook = commContext.getDepth();
        BigDecimal ask1 = depthBook.getAsk1();
        BigDecimal bid1 = depthBook.getBid1();

        if (BigDecimalUtils.moreThan(netPosition, BigDecimal.ZERO)) {
            BigDecimal orderPrice = ask1.multiply(BigDecimal.ONE.add(hedgeConfig.getSlippage()));
            BigDecimal orderAmount = netPosition.divide(orderPrice);
            hedgerContext.placeBuyOrder(orderPrice, orderAmount);
        } else if (BigDecimalUtils.lessThan(netPosition, BigDecimal.ZERO)) {
            BigDecimal orderPrice = bid1.multiply(BigDecimal.ONE.subtract(hedgeConfig.getSlippage()));
            BigDecimal orderAmount = netPosition.divide(orderPrice);
            hedgerContext.placeSellOrder(orderPrice, orderAmount);
        }
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
                    if (now.isAfter(getFriday(stopTime1)) && hedgePhase1Enable.get()) {
                        stopHedgePhase1();
                    }
                    if (now.isAfter(getFriday(stopTime2)) && hedgePhase2Enable.get()) {
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


    public LocalDateTime getFriday(String time) {
        TemporalField fieldISO = WeekFields.of(Locale.CHINA).dayOfWeek();
        LocalDate localDate = LocalDate.now().with(fieldISO, 6);
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
        return LocalDateTime.of(localDate, localTime);
    }

    private long getSecond(String time1, String time2) {
        LocalTime t1 = LocalTime.parse(time1, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime t2 = LocalTime.parse(time2, DateTimeFormatter.ISO_LOCAL_TIME);
        return Duration.between(t1, t2).toMillis() / 1000;
    }

}
