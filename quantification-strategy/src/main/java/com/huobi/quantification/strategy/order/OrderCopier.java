package com.huobi.quantification.strategy.order;


import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.entity.StrategyTradeFee;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.StrategyProperties;
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

    private AtomicLong counter = new AtomicLong(0);

    @Autowired
    private OrderContext orderContext;
    @Autowired
    private CommContext commContext;
    @Autowired
    private DepthBookAdjuster depthBookAdjuster;
    @Autowired
    private OrderCloser orderCloser;

    private Thread copyOrderThread;

    public void init(StrategyProperties.ConfigGroup group) {
        depthBookAdjuster.init(group);
        orderContext.init(group);
        startOrderCtrlThread();
    }

    public void start() {
        copyOrderThread = new Thread(() -> {
            while (!copyOrderThread.isInterrupted() && orderPhase2Enable.get()) {
                try {
                    boolean b = copyOrder();
                    if (!b) {
                        ThreadUtils.sleep(1000);
                    }
                } catch (Throwable e) {
                    logger.error("拷贝订单期间出现异常", e);
                    ThreadUtils.sleep(1000);
                }
            }
        });
        copyOrderThread.setDaemon(true);
        copyOrderThread.setName("摆单线程");
        copyOrderThread.start();
    }

    public boolean copyOrder() {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("========>合约借深度第{}轮 开始", counter.incrementAndGet());
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
                return false;
            case STOP_FORCE_CLOSE_ORDER:
                orderCloser.startForceCloseOrder();
                logger.error("风控已经发出强平指令，停止本轮摆单并强平部分仓位");
                return false;
        }
        // 更新订单信息
        boolean success = orderContext.updateOrderInfo();
        if (!success) {
            logger.error("更新订单信息失败，方法退出");
            return false;
        }
        FuturePosition position = commContext.getFuturePosition();
        if (position == null) {
            logger.error("获取期货持仓信息失败，方法退出");
            return false;
        }
        FutureBalance futureBalance = commContext.getFutureBalance();
        if (futureBalance == null) {
            logger.error("获取期货资产信息失败，方法退出");
            return false;
        }
        SpotBalance spotBalance = commContext.getSpotBalance();
        if (spotBalance == null) {
            logger.error("获取现货资产信息失败，方法退出");
            return false;
        }
        BigDecimal exchangeRate = commContext.getExchangeRateOfUSDT2USD();
        if (exchangeRate == null) {
            logger.error("获取汇率失败，方法退出");
            return false;
        }
        // 每一轮搬砖多个流程使用同一份配置
        StrategyOrderConfig orderConfig = commContext.getStrategyOrderConfig();
        if (orderConfig == null) {
            logger.error("获取订单策略参数失败，方法退出");
            return false;
        }
        StrategyTradeFee tradeFeeConfig = commContext.getStrategyTradeFeeConfig();
        if (tradeFeeConfig == null) {
            logger.error("获取交易费率参数失败，方法退出");
            return false;
        }
        depthBookAdjuster.setOrderConfig(orderConfig);
        depthBookAdjuster.setTradeFeeConfig(tradeFeeConfig);
        depthBookAdjuster.setExchangeRate(exchangeRate);
        DepthBook depthBook = depthBookAdjuster.getAdjustedDepthBook();
        if (depthBook == null) {
            logger.error("获取深度信息失败，方法退出");
            return false;
        }
        List<DepthBook.Depth> asks = depthBook.getAsks();
        List<DepthBook.Depth> bids = depthBook.getBids();
        // 1. 取消那些未在深度列表中的订单，如果任何一单取消失败，开始下一个轮回
        List<FutureOrder> orders = null;
        try {
            orders = orderContext.cancelOrderNotInDepthBook(depthBook);
        } catch (Exception e) {
            logger.error("取消订单失败，方法退出");
            return false;
        }
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
        Integer bidCountTotal = orderReader.getBidOrderCountTotal();
        if (bidCountTotal < orderConfig.getBidsMaxAmount()) {
            for (DepthBook.Depth bid : bids) {
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
            }
            orderContext.metricBuyOrder();
        } else {
            logger.warn("当前买单的下单总量已经超过限制");
        }

        // 处理卖单，逻辑与买单一致
        Integer askCountTotal = orderReader.getAskOrderCountTotal();
        if (askCountTotal < orderConfig.getAsksMaxAmount()) {
            for (DepthBook.Depth ask : asks) {
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
            }
            orderContext.metricSellOrder();
        } else {
            logger.warn("当前卖单的下单总量已经超过限制");
        }
        logger.info("========>合约借深度第{}轮 结束，耗时：{}", counter.get(), started);
        return true;
    }


    private void stopOrderPhase1() {
        orderPhase1Enable.set(false);
        orderContext.setDeliveryCloseOrderOnly(true);
    }

    private void stopOrderPhase2() {
        orderPhase2Enable.set(false);
        copyOrderThread.interrupt();
    }

    private AtomicBoolean orderPhase1Enable = new AtomicBoolean(true);
    private AtomicBoolean orderPhase2Enable = new AtomicBoolean(true);

    private void startOrderCtrlThread() {
        Thread thread = new Thread(() -> {
            while (true) {
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
        });
        thread.setDaemon(true);
        thread.setName("订单控制线程");
        thread.start();
    }

}
