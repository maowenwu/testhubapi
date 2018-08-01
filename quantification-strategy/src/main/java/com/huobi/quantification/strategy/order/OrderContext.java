package com.huobi.quantification.strategy.order;

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
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.order.entity.FutureBalance;
import com.huobi.quantification.strategy.order.entity.FuturePosition;
import com.huobi.quantification.strategy.order.entity.SpotBalance;
import com.huobi.quantification.strategy.order.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Scope("prototype")
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

    /**
     * 用于表示开仓平仓方向,true 代表开仓
     */
    private AtomicBoolean isBuyOpen = new AtomicBoolean(true);

    private AtomicBoolean isSellOpen = new AtomicBoolean(true);

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
        this.futureContractType = future.getContractType();
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
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(3000);
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

    public BigDecimal getExchangeRateOfUSDT2USD() {
        for (int i = 0; i < 3; i++) {
            ServiceResult<BigDecimal> result = futureContractService.getExchangeRateOfUSDT2USD();
            if (result.isSuccess()) {
                return result.getData();
            } else {
                continue;
            }
        }
        throw new RuntimeException("获取usdt兑usd汇率异常");
    }

    public SpotBalance getSpotBalance() {
        SpotBalance spotBalance = new SpotBalance();

        SpotBalance.Coin coin = new SpotBalance.Coin();
        coin.setAvailable(BigDecimal.valueOf(9999999999999999L));
        spotBalance.setCoin(coin);

        SpotBalance.Usdt usdt = new SpotBalance.Usdt();
        usdt.setAvailable(BigDecimal.valueOf(9999999999999999L));
        spotBalance.setUsdt(usdt);
        return spotBalance;

        /*SpotBalance spotBalance = new SpotBalance();
        try {
            ServiceResult<SpotBalanceRespDto> balance = spotAccountService.getBalance(null);
            Map<String, SpotBalanceRespDto.DataBean> data = balance.getData().getData();
            SpotBalanceRespDto.DataBean dataBean = data.get(futureCoinType);
            if (dataBean != null) {
                SpotBalance.Coin coin = new SpotBalance.Coin();
                BeanUtils.copyProperties(dataBean, coin);
                spotBalance.setCoin(coin);
            }
            SpotBalanceRespDto.DataBean usdtBean = data.get("usdt");
            if (usdtBean != null) {
                SpotBalance.Usdt usdt = new SpotBalance.Usdt();
                BeanUtils.copyProperties(dataBean, usdt);
                spotBalance.setUsdt(usdt);
            }
            return spotBalance;
        } catch (BeansException e) {
            logger.error("获取现货资产信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}", exchangeId, futureAccountId, futureCoinType, e);
            return null;
        }*/
    }


    public FutureBalance getFutureBalance() {
        try {
            FutureBalanceReqDto reqDto = new FutureBalanceReqDto();
            reqDto.setExchangeId(this.futureExchangeId);
            reqDto.setAccountId(this.futureAccountId);
            reqDto.setCoinType(this.futureCoinType);
            reqDto.setTimeout(100);
            reqDto.setMaxDelay(3000);
            ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(reqDto);
            Map<String, FutureBalanceRespDto.DataBean> data = balance.getData().getData();
            FutureBalanceRespDto.DataBean dataBean = data.get(futureCoinType);
            if (dataBean == null) {
                dataBean = data.get(futureCoinType.toUpperCase());
            }
            if (dataBean != null) {
                FutureBalance futureBalance = new FutureBalance();
                BeanUtils.copyProperties(dataBean, futureBalance);
                return futureBalance;
            } else {
                return null;
            }
        } catch (BeansException e) {
            logger.error("获取期货资产信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}", this.futureExchangeId, futureAccountId, futureCoinType, e);
            return null;
        }
    }

    public FuturePosition getFuturePosition() {
        try {
            FuturePositionReqDto reqDto = new FuturePositionReqDto();
            reqDto.setExchangeId(this.futureExchangeId);
            reqDto.setAccountId(this.futureAccountId);
            reqDto.setCoinType(this.futureCoinType);
            reqDto.setTimeout(100);
            reqDto.setMaxDelay(3000);
            ServiceResult<FuturePositionRespDto> position = futureAccountService.getPosition(reqDto);
            Map<String, List<FuturePositionRespDto.DataBean>> data = position.getData().getData();
            List<FuturePositionRespDto.DataBean> beanList = data.get(this.futureCoinType);
            FuturePosition futurePosition = new FuturePosition();
            beanList.forEach(e -> {
                if (e.getLongAmount() != null) {
                    FuturePosition.LongPosi longPosi = new FuturePosition.LongPosi();
                    BeanUtils.copyProperties(e, longPosi);
                    futurePosition.setLongPosi(longPosi);
                }
                if (e.getShortAmount() != null) {
                    FuturePosition.ShortPosi shortPosi = new FuturePosition.ShortPosi();
                    BeanUtils.copyProperties(e, shortPosi);
                    futurePosition.setShortPosi(shortPosi);
                }
            });
            return futurePosition;
        } catch (Exception e) {
            logger.error("获取期货持仓信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}", futureExchangeId, futureAccountId, futureCoinType, e);
            return null;
        }
    }


    public StrategyOrderConfig getStrategyOrderConfig() {
        StrategyOrderConfig orderConfig = strategyOrderConfigMapper.selectByPrimaryKey(1);
        logger.info("获取策略配置：" + orderConfig);
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
        FuturePosition.LongPosi longPosi = this.futurePosition.getLongPosi();
        if (longPosi == null) {
            isBuyOpen.set(true);
        } else {
            if (BigDecimalUtils.moreThan(longPosi.getLongAmount(), config.getMaxPositionAmount())) {
                isBuyOpen.set(false);
            } else if (BigDecimalUtils.lessThan(longPosi.getLongAmount(), config.getMinPositionAmount())) {
                isBuyOpen.set(true);
            }
        }

        if (isBuyOpen.get()) {
            placeBuyOpenOrder(price, orderAmount);
        } else {
            FuturePosition.ShortPosi shortPosi = futurePosition.getShortPosi();
            if (shortPosi != null) {
                // 空仓的可平量
                BigDecimal shortAvailable = shortPosi.getShortAvailable();
                if (BigDecimalUtils.moreThan(shortAvailable, BigDecimal.ZERO)) {
                    // 如果有持仓，那么下平仓单
                    // 如果预期量大于可平量，那么就按可平量下单，剩余部分忽略
                    if (BigDecimalUtils.moreThan(orderAmount, shortAvailable)) {
                        boolean success = placeBuyCloseOrder(price, shortAvailable);
                        if (success) {
                            // 下平仓单后减去可平量
                            shortPosi.setShortAvailable(shortPosi.getShortAvailable().subtract(shortAvailable));
                        }
                    } else {
                        // 如果预期量小于等于可平量，那么按预期量下单
                        boolean success = placeBuyCloseOrder(price, orderAmount);
                        if (success) {
                            // 下平仓单后减去可平量
                            shortPosi.setShortAvailable(shortPosi.getShortAvailable().subtract(orderAmount));
                        }
                    }
                } else {
                    logger.warn("当前开仓已达到最大限制，并且可平量为0，所以忽略该订单。price={},orderAmount={}", price, orderAmount);
                }
            } else {
                logger.warn("当前开仓已达到最大限制，并且无仓可平，所以忽略该订单。price={},orderAmount={}", price, orderAmount);
            }
        }
    }

    public void placeSellOrder(BigDecimal price, BigDecimal orderAmount) {
        FuturePosition.ShortPosi shortPosi = this.futurePosition.getShortPosi();
        if (shortPosi == null) {
            isSellOpen.set(true);
        } else {
            if (BigDecimalUtils.moreThan(shortPosi.getShortAmount(), config.getMaxPositionAmount())) {
                isSellOpen.set(false);
            } else if (BigDecimalUtils.lessThan(shortPosi.getShortAmount(), config.getMinPositionAmount())) {
                isSellOpen.set(true);
            }
        }

        if (isSellOpen.get()) {
            placeSellOpenOrder(price, orderAmount);
        } else {
            FuturePosition.LongPosi longPosi = futurePosition.getLongPosi();
            if (longPosi != null) {
                // 空仓的可平量
                BigDecimal longAvailable = longPosi.getLongAvailable();
                if (BigDecimalUtils.moreThan(longAvailable, BigDecimal.ZERO)) {
                    // 如果有持仓，那么下平仓单
                    // 如果预期量大于可平量，那么就按可平量下单，剩余部分忽略
                    if (BigDecimalUtils.moreThan(orderAmount, longAvailable)) {
                        boolean success = placeSellCloseOrder(price, longAvailable);
                        if (success) {
                            // 下平仓单后减去可平量
                            longPosi.setLongAvailable(longPosi.getLongAvailable().subtract(longAvailable));
                        }
                    } else {
                        // 如果预期量小于等于可平量，那么按预期量下单
                        boolean success = placeSellCloseOrder(price, orderAmount);
                        if (success) {
                            // 下平仓单后减去可平量
                            longPosi.setLongAvailable(longPosi.getLongAmount().subtract(orderAmount));
                        }
                    }
                } else {
                    logger.warn("当前开仓已达到最大限制，并且可平量为0，所以忽略该订单。price={},orderAmount={}", price, orderAmount);
                }
            } else {
                logger.warn("当前开仓已达到最大限制，并且无仓可平，所以忽略该订单。price={},orderAmount={}", price, orderAmount);
            }
        }
    }

    // 下开仓买单
    private void placeBuyOpenOrder(BigDecimal price, BigDecimal orderAmount) {
        BigDecimal positionTotal = orderReader.getBidPositionTotal();
        // 当已有仓位量大于配置允许的量，那么不下单
        if (BigDecimalUtils.moreThanOrEquals(positionTotal, config.getLongMaxAmount())) {
            logger.warn("买入开仓单数量已经超过限制，忽略该笔下单，当前总持仓：{}，配置的最大下单量：{}", positionTotal, config.getLongMaxAmount());
            return;
        }
        BigDecimal targetAmount = calcAvailableAmount(SideEnum.BUY, price, orderAmount);
        if (BigDecimalUtils.moreThan(targetAmount, BigDecimal.ZERO)) {
            placeOrder(SideEnum.BUY.getSideType(), OffsetEnum.LONG.getOffset(), price, targetAmount);
        }
    }


    // 下开仓卖单
    public void placeSellOpenOrder(BigDecimal price, BigDecimal orderAmount) {
        BigDecimal positionTotal = orderReader.getAskPositionTotal();
        // 当已有仓位量大于配置允许的量，那么不下单
        if (BigDecimalUtils.moreThanOrEquals(positionTotal, config.getShortMaxAmount())) {
            logger.warn("卖出开仓单数量已经超过限制，忽略该笔下单，当前总持仓：{}，配置的最大下单量：{}", positionTotal, config.getShortMaxAmount());
            return;
        }
        BigDecimal targetAmount = calcAvailableAmount(SideEnum.SELL, price, orderAmount);
        if (BigDecimalUtils.moreThan(targetAmount, BigDecimal.ZERO)) {
            placeOrder(SideEnum.SELL.getSideType(), OffsetEnum.LONG.getOffset(), price, targetAmount);
        }
    }

    // 下平仓买单
    private boolean placeBuyCloseOrder(BigDecimal price, BigDecimal orderAmount) {
        Long orderId = placeOrder(SideEnum.BUY.getSideType(), OffsetEnum.SHORT.getOffset(), price, orderAmount);
        if (orderId != null) {
            return true;
        } else {
            return false;
        }
    }

    // 下平仓卖单
    public boolean placeSellCloseOrder(BigDecimal price, BigDecimal orderAmount) {
        Long orderId = placeOrder(SideEnum.SELL.getSideType(), OffsetEnum.SHORT.getOffset(), price, orderAmount);
        if (orderId != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param sideEnum    开仓 or 平仓
     * @param price       价格 美元
     * @param orderAmount 开仓量 张
     * @return
     */
    private BigDecimal calcAvailableAmount(SideEnum sideEnum, BigDecimal price, BigDecimal orderAmount) {
        // 根据期货账户计算出最多可开仓的数量
        BigDecimal marginBalance = futureBalance.getMarginAvailable().subtract(config.getContractMarginReserve());
        BigDecimal amount = marginBalance.divide(price, 0, BigDecimal.ROUND_FLOOR);
        BigDecimal minAmount = min(amount, orderAmount);

        if (sideEnum == SideEnum.BUY) {
            // 看下现货账户是否有足够的币卖出
            BigDecimal usdtBalance = minAmount.multiply(price).divide(exchangeRate, 18, BigDecimal.ROUND_FLOOR);
            BigDecimal coinAmount = usdtBalance.divide(currPrice);
            // 查看现货账户可用btc币量
            BigDecimal minCoin = min(coinAmount, spotBalance.getCoin().getAvailable());
            BigDecimal contractAmount = minCoin.multiply(currPrice).multiply(exchangeRate).divide(price, 0, BigDecimal.ROUND_FLOOR);
            return contractAmount;
        } else {
            // 看下现货账户是否有足够的usdt买币
            BigDecimal usdtBalance = minAmount.multiply(price).divide(exchangeRate, 18, BigDecimal.ROUND_FLOOR);
            // 查看现货账户可用usdt
            BigDecimal minBalance = min(usdtBalance, spotBalance.getUsdt().getAvailable());
            BigDecimal contractAmount = minBalance.multiply(exchangeRate).divide(price, 0, BigDecimal.ROUND_FLOOR);
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

    public void cancelOrder(List<Long> orderIds) {
        orderIds.forEach(e -> {
            FutureCancelSingleOrderReqDto reqDto = new FutureCancelSingleOrderReqDto();
            reqDto.setExchangeId(this.futureExchangeId);
            reqDto.setAccountId(this.futureAccountId);
            reqDto.setBaseCoin(this.futureBaseCoin);
            reqDto.setQuoteCoin(this.futureQuoteCoin);
            reqDto.setContractType(this.futureContractType);
            reqDto.setExOrderId(e);
            ServiceResult<Long> result = futureOrderService.cancelSingleOrder(reqDto);
            if (!result.isSuccess()) {
                logger.error("取消订单失败，exchangeId={},futureAccountId={},exOrderId={}", this.futureExchangeId, this.futureAccountId, e);
            }
        });
        logger.info("取消订单总数：{}，订单id：{}", orderIds.size(), orderIds);
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
                return result.getData().getExOrderId();
            } else {
                logger.error("placeOrder失败，订单：" + reqDto);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.error("dubbo服务调用异常，placeOrder失败，订单：" + reqDto);
        }
        return null;
    }

    public void cancelAllOrder() {
        ServiceResult result = futureOrderService.cancelAllOrder(null);
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

    public BigDecimal getSpotCurrentPrice() {
        SpotCurrentPriceReqDto reqDto = new SpotCurrentPriceReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setBaseCoin(spotBaseCoin);
        reqDto.setQuoteCoin(spotQuoteCoin);
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(3000);
        try {
            ServiceResult<SpotCurrentPriceRespDto> currentPrice = spotMarketService.getCurrentPrice(reqDto);
            if (currentPrice.isSuccess()) {
                return currentPrice.getData().getCurrentPrice();
            }
        } catch (Exception e) {
            logger.error("获取当前价格失败，exchangeId={}，futureBaseCoin={}，futureQuoteCoin={}", futureExchangeId, futureBaseCoin, futureQuoteCoin);
        }
        return null;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setCurrPrice(BigDecimal currPrice) {
        this.currPrice = currPrice;
    }
}
