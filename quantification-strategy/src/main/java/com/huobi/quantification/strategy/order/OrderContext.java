package com.huobi.quantification.strategy.order;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.dao.StrategyOrderConfigMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.entity.FutureBalance;
import com.huobi.quantification.strategy.entity.FuturePosition;
import com.huobi.quantification.strategy.entity.SpotBalance;
import com.huobi.quantification.strategy.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class OrderContext {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpotMarketService spotMarketService;
    @Autowired
    private FutureContractService futureContractService;
    @Autowired
    private SpotAccountService spotAccountService;
    @Autowired
    private FutureAccountService futureAccountService;
    @Autowired
    private StrategyOrderConfigMapper strategyOrderConfigMapper;
    @Autowired
    private FutureOrderService futureOrderService;
    @Autowired
    private CommContext commContext;

    private BigDecimal faceValue = BigDecimal.valueOf(100);

    private StrategyMetric strategyMetric = new StrategyMetric();

    private OrderReader orderReader;
    private FuturePosition futurePosition;
    private StrategyOrderConfig config;
    private FutureBalance futureBalance;
    private SpotBalance spotBalance;
    private BigDecimal exchangeRate = BigDecimal.ONE;
    private BigDecimal currPrice;


    private Integer futureExchangeId;
    private Long futureAccountId;
    private Integer futureLever;
    private String futureContractType;
    private String futureContractCode;
    private String futureBaseCoin;
    private String futureQuoteCoin;
    private String futureCoinType;

    private Integer spotExchangeId;
    private String spotBaseCoin;
    private String spotQuoteCoin;

    public void init(StrategyProperties.ConfigGroup group) {
        StrategyProperties.Config future = group.getFuture();
        StrategyProperties.Config spot = group.getSpot();
        this.futureExchangeId = future.getExchangeId();
        this.futureAccountId = future.getAccountId();
        this.futureLever = future.getLever();
        this.futureContractCode = Objects.requireNonNull(future.getContractCode());
        this.futureContractType = commContext.getContractTypeFromCode();
        this.futureBaseCoin = future.getBaseCoin();
        this.futureQuoteCoin = future.getQuotCoin();
        this.futureCoinType = future.getBaseCoin();

        this.spotExchangeId = spot.getExchangeId();
        this.spotBaseCoin = spot.getBaseCoin();
        this.spotQuoteCoin = spot.getQuotCoin();
    }

    public DepthBook getDepth() {
        SpotDepthReqDto reqDto = new SpotDepthReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setBaseCoin(spotBaseCoin);
        reqDto.setQuoteCoin(spotQuoteCoin);
        ServiceResult<SpotDepthRespDto> result = spotMarketService.getDepth(reqDto);
        if (result.isSuccess()) {
            SpotDepthRespDto.DataBean data = result.getData().getData();
            DepthBook depthBook = new DepthBook();
            data.getAsks().forEach(e -> {
                depthBook.getAsks().add(new DepthBook.Depth(e.getPrice(), e.getAmount()));
            });
            data.getBids().forEach(e -> {
                depthBook.getBids().add(new DepthBook.Depth(e.getPrice(), e.getAmount()));
            });
            return depthBook;
        } else {
            throw new RuntimeException("获取火币现货深度异常");
        }
    }


    public StrategyOrderConfig getStrategyOrderConfig() {
        StrategyOrderConfig orderConfig = strategyOrderConfigMapper.selectByPrimaryKey(1);
        if (orderConfig != null) {
            logger.info("获取订单策略参数：" + JSON.toJSONString(orderConfig));
        }
        return orderConfig;
    }

    public Map<BigDecimal, List<FutureOrder>> getActiveOrderMap() {
        FuturePriceOrderReqDto reqDto = new FuturePriceOrderReqDto();
        reqDto.setExchangeId(this.futureExchangeId);
        reqDto.setAccountId(this.futureAccountId);
        ServiceResult<FuturePriceOrderRespDto> activeOrderMap = futureOrderService.getActiveOrderMap(reqDto);
        if (activeOrderMap.isSuccess()) {
            Map<BigDecimal, List<FuturePriceOrderRespDto.DataBean>> priceOrderMap = activeOrderMap.getData().getPriceOrderMap();
            Map<BigDecimal, List<FutureOrder>> result = new HashMap<>();
            priceOrderMap.forEach((k, v) -> {
                List<FutureOrder> list = new ArrayList<>();
                v.forEach(e -> {
                    FutureOrder futureOrder = new FutureOrder();
                    BeanUtils.copyProperties(e, futureOrder);
                    list.add(futureOrder);
                });
                result.put(k, list);
            });
            return result;
        } else {
            throw new RuntimeException("获取活跃订单map失败，exchangeId=" + this.futureExchangeId + " futureAccountId=" + this.futureAccountId);
        }
    }


    /**
     * 取消价格不在深度列表中的所有订单
     *
     * @param depthBook
     */
    public List<FutureOrder> cancelOrderNotInDepthBook(DepthBook depthBook) {
        List<DepthBook.Depth> allDepth = new ArrayList<>();
        allDepth.addAll(depthBook.getAsks());
        allDepth.addAll(depthBook.getBids());
        List<BigDecimal> allPrice = allDepth.stream().map(e -> e.getPrice()).collect(Collectors.toList());

        List<Long> preCancelOrderIds = new ArrayList<>();
        Map<BigDecimal, List<FutureOrder>> orderMap = getActiveOrderMap();
        orderMap.forEach((k, v) -> {
            if (!allPrice.contains(k)) {
                List<Long> list = v.stream().map(e -> e.getExOrderId()).collect(Collectors.toList());
                preCancelOrderIds.addAll(list);
            }
        });

        if (CollectionUtils.isNotEmpty(preCancelOrderIds)) {
            cancelOrder(preCancelOrderIds);
        }

        List<FutureOrder> remainOrders = new ArrayList<>();
        orderMap.forEach((k, v) -> {
            v.stream().forEach(e -> {
                if (!preCancelOrderIds.contains(e.getExOrderId())) {
                    remainOrders.add(e);
                }
            });
        });
        return remainOrders;
    }


    // 下买单
    public void placeBuyOrder(BigDecimal price, BigDecimal orderAmount) {
        FuturePosition.Position shortPosi = futurePosition.getShortPosi();
        if (shortPosi != null && BigDecimalUtils.moreThan(shortPosi.getAvailable(), config.getMaxShortPosition())) {
            // 空仓的可平量
            BigDecimal shortAvailable = shortPosi.getAvailable();
            // 如果有持仓，那么下平仓单
            // 如果预期量大于可平量，那么就按可平量下单，剩余部分忽略
            if (BigDecimalUtils.moreThan(orderAmount, shortAvailable)) {
                boolean success = placeBuyCloseOrder(price, shortAvailable);
                if (success) {
                    // 下平仓单后减去可平量
                    shortPosi.setAvailable(shortPosi.getAvailable().subtract(shortAvailable));
                }
            } else {
                // 如果预期量小于等于可平量，那么按预期量下单
                boolean success = placeBuyCloseOrder(price, orderAmount);
                if (success) {
                    // 下平仓单后减去可平量
                    shortPosi.setAvailable(shortPosi.getAvailable().subtract(orderAmount));
                }
            }
        } else {
            placeBuyOpenOrder(price, orderAmount);
        }
    }

    public void placeSellOrder(BigDecimal price, BigDecimal orderAmount) {
        FuturePosition.Position longPosi = futurePosition.getLongPosi();
        // 下卖单，先看下多仓可用持仓是否超过最大允许持仓，如果超过那么下平仓单。
        // 设置最大允许持仓主要是为了能保留一点持仓，如果一点都不需要那么可以设置为0
        if (longPosi != null && BigDecimalUtils.moreThan(longPosi.getAvailable(), config.getMaxLongPosition())) {
            // 空仓的可平量
            BigDecimal longAvailable = longPosi.getAvailable();
            // 如果有持仓，那么下平仓单
            // 如果预期量大于可平量，那么就按可平量下单，剩余部分忽略
            if (BigDecimalUtils.moreThan(orderAmount, longAvailable)) {
                boolean success = placeSellCloseOrder(price, longAvailable);
                if (success) {
                    // 下平仓单后减去可平量
                    longPosi.setAvailable(longPosi.getAvailable().subtract(longAvailable));
                }
            } else {
                // 如果预期量小于等于可平量，那么按预期量下单
                boolean success = placeSellCloseOrder(price, orderAmount);
                if (success) {
                    // 下平仓单后减去可平量
                    longPosi.setAvailable(longPosi.getAvailable().subtract(orderAmount));
                }
            }
        } else {
            placeSellOpenOrder(price, orderAmount);
        }
    }

    // 下开仓买单
    private void placeBuyOpenOrder(BigDecimal price, BigDecimal orderAmount) {
        BigDecimal positionTotal = orderReader.getBidPositionTotal();
        // 当已有仓位量大于配置允许的量，那么不下单
        if (BigDecimalUtils.moreThanOrEquals(positionTotal, config.getLongMaxAmount())) {
            logger.warn("买入开仓单数量已经超过限制，忽略该笔下单，当前总持仓：{}，配置的最大下单量：{}", positionTotal, config.getLongMaxAmount());
            strategyMetric.ignoreBuyOpenOrderIncrement();
            return;
        }
        BigDecimal targetAmount = calcAvailableAmount(SideEnum.BUY, OffsetEnum.LONG, price, orderAmount);
        if (BigDecimalUtils.moreThan(targetAmount, BigDecimal.ZERO)) {
            Long orderId = placeOrder(SideEnum.BUY.getSideType(), OffsetEnum.LONG.getOffset(), price, targetAmount);
            if (orderId != null) {
                strategyMetric.successBuyOpenOrderAdd(orderId);
            } else {
                strategyMetric.failedBuyOpenOrderIncrement();
            }
        } else {
            strategyMetric.ignoreBuyOpenOrderIncrement();
            logger.warn("可用买入开仓数量为0，忽略该笔下单");
        }
    }


    // 下开仓卖单
    public void placeSellOpenOrder(BigDecimal price, BigDecimal orderAmount) {
        BigDecimal positionTotal = orderReader.getAskPositionTotal();
        // 当已有仓位量大于配置允许的量，那么不下单
        if (BigDecimalUtils.moreThanOrEquals(positionTotal, config.getShortMaxAmount())) {
            strategyMetric.ignoreSellOpenOrderIncrement();
            logger.warn("卖出开仓单数量已经超过限制，忽略该笔下单，当前总持仓：{}，配置的最大下单量：{}", positionTotal, config.getShortMaxAmount());
            return;
        }
        BigDecimal targetAmount = calcAvailableAmount(SideEnum.SELL, OffsetEnum.LONG, price, orderAmount);
        if (BigDecimalUtils.moreThan(targetAmount, BigDecimal.ZERO)) {
            Long orderId = placeOrder(SideEnum.SELL.getSideType(), OffsetEnum.LONG.getOffset(), price, targetAmount);
            if (orderId != null) {
                strategyMetric.successSellOpenOrderAdd(orderId);
            } else {
                strategyMetric.failedSellOpenOrderIncrement();
            }
        } else {
            strategyMetric.ignoreSellOpenOrderIncrement();
        }
    }

    // 下平仓买单
    private boolean placeBuyCloseOrder(BigDecimal price, BigDecimal orderAmount) {
        BigDecimal targetAmount = calcAvailableAmount(SideEnum.BUY, OffsetEnum.SHORT, price, orderAmount);
        if (BigDecimalUtils.moreThan(targetAmount, BigDecimal.ZERO)) {
            Long orderId = placeOrder(SideEnum.BUY.getSideType(), OffsetEnum.SHORT.getOffset(), price, targetAmount);
            if (orderId != null) {
                strategyMetric.successBuyCloseOrderAdd(orderId);
                return true;
            } else {
                strategyMetric.failedBuyCloseOrderIncrement();
                return false;
            }
        } else {
            strategyMetric.ignoreBuyCloseOrderIncrement();
            return false;
        }
    }

    // 下平仓卖单
    public boolean placeSellCloseOrder(BigDecimal price, BigDecimal orderAmount) {
        BigDecimal targetAmount = calcAvailableAmount(SideEnum.SELL, OffsetEnum.SHORT, price, orderAmount);
        if (BigDecimalUtils.moreThan(targetAmount, BigDecimal.ZERO)) {
            Long orderId = placeOrder(SideEnum.SELL.getSideType(), OffsetEnum.SHORT.getOffset(), price, targetAmount);
            if (orderId != null) {
                strategyMetric.successSellCloseOrderAdd(orderId);
                return true;
            } else {
                strategyMetric.failedSellCloseOrderIncrement();
                return false;
            }
        } else {
            strategyMetric.ignoreSellCloseOrderIncrement();
            return false;
        }
    }

    /**
     * @param sideEnum    开仓 or 平仓
     * @param price       价格 美元
     * @param orderAmount 开仓量 张
     * @return
     */
    private BigDecimal calcAvailableAmount(SideEnum sideEnum, OffsetEnum offsetEnum, BigDecimal price, BigDecimal orderAmount) {
        // 根据期货账户计算出最多可开仓的数量
        BigDecimal minAmount;
        if (offsetEnum == OffsetEnum.LONG) {
            // 如果是开仓，那么需要判断账户余额是否能开这么多张
            // 可用余额单位为btc币
            BigDecimal marginBalance = futureBalance.getMarginAvailable().subtract(config.getContractMarginReserve());
            // 期货可开张数=余额*杠杆*价格*汇率/面值
            BigDecimal amount = marginBalance.multiply(BigDecimal.valueOf(this.futureLever)).multiply(price).divide(faceValue, 18, BigDecimal.ROUND_DOWN);
            minAmount = min(amount, orderAmount);
        } else {
            // 如果是平仓，那么不需要关心账户余额
            minAmount = orderAmount;
        }

        if (sideEnum == SideEnum.BUY) {
            // 看下现货账户是否有足够的币卖出
            BigDecimal minBalance = min(minAmount.multiply(price), spotBalance.getCoin().getAvailable().subtract(config.getSpotCoinReserve()).multiply(price));
            BigDecimal contractAmount = minBalance.divide(price, 0, BigDecimal.ROUND_FLOOR);
            return contractAmount;
        } else {
            // 看下现货账户是否有足够的usdt买币
            BigDecimal usdBalance = minAmount.multiply(price);
            // 查看现货账户可用usdt
            BigDecimal minBalance = min(usdBalance, spotBalance.getUsdt().getAvailable().subtract(config.getSpotBalanceReserve()).multiply(exchangeRate));
            BigDecimal contractAmount = minBalance.divide(price, 0, BigDecimal.ROUND_FLOOR);
            return contractAmount;
        }
    }


    private BigDecimal min(BigDecimal a, BigDecimal b) {
        if (BigDecimalUtils.lessThan(a, b)) {
            return a;
        } else {
            return b;
        }
    }

    public boolean cancelOrder(List<Long> orderIds) {
        List<Long> successOrderId = new ArrayList<>();
        List<Long> failedOrderId = new ArrayList<>();
        for (Long orderId : orderIds) {
            FutureCancelSingleOrderReqDto reqDto = new FutureCancelSingleOrderReqDto();
            reqDto.setExchangeId(this.futureExchangeId);
            reqDto.setAccountId(this.futureAccountId);
            reqDto.setBaseCoin(this.futureBaseCoin);
            reqDto.setQuoteCoin(this.futureQuoteCoin);
            reqDto.setContractType(this.futureContractType);
            reqDto.setExOrderId(orderId);
            ServiceResult<Long> result = futureOrderService.cancelSingleOrder(reqDto);
            if (result.isSuccess()) {
                successOrderId.add(orderId);
            } else {
                failedOrderId.add(orderId);
            }
        }
        logger.info("取消订单总数：{}，取消成功id：{}，取消失败id：{}", orderIds.size(), successOrderId, failedOrderId);
        return failedOrderId.size() == 0;
    }


    public void setOrderReader(OrderReader orderReader) {
        this.orderReader = orderReader;
    }

    public void setFuturePosition(FuturePosition futurePosition) {
        this.futurePosition = futurePosition;
    }

    public void setConfig(StrategyOrderConfig config) {
        this.config = config;
    }

    public void setFutureBalance(FutureBalance futureBalance) {
        this.futureBalance = futureBalance;
    }

    public void setSpotBalance(SpotBalance spotBalance) {
        this.spotBalance = spotBalance;
    }


    public Long placeOrder(int side, int offset, BigDecimal price, BigDecimal orderAmount) {
        FuturePlaceOrderReqDto reqDto = new FuturePlaceOrderReqDto();
        reqDto.setExchangeId(this.futureExchangeId);
        reqDto.setAccountId(this.futureAccountId);
        reqDto.setBaseCoin(this.futureBaseCoin);
        reqDto.setQuoteCoin(this.futureQuoteCoin);
        reqDto.setContractType(this.futureContractType);
        reqDto.setContractCode(this.futureContractCode);
        reqDto.setSide(side);
        reqDto.setOffset(offset);
        reqDto.setOrderType("limit");
        reqDto.setPrice(price);
        reqDto.setQuantity(orderAmount);
        reqDto.setLever(this.futureLever);
        reqDto.setSync(true);
        try {
            ServiceResult<FuturePlaceOrderRespDto> result = futureOrderService.placeOrder(reqDto);
            if (result.isSuccess()) {
                Long exOrderId = result.getData().getExOrderId();
                postPlaceOrder(exOrderId, side, offset, price, orderAmount);
                return exOrderId;
            } else {
                logger.error("placeOrder失败，订单：" + reqDto);
            }
        } catch (Throwable e) {
            logger.error("dubbo服务调用异常，placeOrder失败，订单：" + reqDto, e);
        }
        return null;
    }

    private void postPlaceOrder(Long exOrderId, int side, int offset, BigDecimal price, BigDecimal orderAmount) {
        FutureOrder futureOrder = new FutureOrder();
        futureOrder.setExOrderId(exOrderId);
        futureOrder.setSide(side);
        futureOrder.setOffset(offset);
        futureOrder.setOrderPrice(price);
        futureOrder.setOrderQty(orderAmount);
        futureOrder.setDealQty(BigDecimal.ZERO);
        orderReader.addOrder(futureOrder);
    }


    public boolean updateOrderInfo() {
        ServiceResult result = null;
        try {
            result = futureOrderService.updateOrderInfo(this.futureExchangeId, this.futureAccountId);
        } catch (Exception e) {
            logger.error("更新订单信息，dubbo调用失败，exchangeId={}，futureAccountId={}", this.futureExchangeId, this.futureAccountId);
            return false;
        }
        if (result.isSuccess()) {
            logger.info("更新订单信息成功，exchangeId={}，futureAccountId={}", this.futureExchangeId, this.futureAccountId);
            return true;
        } else {
            logger.error("更新订单信息失败，exchangeId={}，futureAccountId={}", this.futureExchangeId, this.futureAccountId);
            return false;
        }
    }


    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setCurrPrice(BigDecimal currPrice) {
        this.currPrice = currPrice;
    }


    public void resetMetric() {
        strategyMetric.reset();
    }

    public void metricBuyOrder() {
        strategyMetric.metricBuyOrder();
    }

    public void metricSellOrder() {
        strategyMetric.metricSellOrder();
    }
}
