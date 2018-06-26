package com.huobi.contract.index.ws.huobi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.huobi.contract.index.ws.GZIPUtils;
import com.huobi.contract.index.ws.WebSocketClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public abstract class AbstractHuobiWSClient extends WebSocketClient implements InitializingBean {

    @Autowired
    protected BaseIndexGrabService baseIndexGrabService;

    @Autowired
    protected ExchangeInfoMapper exchangeInfoMapper;

    @Autowired
    private RedisService redisService;

    @Override
    protected void handleOpen() {
        send(getSubContent());
    }

    @Override
    protected void handleMessage(byte[] message) {
        String s = new String(GZIPUtils.uncompressToString(message));
        logger.info("huobi收到原始数据：" + s);
        if (s.startsWith("{\"ping\"")) {
            HashMap<String, Long> map = new HashMap<>();
            map.put("pong", new Date().getTime());
            send(JSON.toJSONString(map));
            send(getSubContent());
        } else {
            JSONObject tick = JSON.parseObject(s).getJSONObject("tick");
            if (tick == null) {
                return;
            }
            JSONArray data = tick.getJSONArray("data");
            if (data.size() > 0) {
                JSONObject jsonObject = data.getJSONObject(0);
                long ts = jsonObject.getLongValue("ts");
                BigDecimal price = jsonObject.getBigDecimal("price");
                doHandleMessage(price, ts);
            }
        }
    }

    public abstract String getSubContent();

    public abstract String getIndexSymbol();

    public void doHandleMessage(BigDecimal price, long timestamp) {
        if (BigDecimal.ZERO.equals(price)) {
            return;
        }
        logger.info("WebSocket接收到数据:exchangeName={},symbol={},price={}", ExchangeEnum.HUOBI.getExchangeName(), getIndexSymbol(), price);
        redisService.updateIndexRealtimePrice(ExchangeEnum.HUOBI.getExchangeName(), getIndexSymbol(), price);
    }

    @Override
    public void afterPropertiesSet() {
        setUri(Constant.HUOBI_WSS);
        connect();
        ConnectionStatusManager.register(ExchangeEnum.HUOBI, getIndexSymbol(), this);
        logger.info("websocket connect success");
    }
}
