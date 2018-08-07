package com.huobi.quantification.strategy.hedge;

import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.dao.StrategyHedgeConfigMapper;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.entity.QuanExchangeConfig;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.config.ExchangeConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.entity.DepthBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HedgerContext {


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
        spotExchangeConfig = ExchangeConfig.getExchangeConfig(spotExchangeId, spotBaseCoin, spotQuoteCoin);
        futureExchangeConfig = ExchangeConfig.getExchangeConfig(futureExchangeId, spotBaseCoin, spotQuoteCoin);
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


    public void placeHedgeOrder(BigDecimal netPosition) {
        // 3. 获取买一卖一价格
        DepthBook depthBook = commContext.getDepth();
        BigDecimal ask1 = depthBook.getAsk1();
        BigDecimal bid1 = depthBook.getBid1();

        if (BigDecimalUtils.moreThan(netPosition, BigDecimal.ZERO)) {
            BigDecimal orderPrice = ask1.multiply(BigDecimal.ONE.add(hedgeConfig.getSlippage()));
            BigDecimal orderAmount = netPosition.divide(orderPrice);
            placeBuyOrder(orderPrice, orderAmount);
        } else if (BigDecimalUtils.lessThan(netPosition, BigDecimal.ZERO)) {
            BigDecimal orderPrice = bid1.multiply(BigDecimal.ONE.subtract(hedgeConfig.getSlippage()));
            BigDecimal orderAmount = netPosition.divide(orderPrice);
            placeSellOrder(orderPrice, orderAmount);
        }
    }

    public void setHedgeConfig(StrategyHedgeConfig hedgeConfig) {
        this.hedgeConfig = hedgeConfig;
    }


}
