package com.huobi.contract.index.ws.bitstamp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.contract.index.service.impl.BaseIndexGrabService;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.ws.ConnectionStatus;
import com.huobi.contract.index.ws.ConnectionStatusManager;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public abstract class AbstractPusher implements ConnectionStatus {

    @Autowired
    private RedisService redisService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static final String apiKey = "de504dc5763aeef9ff52";

    protected boolean reconnecting = true;

    Pusher pusher = new Pusher(apiKey, new PusherOptions());

    public void connect(String channelName) {

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                if (change.getCurrentState() == ConnectionState.CONNECTED) {
                    reconnecting = false;
                    onOpen();
                } else {
                    reconnecting = true;
                }
            }

            @Override
            public void onError(String message, String code, Exception e) {
                onFailed(e);
            }
        }, ConnectionState.ALL);

        // live_trades_xrpbtc
        Channel channel = pusher.subscribe(channelName);

        channel.bind("trade", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                try {
                    onMessage(data);
                } catch (Throwable t) {
                    logger.error("websocket 消息处理异常", t);
                }
            }
        });

    }

    protected void onOpen() {

    }

    protected abstract String getIndexSymbol();

    protected void onMessage(String data) {
        logger.info("bitstamp收到原始数据："+data);
        JSONObject jsonObject = JSON.parseObject(data);
        BigDecimal price = jsonObject.getBigDecimal("price");
        //long timestamp = jsonObject.getLongValue("timestamp");
        if (BigDecimal.ZERO.equals(price)) {
            return;
        }
        logger.info("WebSocket接收到数据:exchangeName={},symbol={},price={}", ExchangeEnum.BITSTAMP.getExchangeName(), getIndexSymbol(), price);
        redisService.updateIndexRealtimePrice(ExchangeEnum.BITSTAMP.getExchangeName(), getIndexSymbol(), price);
    }

    protected void onFailed(Exception e) {
        logger.error("websocket connect fail");
    }

    @Override
    public boolean isReconnecting() {
        return reconnecting;
    }
}
