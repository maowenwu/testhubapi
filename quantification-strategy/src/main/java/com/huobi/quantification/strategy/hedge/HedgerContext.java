package com.huobi.quantification.strategy.hedge;

import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.dao.StrategyHedgeConfigMapper;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.entity.QuanExchangeConfig;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.SpotBalanceMock;
import com.huobi.quantification.strategy.config.ExchangeConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.entity.DepthBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class HedgerContext {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommContext commContext;
    @Autowired
    private StrategyHedgeConfigMapper strategyHedgeConfigMapper;
    @Autowired
    private SpotOrderService spotOrderService;


    private Integer futureExchangeId;
    private Long futureAccountId;
    private Integer futureLever;
    private String futureContractType;
    private String futureContractCode;
    private String futureBaseCoin;
    private String futureQuoteCoin;
    private String futureCoinType;

    private Integer spotExchangeId;
    private Long spotAccountId;
    private String spotBaseCoin;
    private String spotQuoteCoin;


    private QuanExchangeConfig spotExchangeConfig;
    private QuanExchangeConfig futureExchangeConfig;
    private StrategyHedgeConfig hedgeConfig;


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
        this.spotAccountId = spot.getAccountId();
        this.spotBaseCoin = spot.getBaseCoin();
        this.spotQuoteCoin = spot.getQuotCoin();

        spotExchangeConfig = ExchangeConfig.getExchangeConfig(spotExchangeId, spotBaseCoin, spotQuoteCoin);
        futureExchangeConfig = ExchangeConfig.getExchangeConfig(futureExchangeId, futureBaseCoin, futureQuoteCoin);
    }

    public StrategyHedgeConfig getStrategyHedgeConfig() {
        StrategyHedgeConfig hedgeConfig = strategyHedgeConfigMapper.selectByPrimaryKey(1);
        return hedgeConfig;
    }


    public void placeBuyOrder(BigDecimal orderPrice, BigDecimal orderAmount) {
        orderPrice = checkPrice(orderPrice);
        orderAmount = checkAmount(orderAmount);
        placeOrder(SideEnum.BUY, orderPrice, orderAmount);
        // todo
        SpotBalanceMock.setUsdt(SpotBalanceMock.getUsdt().subtract(orderPrice.multiply(orderAmount)));
        SpotBalanceMock.setCoin(SpotBalanceMock.getCoin().add(orderAmount));
    }

    public void placeSellOrder(BigDecimal orderPrice, BigDecimal orderAmount) {
        orderPrice = checkPrice(orderPrice);
        orderAmount = checkAmount(orderAmount);
        placeOrder(SideEnum.SELL, orderPrice, orderAmount);
        // todo
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
            logger.info("价格或数量<=0，忽略此单，orderPrice={}，orderAmount{}", orderPrice, orderAmount);
        }
    }


    public void placeHedgeOrder(BigDecimal netPosition) {
        // 3. 获取买一卖一价格
        DepthBook depthBook = commContext.getDepth();
        BigDecimal ask1 = depthBook.getAsk1();
        BigDecimal bid1 = depthBook.getBid1();

        if (BigDecimalUtils.moreThan(netPosition, BigDecimal.ZERO)) {
            BigDecimal orderPrice = ask1.multiply(BigDecimal.ONE.add(hedgeConfig.getSlippage()));
            BigDecimal orderAmount = netPosition.divide(orderPrice, 18, BigDecimal.ROUND_DOWN);
            placeBuyOrder(orderPrice, orderAmount);
        } else if (BigDecimalUtils.lessThan(netPosition, BigDecimal.ZERO)) {
            BigDecimal orderPrice = bid1.multiply(BigDecimal.ONE.subtract(hedgeConfig.getSlippage()));
            BigDecimal orderAmount = netPosition.divide(orderPrice, 18, BigDecimal.ROUND_DOWN)
                    .divide(BigDecimal.ONE.subtract(hedgeConfig.getSpotFee()), 18, BigDecimal.ROUND_DOWN);
            placeSellOrder(orderPrice, orderAmount);
        }
    }

    public void setHedgeConfig(StrategyHedgeConfig hedgeConfig) {
        this.hedgeConfig = hedgeConfig;
    }


}
