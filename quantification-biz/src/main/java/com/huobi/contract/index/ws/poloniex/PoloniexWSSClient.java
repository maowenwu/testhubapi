package com.huobi.contract.index.ws.poloniex;


import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.contract.index.service.impl.BaseIndexGrabService;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.ws.ConnectionStatusManager;
import com.huobi.contract.index.ws.Constant;
import com.huobi.contract.index.ws.WebSocketClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


public class PoloniexWSSClient extends WebSocketClient implements InitializingBean {

    @Autowired
    private RedisService redisService;

    @Autowired
    protected BaseIndexGrabService baseIndexGrabService;

    @Autowired
    protected ExchangeInfoMapper exchangeInfoMapper;

    @Override
    protected void handleOpen() {
        send("{\"command\":\"subscribe\",\"channel\":1002}");
    }

    @Override
    protected void handleMessage(String message) {
        PoloniexWSSTicker ticker = PoloniexWSSTicker.mapMessageToPoloniexTicker(message);
        if (ticker != null) {
            TickerEnum tickerEnum = TickerEnum.valueOfInt(ticker.getCurrencyPair().intValue());
            if (TickerEnum.ETC_BTC == tickerEnum || TickerEnum.XRP_BTC == tickerEnum) {
                doHandleMessage(tickerEnum.getSymbol(), ticker.getLastPrice());
            }
        }
    }

    private void doHandleMessage(String symbol, BigDecimal price) {
        if (BigDecimal.ZERO.equals(price)) {
            return;
        }
        logger.info("WebSocket接收到数据:exchangeName={},symbol={},price={}", ExchangeEnum.POLONIEX.getExchangeName(), symbol, price);
        redisService.updateIndexRealtimePrice(ExchangeEnum.POLONIEX.getExchangeName(), symbol, price);
    }

    @Override
    public void afterPropertiesSet() {
        setUri(Constant.POLONIEX_WSS);
        connect();
        ConnectionStatusManager.register(ExchangeEnum.POLONIEX, "ETC-USD", this);
        ConnectionStatusManager.register(ExchangeEnum.POLONIEX, "XRP-USD", this);
        logger.info("websocket connect success");
    }
}
