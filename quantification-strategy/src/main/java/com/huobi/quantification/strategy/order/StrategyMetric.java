package com.huobi.quantification.strategy.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class StrategyMetric {

    Logger logger = LoggerFactory.getLogger(getClass());

    private List<Long> successBuyOpenOrder = new ArrayList<>();
    private AtomicLong failedBuyOpenOrder = new AtomicLong(0);
    private AtomicLong ignoreBuyOpenOrder = new AtomicLong(0);

    private List<Long> successBuyCloseOrder = new ArrayList<>();
    private AtomicLong failedBuyCloseOrder = new AtomicLong(0);
    private AtomicLong ignoreBuyCloseOrder = new AtomicLong(0);

    private List<Long> successSellOpenOrder = new ArrayList<>();
    private AtomicLong failedSellOpenOrder = new AtomicLong(0);
    private AtomicLong ignoreSellOpenOrder = new AtomicLong(0);

    private List<Long> successSellCloseOrder = new ArrayList<>();
    private AtomicLong failedSellCloseOrder = new AtomicLong(0);
    private AtomicLong ignoreSellCloseOrder = new AtomicLong(0);

    public void reset() {
        successBuyOpenOrder.clear();
        failedBuyOpenOrder.set(0);
        ignoreBuyOpenOrder.set(0);

        successBuyCloseOrder.clear();
        failedBuyCloseOrder.set(0);
        ignoreBuyCloseOrder.set(0);

        successSellOpenOrder.clear();
        failedSellOpenOrder.set(0);
        ignoreSellOpenOrder.set(0);

        successSellCloseOrder.clear();
        failedSellCloseOrder.set(0);
        ignoreSellCloseOrder.set(0);

    }

    /*****************BuyOpenOrder**********************/
    public void successBuyOpenOrderAdd(Long orderId) {
        successBuyOpenOrder.add(orderId);
    }

    public void failedBuyOpenOrderIncrement() {
        failedBuyOpenOrder.incrementAndGet();
    }

    public void ignoreBuyOpenOrderIncrement() {
        ignoreBuyOpenOrder.incrementAndGet();
    }

    /*****************BuyCloseOrder**********************/

    public void successBuyCloseOrderAdd(Long orderId) {
        successBuyCloseOrder.add(orderId);
    }

    public void failedBuyCloseOrderIncrement() {
        failedBuyCloseOrder.incrementAndGet();
    }

    public void ignoreBuyCloseOrderIncrement() {
        ignoreBuyCloseOrder.incrementAndGet();
    }

    /*****************SellOpenOrder**********************/
    public void successSellOpenOrderAdd(Long orderId) {
        successSellOpenOrder.add(orderId);
    }

    public void failedSellOpenOrderIncrement() {
        failedSellOpenOrder.incrementAndGet();
    }

    public void ignoreSellOpenOrderIncrement() {
        ignoreSellOpenOrder.incrementAndGet();
    }

    /*****************SellCloseOrder**********************/
    public void successSellCloseOrderAdd(Long orderId) {
        successSellCloseOrder.add(orderId);
    }

    public void failedSellCloseOrderIncrement() {
        failedSellCloseOrder.incrementAndGet();
    }

    public void ignoreSellCloseOrderIncrement() {
        ignoreSellCloseOrder.incrementAndGet();
    }

    public void metricBuyOrder() {
        logger.info("买单统计==>买入开仓成功：{}，买入开仓失败：{}，买入开仓忽略：{}\r\n" +
                        "买入平仓成功：{}，买入平仓失败：{}，买入平仓忽略：{}",
                successBuyOpenOrder.size(), failedBuyOpenOrder.get(), ignoreBuyOpenOrder.get(),
                successBuyCloseOrder.size(), failedBuyCloseOrder.get(), ignoreBuyCloseOrder.get());
    }

    public void metricSellOrder() {
        logger.info("卖单统计==>卖出开仓成功：{}，卖出开仓失败：{}，卖出开仓忽略：{}\r\n" +
                        "卖出平仓成功：{}，卖出平仓失败：{}，卖出平仓忽略：{}",
                successSellOpenOrder.size(), failedSellOpenOrder.get(), ignoreSellOpenOrder.get(),
                successSellCloseOrder.size(), failedSellCloseOrder.get(), ignoreSellCloseOrder.get());
    }
}
