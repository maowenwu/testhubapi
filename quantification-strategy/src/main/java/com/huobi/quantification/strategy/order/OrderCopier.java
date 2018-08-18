package com.huobi.quantification.strategy.order;


import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.entity.StrategyInstanceConfig;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.entity.StrategyTradeFee;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.entity.*;
import com.huobi.quantification.strategy.enums.OrderActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OrderCopier {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicLong counter;

    @Autowired
    private OrderContext orderContext;
    @Autowired
    private CommContext commContext;
    @Autowired
    private DepthBookAdjuster depthBookAdjuster;
    @Autowired
    private OrderCloser orderCloser;

    private Thread copyOrderThread;

    private Thread orderCtrlThread;

    private AtomicBoolean orderPhase1Enable;
    private AtomicBoolean orderPhase2Enable;

    public void init(StrategyInstanceConfig config) {
        depthBookAdjuster.init(config);
        orderContext.init(config);
        counter = new AtomicLong(0);
        orderPhase1Enable = new AtomicBoolean(true);
        orderPhase2Enable = new AtomicBoolean(true);
        startOrderCtrlThread();
    }

    private void startOrderCtrlThread() {
        orderCtrlThread = new Thread(() -> {
            while (!orderCtrlThread.isInterrupted()) {
                try {
                    StrategyOrderConfig orderConfig = commContext.getStrategyOrderConfig();
                    if (commContext.isThisWeek()) {
                        LocalDateTime now = LocalDateTime.now();
                        if (now.isAfter(DateUtils.getFriday(orderConfig.getStopTime1())) && orderPhase1Enable.get()) {
                            stopOrderPhase1();
                        }
                        if (now.isAfter(DateUtils.getFriday(orderConfig.getStopTime2())) && orderPhase2Enable.get()) {
                            stopOrderPhase2();
                        }
                    }
                    ThreadUtils.sleep(1000);
                } catch (Exception e) {
                    logger.error("订单监控线程出现异常", e);
                    ThreadUtils.sleep(1000);
                }
            }
            logger.info("摆单控制线程退出");
        });
        orderCtrlThread.setDaemon(true);
        orderCtrlThread.setName("摆单控制线程");
        orderCtrlThread.start();
        logger.info("摆单控制线程启动成功");
    }

    private void stopOrderPhase1() {
        orderPhase1Enable.set(false);
        orderContext.setDeliveryCloseOrderOnly(true);
        logger.info("摆单第一阶段停止");
    }

    private void stopOrderPhase2() {
        orderPhase2Enable.set(false);
        copyOrderThread.interrupt();
        logger.info("摆单第二阶段停止");
    }

    public void start() {
        copyOrderThread = new Thread(() -> {
            int failedCount = 0;
            while (!copyOrderThread.isInterrupted() && orderPhase2Enable.get()) {
                try {
                    copyOrder();
                    failedCount = 0;
                } catch (Exception e) {
                    logger.error("摆盘期间出现异常", e);
                    failedCount += 1;
                    if (failedCount > 3) {
                        try {
                            commContext.cancelAllFutureOrder();
                        } catch (Exception e1) {
                            logger.error("摆盘期间取消所有期货订单异常", e1);
                        }
                        failedCount = 0;
                    }
                    ThreadUtils.sleep(1000);
                }
            }
            logger.info("摆单线程退出");
        });
        copyOrderThread.setDaemon(true);
        copyOrderThread.setName("摆单线程");
        copyOrderThread.start();
    }

    public void stop() {
        if (orderCtrlThread != null) {
            orderCtrlThread.interrupt();
        }
        if (copyOrderThread != null) {
            copyOrderThread.interrupt();
        }
    }

    public void copyOrder() {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("========>合约借深度第{}轮 开始", counter.incrementAndGet());
        // 更新订单信息
        orderContext.updateOrderInfo();
        OrderActionEnum orderAction = commContext.getOrderAction();
        switch (orderAction) {
            case NORMAL:
                orderContext.setRiskCloseOrderOnly(false);
                break;
            case CLOSE_ORDER_ONLY:
                orderContext.setRiskCloseOrderOnly(true);
                orderContext.cancelAllOpenOrder();
                break;
            case STOP_CANCEL_ORDER:
                commContext.cancelAllFutureOrder();
                logger.error("风控已经发出停止摆单指令，停止本轮摆单并撤销所有订单");
                ThreadUtils.sleep(1000);
                return;
            case STOP_FORCE_CLOSE_ORDER:
                orderCloser.startForceCloseOrder();
                logger.error("风控已经发出强平指令，停止本轮摆单并强平部分仓位");
                ThreadUtils.sleep(1000);
                return;
        }
        FuturePosition position = commContext.getFuturePosition();
        FutureBalance futureBalance = commContext.getFutureBalance();
        SpotBalance spotBalance = commContext.getSpotBalance();
        BigDecimal exchangeRate = commContext.getExchangeRateOfUSDT2USD();

        // 每一轮搬砖多个流程使用同一份配置
        StrategyOrderConfig orderConfig = commContext.getStrategyOrderConfig();
        StrategyTradeFee tradeFeeConfig = commContext.getStrategyTradeFeeConfig();

        depthBookAdjuster.setOrderConfig(orderConfig);
        depthBookAdjuster.setTradeFeeConfig(tradeFeeConfig);
        depthBookAdjuster.setExchangeRate(exchangeRate);
        DepthBook depthBook = depthBookAdjuster.getAdjustedDepthBook();

        List<DepthBook.Depth> asks = depthBook.getAsks();
        List<DepthBook.Depth> bids = depthBook.getBids();
        // 取消那些未在深度列表中的订单
        List<FutureOrder> orders = orderContext.cancelOrderNotInDepthBook(depthBook);
        // 创建一个订单读取器
        OrderReader orderReader = new OrderReader(orders, position);

        orderContext.setOrderReader(orderReader);
        orderContext.setFuturePosition(position);
        orderContext.setConfig(orderConfig);
        orderContext.setFutureBalance(futureBalance);
        orderContext.setSpotBalance(spotBalance);
        orderContext.setExchangeRate(exchangeRate);
        // 重置统计工具
        orderContext.resetMetric();

        // 先处理买单
        for (DepthBook.Depth bid : bids) {
            // 检查当前已下买单订单数量是否小于配置
            if (orderReader.getBidOrderCountTotal() < orderConfig.getBidsMaxAmount()) {
                BigDecimal amountTotal = orderReader.getBidAmountTotalByPrice(bid.getPrice());
                // 如果预期下单数量大于目前已下单数量，那么补充一些订单
                if (BigDecimalUtils.moreThan(bid.getAmount(), amountTotal)) {
                    BigDecimal orderAmount = bid.getAmount().subtract(amountTotal);
                    // 下一些单
                    orderContext.placeBuyOrder(bid.getPrice(), orderAmount);
                } else if (BigDecimalUtils.lessThan(bid.getAmount(), amountTotal)) {
                    // 如果预期下单数量小于当前已下单量，那么先取消所有订单再下预期数量的单
                    // 撤销所有单
                    List<Long> orderIds = orderReader.getBidExOrderIdByPrice(bid.getPrice());
                    orderContext.cancelOrder(orderIds);
                    // 下单
                    orderContext.placeBuyOrder(bid.getPrice(), bid.getAmount());
                }
            } else {
                logger.warn("当前买单的下单总量已经超过限制");
            }
        }
        orderContext.metricBuyOrder();

        // 处理卖单，逻辑与买单一致
        for (DepthBook.Depth ask : asks) {
            // 检查当前已下卖单订单数量是否小于配置
            if (orderReader.getAskOrderCountTotal() < orderConfig.getAsksMaxAmount()) {
                BigDecimal amountTotal = orderReader.getAskAmountTotalByPrice(ask.getPrice());
                if (BigDecimalUtils.moreThan(ask.getAmount(), amountTotal)) {
                    BigDecimal orderAmount = ask.getAmount().subtract(amountTotal);
                    // 下一些单
                    orderContext.placeSellOrder(ask.getPrice(), orderAmount);
                } else if (BigDecimalUtils.lessThan(ask.getAmount(), amountTotal)) {
                    List<Long> orderIds = orderReader.getAskExOrderIdByPrice(ask.getPrice());
                    // 撤销所有单
                    orderContext.cancelOrder(orderIds);
                    // 下单
                    orderContext.placeSellOrder(ask.getPrice(), ask.getAmount());
                }
            } else {
                logger.warn("当前卖单的下单总量已经超过限制");
            }
        }
        orderContext.metricSellOrder();
        logger.info("========>合约借深度第{}轮 结束，耗时：{}", counter.get(), started);
        ThreadUtils.sleep(orderConfig.getPlaceOrderInterval() * 1000);
    }

}
