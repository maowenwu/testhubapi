package com.huobi.quantification.strategy.hedge;

import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.entity.QuanExchangeConfig;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import com.huobi.quantification.entity.StrategyInstanceConfig;
import com.huobi.quantification.entity.StrategyTradeFee;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.SpotBalanceMock;
import com.huobi.quantification.strategy.config.ExchangeConfig;
import com.huobi.quantification.strategy.entity.DepthBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HedgerContext {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommContext commContext;
    @Autowired
    private SpotOrderService spotOrderService;

    private Integer spotExchangeId;
    private Long spotAccountId;
    private String spotBaseCoin;
    private String spotQuoteCoin;

    private QuanExchangeConfig spotExchangeConfig;

    private StrategyHedgeConfig hedgeConfig;
    private StrategyTradeFee tradeFeeConfig;


    public void init(StrategyInstanceConfig config) {
        this.spotExchangeId = config.getSpotExchangeId();
        this.spotAccountId = config.getSpotAccountId();
        this.spotBaseCoin = config.getSpotBaseCoin();
        this.spotQuoteCoin = config.getSpotQuotCoin();

        spotExchangeConfig = ExchangeConfig.getExchangeConfig(spotExchangeId, spotBaseCoin, spotQuoteCoin);
        if (spotExchangeConfig == null) {
            throw new RuntimeException("获取现货交易所配置失败，这里需要使用到价格精度和数量精度");
        }
    }


    public void placeBuyOrder(BigDecimal orderPrice, BigDecimal orderAmount) {
        orderPrice = checkPrice(orderPrice);
        orderAmount = checkAmount(orderAmount);
        placeOrder(SideEnum.BUY, orderPrice, orderAmount);
        // todo 这里是用的mock对象
        SpotBalanceMock.setUsdt(SpotBalanceMock.getUsdt().subtract(orderPrice.multiply(orderAmount)));
        SpotBalanceMock.setCoin(SpotBalanceMock.getCoin().add(orderAmount));
    }

    public void placeSellOrder(BigDecimal orderPrice, BigDecimal orderAmount) {
        orderPrice = checkPrice(orderPrice);
        orderAmount = checkAmount(orderAmount);
        placeOrder(SideEnum.SELL, orderPrice, orderAmount);
        // todo 这里是用的mock对象
        SpotBalanceMock.setUsdt(SpotBalanceMock.getUsdt().add(orderPrice.multiply(orderAmount)));
        SpotBalanceMock.setCoin(SpotBalanceMock.getCoin().subtract(orderPrice.multiply(orderAmount)));
    }

    private BigDecimal checkPrice(BigDecimal price) {
        return price.divide(BigDecimal.ONE, spotExchangeConfig.getPricePrecision(), BigDecimal.ROUND_DOWN);
    }

    private BigDecimal checkAmount(BigDecimal amount) {
        return amount.divide(BigDecimal.ONE, spotExchangeConfig.getAmountPrecision(), BigDecimal.ROUND_DOWN).abs();
    }

    private void placeOrder(SideEnum side, BigDecimal orderPrice, BigDecimal orderAmount) {
        // 检查数量、价格
        if (BigDecimalUtils.moreThan(orderPrice, BigDecimal.ZERO) && BigDecimalUtils.moreThan(orderAmount, BigDecimal.ZERO)) {
            SpotPlaceOrderReqDto spotPlaceOrderReqDto = new SpotPlaceOrderReqDto();
            spotPlaceOrderReqDto.setExchangeId(spotExchangeId);
            spotPlaceOrderReqDto.setAccountId(spotAccountId);
            spotPlaceOrderReqDto.setBaseCoin(spotBaseCoin);
            spotPlaceOrderReqDto.setQuoteCoin(spotQuoteCoin);
            spotPlaceOrderReqDto.setQuantity(orderAmount);
            spotPlaceOrderReqDto.setPrice(orderPrice);
            if (side == SideEnum.BUY) {
                spotPlaceOrderReqDto.setSide("buy");
            } else {
                spotPlaceOrderReqDto.setSide("sell");
            }
            spotPlaceOrderReqDto.setOrderType("limit");

            ServiceResult<SpotPlaceOrderRespDto> result = spotOrderService.placeOrder(spotPlaceOrderReqDto);
            if (result.isSuccess()) {
                logger.info("下单成功，方向：{}，价格：{}，数量：{}", side, orderPrice, orderAmount);
            } else {
                logger.info("下单失败，方向：{}，价格：{}，数量：{}", side, orderPrice, orderAmount);
            }
        } else {
            logger.info("价格或数量<=0，忽略此单，orderPrice={}，orderAmount={}", orderPrice, orderAmount);
        }
    }


    public void placeHedgeOrder(BigDecimal netPosition, boolean isDelivery) {
        // 如果当前净头寸小于配置，那么直接忽略
        if (BigDecimalUtils.lessThan(netPosition.abs(), hedgeConfig.getMinNetPosition())) {
            return;
        }
        // 获取买一卖一价格
        DepthBook depthBook = commContext.getSpotDepth();
        BigDecimal ask1 = depthBook.getAsk1();
        BigDecimal bid1 = depthBook.getBid1();
        // 净头寸大于0，下买单
        if (BigDecimalUtils.moreThan(netPosition, BigDecimal.ZERO)) {
            if (ask1 != null) {
                BigDecimal orderPrice;
                if (isDelivery) {
                    orderPrice = ask1.multiply(BigDecimal.ONE.add(hedgeConfig.getDeliveryBuySlippage()));
                } else {
                    orderPrice = ask1.multiply(BigDecimal.ONE.add(hedgeConfig.getBuySlippage()));
                }
                BigDecimal orderAmount = netPosition.divide(orderPrice, 18, BigDecimal.ROUND_DOWN);
                placeBuyOrder(orderPrice, orderAmount);
            } else {
                logger.error("卖一价为空，忽略本笔对冲单");
            }
        } else if (BigDecimalUtils.lessThan(netPosition, BigDecimal.ZERO)) {
            // 净头寸小于0，下卖单
            if (bid1 != null) {
                BigDecimal orderPrice;
                if (isDelivery) {
                    orderPrice = bid1.multiply(BigDecimal.ONE.subtract(hedgeConfig.getDeliverySellSlippage()));
                } else {
                    orderPrice = bid1.multiply(BigDecimal.ONE.subtract(hedgeConfig.getSellSlippage()));
                }
                BigDecimal orderAmount = netPosition.divide(orderPrice, 18, BigDecimal.ROUND_DOWN)
                        .divide(BigDecimal.ONE.subtract(tradeFeeConfig.getSpotFee()), 18, BigDecimal.ROUND_DOWN);
                placeSellOrder(orderPrice, orderAmount);
            } else {
                logger.error("买一价为空，忽略本笔对冲单");
            }
        }
    }


    public void setHedgeConfig(StrategyHedgeConfig hedgeConfig) {
        this.hedgeConfig = hedgeConfig;
    }

    public void setTradeFeeConfig(StrategyTradeFee tradeFeeConfig) {
        this.tradeFeeConfig = tradeFeeConfig;
    }
}
