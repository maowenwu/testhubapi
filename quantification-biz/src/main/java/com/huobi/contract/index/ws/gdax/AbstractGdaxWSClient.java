package com.huobi.contract.index.ws.gdax;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.common.util.DateUtil;
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

import java.math.BigDecimal;

public abstract class AbstractGdaxWSClient extends WebSocketClient implements InitializingBean {

    @Autowired
    private RedisService redisService;

    @Override
    protected void handleMessage(String message) {
        logger.debug("WS接收到原始数据:" + message);
        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        if ("match".equals(type)) {
            BigDecimal price = jsonObject.getBigDecimal("price");
            String time = jsonObject.getString("time");
            doHandleMessage(price, time);
        }
    }

    protected abstract String getIndexSymbol();

    public void doHandleMessage(BigDecimal price, String timeStr) {
        if (BigDecimal.ZERO.equals(price)) {
            return;
        }
        logger.info("WebSocket接收到数据:exchangeName={},symbol={},price={}", ExchangeEnum.GDAX.getExchangeName(), getIndexSymbol(), price);
        redisService.updateIndexRealtimePrice(ExchangeEnum.GDAX.getExchangeName(), getIndexSymbol(), price);
    }

    @Override
    public void afterPropertiesSet() {
        setUri(Constant.GDAX_WSS);
        //connect();
        logger.info("gdax 开始连接");
        ConnectionStatusManager.register(ExchangeEnum.GDAX, getIndexSymbol(), this);
        logger.info("websocket connect success");
    }

}
