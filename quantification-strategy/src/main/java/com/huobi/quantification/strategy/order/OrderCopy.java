package com.huobi.quantification.strategy.order;


import com.google.common.base.Stopwatch;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.order.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;


@Scope("prototype")
@Component
public class OrderCopy {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderContext context;

    private DepthBookAdjuster depthBookAdjuster;


    public void init(StrategyProperties.ConfigGroup group) {
        depthBookAdjuster = new DepthBookAdjuster(context);
        context.init(group);
    }


    public void copyOrder() {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("==>copyOrder start");
        // 更新订单信息
        boolean success = context.updateOrderInfo();
        if (!success) {
            logger.error("更新订单信息失败，方法退出");
            return;
        }
        FuturePosition position = context.getFuturePosition();
        if (position == null) {
            logger.error("获取期货持仓信息失败，方法退出");
            return;
        }
        FutureBalance futureBalance = context.getFutureBalance();
        if (futureBalance == null) {
            logger.error("获取期货资产信息失败，方法退出");
            return;
        }
        SpotBalance spotBalance = context.getSpotBalance();
        if (spotBalance == null) {
            logger.error("获取现货资产信息失败，方法退出");
            return;
        }
        BigDecimal exchangeRate = context.getExchangeRateOfUSDT2USD();
        if (exchangeRate == null) {
            logger.error("获取汇率失败，方法退出");
            return;
        }
        BigDecimal currPrice = context.getSpotCurrentPrice();
        if (currPrice == null) {
            logger.error("获取现货当前价格失败，方法退出");
            return;
        }
        // 每一轮搬砖多个流程使用同一份配置
        StrategyOrderConfig config = context.getStrategyOrderConfig();
        depthBookAdjuster.setExchangeRate(exchangeRate);
        DepthBook depthBook = depthBookAdjuster.getAdjustedDepthBook(config);
        if (depthBook == null) {
            logger.error("获取深度信息失败，方法退出");
            return;
        }
        List<DepthBook.Depth> asks = depthBook.getAsks();
        List<DepthBook.Depth> bids = depthBook.getBids();
        // 1. 取消那些未在深度列表中的订单，如果任何一单取消失败，开始下一个轮回
        List<FutureOrder> orders = context.cancelOrderNotInDepthBook(depthBook);
        // 创建一个订单读取器
        OrderReader orderReader = new OrderReader(orders);

        context.setOrderReader(orderReader);
        context.setFuturePosition(position);
        context.setConfig(config);
        context.setFutureBalance(futureBalance);
        context.setSpotBalance(spotBalance);
        context.setCurrPrice(currPrice);
        context.setExchangeRate(exchangeRate);

        // 先处理买单
        for (DepthBook.Depth bid : bids) {
            BigDecimal amountTotal = orderReader.getBidAmountTotalByPrice(bid.getPrice());
            // 如果预期下单数量大于目前已下单数量，那么补充一些订单
            if (BigDecimalUtils.moreThan(bid.getAmount(), amountTotal)) {
                BigDecimal orderAmount = bid.getAmount().subtract(amountTotal);
                // 下一些单
                context.placeBuyOrder(bid.getPrice(), orderAmount);
            } else if (BigDecimalUtils.lessThan(bid.getAmount(), amountTotal)) {
                // 如果预期下单数量小于当前已下单量，那么先取消所有订单再下预期数量的单
                // 撤销所有单
                List<Long> orderIds = orderReader.getBidExOrderIdByPrice(bid.getPrice());
                context.cancelOrder(orderIds);
                // 下单
                context.placeBuyOrder(bid.getPrice(), bid.getAmount());
            }
        }

        // 处理卖单，逻辑与买单一致
        for (DepthBook.Depth ask : asks) {
            BigDecimal amountTotal = orderReader.getAskAmountTotalByPrice(ask.getPrice());
            if (BigDecimalUtils.moreThan(ask.getAmount(), amountTotal)) {
                BigDecimal orderAmount = ask.getAmount().subtract(amountTotal);
                // 下一些单
                context.placeSellOrder(ask.getPrice(), orderAmount);
            } else if (BigDecimalUtils.lessThan(ask.getAmount(), amountTotal)) {
                List<Long> orderIds = orderReader.getAskExOrderIdByPrice(ask.getPrice());
                // 撤销所有单
                context.cancelOrder(orderIds);
                // 下单
                context.placeSellOrder(ask.getPrice(), ask.getAmount());
            }
        }
        logger.info("==>copyOrder end,耗时：" + started);
    }

}
