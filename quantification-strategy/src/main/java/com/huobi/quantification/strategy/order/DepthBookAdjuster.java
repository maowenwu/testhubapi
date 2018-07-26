package com.huobi.quantification.strategy.order;

import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.strategy.order.entity.DepthBook;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DepthBookAdjuster {

    private OrderContext context;

    public DepthBookAdjuster(OrderContext context) {
        this.context = context;
    }

    public DepthBook getAdjustedDepthBook(String symbol) {
        DepthBook depthBook = context.getDepth(symbol);
        StrategyOrderConfig config = context.getStrategyOrderConfig();
        adjPriceByExchangeRate(depthBook);
        adjPriceByFee(depthBook, config);
        mergeDepth(depthBook, config);
        adjMaxAmount(depthBook, config);
        adjBasisPrice(depthBook, config);
        sortDepthBook(depthBook);
        return depthBook;
    }

    /**
     * 使用汇率修正价格
     *
     * @param depthBook
     */
    private void adjPriceByExchangeRate(DepthBook depthBook) {
        BigDecimal exchangeRate = context.getExchangeRateOfUSDT2USD();
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            e.setPrice(e.getPrice().multiply(exchangeRate));
        });
        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            e.setPrice(e.getPrice().multiply(exchangeRate));
        });
    }

    /**
     * 使用费率和收益率修正价格
     *
     * @param depthBook
     */
    private void adjPriceByFee(DepthBook depthBook, StrategyOrderConfig config) {
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            BigDecimal newPrice = e.getPrice().multiply(BigDecimal.ONE.subtract(config.getContractFee()))
                    .multiply(BigDecimal.ONE.subtract(config.getSpotFee()))
                    .multiply(BigDecimal.ONE.subtract(config.getDeliveryFee()))
                    .multiply(BigDecimal.ONE.subtract(config.getExpectYields()));
            e.setPrice(newPrice);
        });
        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            BigDecimal newPrice = e.getPrice().multiply(BigDecimal.ONE.add(config.getContractFee()))
                    .multiply(BigDecimal.ONE.add(config.getSpotFee()))
                    .multiply(BigDecimal.ONE.add(config.getDeliveryFee()))
                    .multiply(BigDecimal.ONE.add(config.getExpectYields()));
            e.setPrice(newPrice);
        });
    }


    /**
     * 根据配置合并深度
     *
     * @param depthBook
     * @param config
     */
    private void mergeDepth(DepthBook depthBook, StrategyOrderConfig config) {
        BigDecimal priceStep = config.getPriceStep();
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            e.setPrice(priceRoundUp(e.getPrice(), priceStep));
        });
        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            e.setPrice(priceRoundDown(e.getPrice(), priceStep));
        });

        Map<BigDecimal, List<DepthBook.Depth>> asksMap = asks.stream().collect(Collectors.groupingBy(e -> e.getPrice()));
        List<DepthBook.Depth> mergedAsks = new ArrayList<>();
        asksMap.forEach((k, v) -> {
            BigDecimal sum = BigDecimal.ZERO;
            for (DepthBook.Depth depth : v) {
                sum = sum.add(depth.getAmount());
            }
            mergedAsks.add(new DepthBook.Depth(k, sum));
        });
        depthBook.setAsks(mergedAsks);

        Map<BigDecimal, List<DepthBook.Depth>> bidsMap = bids.stream().collect(Collectors.groupingBy(e -> e.getPrice()));
        List<DepthBook.Depth> mergedBids = new ArrayList<>();
        bidsMap.forEach((k, v) -> {
            BigDecimal sum = BigDecimal.ZERO;
            for (DepthBook.Depth depth : v) {
                sum = sum.add(depth.getAmount());
            }
            mergedBids.add(new DepthBook.Depth(k, sum));
        });
        depthBook.setBids(mergedBids);

    }


    private static BigDecimal priceRoundUp(BigDecimal price, BigDecimal priceStep) {
        return price.divide(priceStep, 0, BigDecimal.ROUND_FLOOR).add(BigDecimal.ONE).multiply(priceStep);
    }

    private static BigDecimal priceRoundDown(BigDecimal price, BigDecimal priceStep) {
        return price.divide(priceStep, 0, BigDecimal.ROUND_FLOOR).multiply(priceStep);
    }


    /**
     * 设置每个深度的每个深度的数量
     *
     * @param depthBook
     * @param config
     */
    private void adjMaxAmount(DepthBook depthBook, StrategyOrderConfig config) {
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            BigDecimal amount = e.getAmount();
            if (BigDecimalUtils.moreThanOrEquals(amount, BigDecimal.valueOf(config.getAsksMaxAmount()))) {
                e.setAmount(BigDecimal.valueOf(config.getAsksMaxAmount()));
            }
        });

        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            BigDecimal amount = e.getAmount();
            if (BigDecimalUtils.moreThanOrEquals(amount, BigDecimal.valueOf(config.getBidsMaxAmount()))) {
                e.setAmount(BigDecimal.valueOf(config.getBidsMaxAmount()));
            }
        });
    }


    private void adjBasisPrice(DepthBook depthBook, StrategyOrderConfig config) {
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            e.setPrice(e.getPrice().add(config.getAsksBasisPrice()));
        });

        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            e.setPrice(e.getPrice().add(config.getBidsBasisPrice()));
        });
    }

    private void sortDepthBook(DepthBook depthBook) {
        depthBook.getAsks().sort(Comparator.comparing(DepthBook.Depth::getPrice));
        depthBook.getBids().sort(Comparator.comparing(DepthBook.Depth::getPrice).reversed());
    }
}
