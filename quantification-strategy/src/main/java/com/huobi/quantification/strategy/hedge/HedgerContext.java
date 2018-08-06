package com.huobi.quantification.strategy.hedge;

import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.dao.StrategyHedgeConfigMapper;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.strategy.config.StrategyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HedgerContext {

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

    @Autowired
    private StrategyHedgeConfigMapper strategyHedgeConfigMapper;

    @Autowired
    private SpotOrderService spotOrderService;

    public void init(StrategyProperties.ConfigGroup group) {

    }

    public StrategyHedgeConfig getStrategyHedgeConfig() {
        StrategyHedgeConfig hedgeConfig = strategyHedgeConfigMapper.selectByPrimaryKey(1);
        return hedgeConfig;
    }


    public void placeBuyOrder(BigDecimal orderPrice, BigDecimal orderAmount) {
        placeOrder(SideEnum.BUY.getSideType(), orderPrice, orderAmount);
    }

    public void placeSellOrder(BigDecimal orderPrice, BigDecimal orderAmount) {
        placeOrder(SideEnum.SELL.getSideType(), orderPrice, orderAmount);
    }

    private void placeOrder(int side, BigDecimal orderPrice, BigDecimal orderAmount) {
        SpotPlaceOrderReqDto spotPlaceOrderReqDto = new SpotPlaceOrderReqDto();
        spotPlaceOrderReqDto.setExchangeId(spotExchangeId);
        spotPlaceOrderReqDto.setAccountId(spotAccountId);
        spotPlaceOrderReqDto.setBaseCoin(spotBaseCoin);
        spotPlaceOrderReqDto.setQuoteCoin(spotQuoteCoin);
        spotPlaceOrderReqDto.setQuantity(orderAmount);
        spotPlaceOrderReqDto.setPrice(orderPrice);
        spotPlaceOrderReqDto.setSide(side + "");
        spotPlaceOrderReqDto.setOrderType("limit");
        // todo 检查数量
        spotOrderService.placeOrder(spotPlaceOrderReqDto);
    }


}
