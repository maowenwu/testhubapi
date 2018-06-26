package com.huobi.contract.index.taskjob.grab.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.Ticker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service("huobiIndexGrabJob")
public class HuobiIndexGrabJob extends AbstractHttpGrab {
    /**
     * 该交易所被封禁的HTTP响应码
     */
    private  final static int banIpStatusCode = -1;
    @Override
    public ExchangeEnum getExchange() {
        return ExchangeEnum.HUOBI;
    }

    @Override
    protected Ticker parseIndexPrice(String response, ExchangeIndex exchangeIndex,Date requestTime) {
        //{"status":"ok","ch":"market.etcbtc.trade.detail","ts":1528076117435,"tick":{"id":8596707356,"ts":1528076095879,"data":[{"amount":8.3469,"ts":1528076095879,"id":85967073565307961719,"price":0.002103,"direction":"sell"}]}}
        JSONObject jsonObject = JSON.parseObject(response);
        JSONObject tick = jsonObject.getJSONObject("tick");
        JSONArray data = tick.getJSONArray("data");
        if (data.size() > 0) {
            BigDecimal price = data.getJSONObject(0).getBigDecimal("price");
            String timeStr = data.getJSONObject(0).getString("ts");
            //解析毫秒的时间戳
            Date responseTime = DateUtil.parseStrTimestampMilliSecondToDate(timeStr);
            return new Ticker(price, responseTime);
        }
        return new Ticker(BigDecimal.ZERO, requestTime);
    }

    @Override
    protected int getBanIpSattusCode() {
        return banIpStatusCode;
    }
}
