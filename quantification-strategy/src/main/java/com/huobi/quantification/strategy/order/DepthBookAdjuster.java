package com.huobi.quantification.strategy.order;

import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.strategy.entity.DepthBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DepthBookAdjuster {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private BigDecimal faceValue = BigDecimal.valueOf(100);

    private OrderContext context;


    public DepthBookAdjuster(OrderContext context) {
        this.context = context;
    }

    public DepthBook getAdjustedDepthBook(BigDecimal exchangeRate, StrategyOrderConfig config) {
        try {
            DepthBook depthBook = context.getDepth();
            // 如果币币现货是以非USD计价，则所有买卖价格，需要乘以汇率，转化为USD计价
            adjPriceByExchangeRate(exchangeRate, depthBook);
            // 考虑手续费和收益率，买卖单调整后价格
            adjPriceByFee(depthBook, config);
            // 对买卖盘进行深度合并
            mergeDepth(depthBook, config);
            // 深度合并后，对每个深度的数量，乘以一个拷贝系数+随机，得到数量调整后的买卖盘列表
            adjCopyFactor(depthBook, config);
            // 每个价格对应的数量不能超过阈值，超过则取数量=阈值
            adjMaxAmount(depthBook, config);
            // 考虑到基差以及挂单远近，将所有买单价格下调p元，卖单上调q元
            adjBasisPrice(depthBook, config);
            // 对买卖盘进行排序
            sortDepthBook(depthBook);
            // 将比特币转为张
            calcVolume(depthBook);
            logger.info("DepthBook, asks数量：{}，bids数量：{}", depthBook.getAsks().size(), depthBook.getBids().size());
            return depthBook;
        } catch (Exception e) {
            logger.error("获取深度信息失败", e);
            return null;
        }
    }





    /**
     * 使用汇率修正价格
     *
     * @param depthBook
     */
    private void adjPriceByExchangeRate(BigDecimal exchangeRate, DepthBook depthBook) {
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
     * 设置每个价格的每个深度的数量
     *
     * @param depthBook
     * @param config
     */
    private void adjMaxAmount(DepthBook depthBook, StrategyOrderConfig config) {
        Integer maxAmountPerPrice = config.getMaxAmountPerPrice();
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            BigDecimal amount = e.getAmount();
            if (BigDecimalUtils.moreThanOrEquals(amount, BigDecimal.valueOf(maxAmountPerPrice))) {
                e.setAmount(BigDecimal.valueOf(maxAmountPerPrice));
            }
        });

        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            BigDecimal amount = e.getAmount();
            if (BigDecimalUtils.moreThanOrEquals(amount, BigDecimal.valueOf(maxAmountPerPrice))) {
                e.setAmount(BigDecimal.valueOf(maxAmountPerPrice));
            }
        });
    }

    private void adjCopyFactor(DepthBook depthBook, StrategyOrderConfig config) {
        BigDecimal maxFactor = config.getMaxCopyFactor();
        BigDecimal minFactor = config.getMinCopyFactor();
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            BigDecimal factor=BigDecimal.valueOf(Math.random()).multiply(maxFactor.subtract(minFactor)).add(minFactor);
            e.setAmount(e.getAmount().multiply(factor));
        });

        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            BigDecimal factor=BigDecimal.valueOf(Math.random()).multiply(maxFactor.subtract(minFactor)).add(minFactor);
            e.setAmount(e.getAmount().multiply(factor));
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

    /**
     * 将比特币转为张
     *
     * @param depthBook
     */
    private void calcVolume(DepthBook depthBook) {
        depthBook.getAsks().forEach(e -> {
            BigDecimal volume = e.getPrice().multiply(e.getAmount()).divide(faceValue, 0, BigDecimal.ROUND_FLOOR);
            e.setAmount(volume);
        });

        depthBook.getBids().forEach(e -> {
            BigDecimal volume = e.getPrice().multiply(e.getAmount()).divide(faceValue, 0, BigDecimal.ROUND_FLOOR);
            e.setAmount(volume);
        });
    }

}
