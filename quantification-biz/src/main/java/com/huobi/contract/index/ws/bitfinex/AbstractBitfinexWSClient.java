package com.huobi.contract.index.ws.bitfinex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.ws.ConnectionStatusManager;
import com.huobi.contract.index.ws.Constant;
import com.huobi.contract.index.ws.WebSocketClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public abstract class AbstractBitfinexWSClient extends WebSocketClient implements InitializingBean {

    @Autowired
    private RedisService redisService;

    @Override
    protected void handleMessage(String message) {
        logger.debug("WS接收到原始数据:" + message);
        Object obj = JSON.parse(message);
        if (obj instanceof JSONArray) {
            JSONArray arr = (JSONArray) obj;
            logger.debug(arr.toJSONString());
            if (arr.size() == 7 && "tu".equals(arr.getString(1))) {
                BigDecimal price = arr.getBigDecimal(5);
                doHandleMessage(price);
            }
        }
    }

    protected abstract String getIndexSymbol();

    public void doHandleMessage(BigDecimal price) {
        if (BigDecimal.ZERO.equals(price)) {
            return;
        }
        logger.info("WebSocket接收到数据:exchangeName={},symbol={},price={}", ExchangeEnum.BITFINEX.getExchangeName(), getIndexSymbol(), price);
        redisService.updateIndexRealtimePrice(ExchangeEnum.BITFINEX.getExchangeName(), getIndexSymbol(), price);
    }

    @Override
    public void afterPropertiesSet() {
        setUri(Constant.BITFINEX_WSS);
        connect();
        ConnectionStatusManager.register(ExchangeEnum.BITFINEX, getIndexSymbol(), this);
        logger.info("websocket connect success");
    }
}
