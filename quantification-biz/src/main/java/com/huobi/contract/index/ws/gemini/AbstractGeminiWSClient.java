package com.huobi.contract.index.ws.gemini;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.contract.index.service.impl.BaseIndexGrabService;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.ws.ConnectionStatusManager;
import com.huobi.contract.index.ws.WebSocketClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public abstract class AbstractGeminiWSClient extends WebSocketClient implements InitializingBean {

    @Autowired
    private RedisService redisService;

    @Override
    protected void handleMessage(String message) {
        if (message.contains("trade")) {
            logger.debug(message);
            JSONArray events = JSON.parseObject(message).getJSONArray("events");
            for (Object obj : events) {
                if (obj instanceof JSONObject) {
                    String type = ((JSONObject) obj).getString("type");
                    if ("trade".equals(type)) {
                        BigDecimal price = ((JSONObject) obj).getBigDecimal("price");
                        doHandleMessage(price);
                    }
                }
            }
        }
    }

    public abstract String getConnUrl();

    public abstract String getIndexSymbol();

    public void doHandleMessage(BigDecimal price) {
        if (BigDecimal.ZERO.equals(price)) {
            return;
        }
        logger.info("WebSocket接收到数据:exchangeName={},symbol={},price={}", ExchangeEnum.GEMINI.getExchangeName(), getIndexSymbol(), price);
        redisService.updateIndexRealtimePrice(ExchangeEnum.GEMINI.getExchangeName(), getIndexSymbol(), price);
    }

    @Override
    public void afterPropertiesSet() {
        setUri(getConnUrl());
        connect();
        ConnectionStatusManager.register(ExchangeEnum.GEMINI, getIndexSymbol(), this);
        logger.info("websocket connect success");
    }
}
