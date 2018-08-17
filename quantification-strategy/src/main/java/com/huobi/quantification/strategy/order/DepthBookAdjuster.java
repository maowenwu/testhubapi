package com.huobi.quantification.strategy.order;

import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.entity.QuanExchangeConfig;
import com.huobi.quantification.entity.StrategyInstanceConfig;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.entity.StrategyTradeFee;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.ExchangeConfig;
import com.huobi.quantification.strategy.entity.DepthBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DepthBookAdjuster {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommContext commContext;

    private Integer futureExchangeId;
    private String futureBaseCoin;
    private String futureQuoteCoin;

    private QuanExchangeConfig futureExchangeConfig;

    private StrategyOrderConfig orderConfig;
    private StrategyTradeFee tradeFeeConfig;
    private BigDecimal exchangeRate;

    public void init(StrategyInstanceConfig config) {
        this.futureExchangeId = config.getFutureExchangeId();
        this.futureBaseCoin = config.getFutureBaseCoin();
        this.futureQuoteCoin = config.getFutureQuotCoin();

        this.futureExchangeConfig = ExchangeConfig.getExchangeConfig(futureExchangeId, futureBaseCoin, futureQuoteCoin);
        if (futureExchangeConfig == null) {
            throw new RuntimeException("获取交易所配置失败，futureExchangeId=" + futureExchangeId + "，futureBaseCoin=" + futureBaseCoin + "，futureQuoteCoin=" + futureQuoteCoin);
        }
    }

    public DepthBook getAdjustedDepthBook() {
        try {
            DepthBook depthBook = commContext.getSpotDepth();
            // 如果币币现货是以非USD计价，则所有买卖价格，需要乘以汇率，转化为USD计价
            adjPriceByExchangeRate(exchangeRate, depthBook);
            // 将比特币转为张，值为小数
            calcVolume(depthBook);
            // 考虑手续费和收益率，买卖单调整后价格
            adjPriceByFee(depthBook);
            // 对买卖盘进行深度合并
            mergeDepth(depthBook);
            // 深度合并后，对每个深度的数量，乘以一个拷贝系数+随机，得到数量调整后的买卖盘列表
            adjCopyFactor(depthBook);
            // 考虑到基差以及挂单远近，将所有买单价格下调p元，卖单上调q元
            adjBasisPrice(depthBook);
            // 对买卖盘进行排序
            sortDepthBook(depthBook);
            // 每个价格对应的数量不能超过阈值，超过则取数量=阈值
            adjAmount(depthBook);
            // 下单量调整为整数
            roundDownVolume(depthBook);
            logger.info("DepthBook, asks数量：{}，bids数量：{}", depthBook.getAsks().size(), depthBook.getBids().size());
            return depthBook;
        } catch (Exception e) {
            logger.error("获取深度信息失败", e);
            return null;
        }
    }

    private void roundDownVolume(DepthBook depthBook) {
        List<DepthBook.Depth> asks = depthBook.getAsks();
        List<DepthBook.Depth> bids = depthBook.getBids();
        asks.forEach(e -> {
            e.setAmount(e.getAmount().divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN));
        });
        bids.forEach(e -> {
            e.setAmount(e.getAmount().divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN));
        });
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
    private void adjPriceByFee(DepthBook depthBook) {
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            BigDecimal newPrice = e.getPrice()
                    .multiply(BigDecimal.ONE.add(tradeFeeConfig.getContractFee()))
                    .multiply(BigDecimal.ONE.add(tradeFeeConfig.getSpotFee()))
                    .multiply(BigDecimal.ONE.add(tradeFeeConfig.getDeliveryFee()))
                    .multiply(BigDecimal.ONE.add(orderConfig.getExpectYields()));
            e.setPrice(newPrice);
        });
        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            BigDecimal newPrice = e.getPrice()
                    .multiply(BigDecimal.ONE.subtract(tradeFeeConfig.getContractFee()))
                    .multiply(BigDecimal.ONE.subtract(tradeFeeConfig.getSpotFee()))
                    .multiply(BigDecimal.ONE.subtract(tradeFeeConfig.getDeliveryFee()))
                    .multiply(BigDecimal.ONE.subtract(orderConfig.getExpectYields()));
            e.setPrice(newPrice);
        });
    }


    /**
     * 根据配置合并深度
     *
     * @param depthBook
     * @param config
     */
    private void mergeDepth(DepthBook depthBook) {
        BigDecimal priceStep = orderConfig.getPriceStep();
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
        return price.divide(priceStep, 0, BigDecimal.ROUND_CEILING).multiply(priceStep);
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
    private void adjAmount(DepthBook depthBook) {
        BigDecimal maxAmountPerPrice = orderConfig.getMaxAmountPerPrice();
        BigDecimal minAmountPerPrice = orderConfig.getMinAmountPerPrice();
        List<DepthBook.Depth> asks = depthBook.getAsks();
        List<DepthBook.Depth> newAsks = new ArrayList<>();
        asks.forEach(e -> {
            BigDecimal amount = e.getAmount();
            if (BigDecimalUtils.moreThanOrEquals(amount, maxAmountPerPrice)) {
                e.setAmount(maxAmountPerPrice);
            }
            if (BigDecimalUtils.moreThanOrEquals(amount, minAmountPerPrice)) {
                newAsks.add(e);
            }
        });
        depthBook.setAsks(newAsks);

        List<DepthBook.Depth> bids = depthBook.getBids();
        List<DepthBook.Depth> newBids = new ArrayList<>();
        bids.forEach(e -> {
            BigDecimal amount = e.getAmount();
            if (BigDecimalUtils.moreThanOrEquals(amount, maxAmountPerPrice)) {
                e.setAmount(maxAmountPerPrice);
            }
            if (BigDecimalUtils.moreThanOrEquals(amount, minAmountPerPrice)) {
                newBids.add(e);
            }
        });
        depthBook.setBids(newBids);
    }

    private void adjCopyFactor(DepthBook depthBook) {
        BigDecimal maxFactor = orderConfig.getMaxCopyFactor();
        BigDecimal minFactor = orderConfig.getMinCopyFactor();
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            BigDecimal factor = BigDecimal.valueOf(Math.random()).multiply(maxFactor.subtract(minFactor)).add(minFactor);
            e.setAmount(e.getAmount().multiply(factor));
        });

        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            BigDecimal factor = BigDecimal.valueOf(Math.random()).multiply(maxFactor.subtract(minFactor)).add(minFactor);
            e.setAmount(e.getAmount().multiply(factor));
        });
    }

    private void adjBasisPrice(DepthBook depthBook) {
        List<DepthBook.Depth> asks = depthBook.getAsks();
        asks.forEach(e -> {
            e.setPrice(e.getPrice().add(orderConfig.getAsksBasisPrice()));
        });

        List<DepthBook.Depth> bids = depthBook.getBids();
        bids.forEach(e -> {
            e.setPrice(e.getPrice().add(orderConfig.getBidsBasisPrice()));
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
            BigDecimal volume = e.getPrice().multiply(e.getAmount()).divide(futureExchangeConfig.getFaceValue(), 18, BigDecimal.ROUND_FLOOR);
            e.setAmount(volume);
        });

        depthBook.getBids().forEach(e -> {
            BigDecimal volume = e.getPrice().multiply(e.getAmount()).divide(futureExchangeConfig.getFaceValue(), 18, BigDecimal.ROUND_FLOOR);
            e.setAmount(volume);
        });
    }

    public void setOrderConfig(StrategyOrderConfig orderConfig) {
        this.orderConfig = orderConfig;
    }

    public void setTradeFeeConfig(StrategyTradeFee tradeFeeConfig) {
        this.tradeFeeConfig = tradeFeeConfig;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
