package com.huobi.quantification.strategy.order;


import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OrderCopier {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicLong counter = new AtomicLong(0);

    @Autowired
    private OrderContext orderContext;
    @Autowired
    private CommContext commContext;

    private DepthBookAdjuster depthBookAdjuster;


    public void init(StrategyProperties.ConfigGroup group) {
        depthBookAdjuster = new DepthBookAdjuster(orderContext);
        orderContext.init(group);
    }


    public boolean copyOrder() {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("========>合约借深度第{}轮 开始", counter.incrementAndGet());
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
        BigDecimal currPrice = commContext.getSpotCurrentPrice();
        if (currPrice == null) {
            logger.error("获取现货当前价格失败，方法退出");
            return false;
        }
        // todo 撤单 失败次数超过
        // 每一轮搬砖多个流程使用同一份配置
        StrategyOrderConfig config = orderContext.getStrategyOrderConfig();
        depthBookAdjuster.setExchangeRate(exchangeRate);
        DepthBook depthBook = depthBookAdjuster.getAdjustedDepthBook(config);
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
        orderContext.setConfig(config);
        orderContext.setFutureBalance(futureBalance);
        orderContext.setSpotBalance(spotBalance);
        orderContext.setCurrPrice(currPrice);
        orderContext.setExchangeRate(exchangeRate);

        // 先处理买单
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

        // 处理卖单，逻辑与买单一致
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
        logger.info("========>合约借深度第{}轮 结束，耗时：{}", counter.get(), started);
        return true;
    }

}
