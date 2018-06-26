package com.huobi.contract.index.taskjob.grab.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.Ticker;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class GeminiIndexGrabJob extends AbstractHttpGrab {

    /**
     * 该交易所被封禁的HTTP响应码
     */
    private  final static int banIpStatusCode = -1;
    @Override
    public ExchangeEnum getExchange() {
        return ExchangeEnum.GEMINI;
    }

    @Override
    protected Ticker parseIndexPrice(String response, ExchangeIndex exchangeIndex,Date requestTime) {
        //{"bid":"621.20","ask":"622.07","volume":{"ETH":"8047.13637628","USD":"4948586.4570964514","timestamp":1528075800000},"last":"620.97"}
        JSONObject jsonObject = JSON.parseObject(response);
        BigDecimal lastPrice = jsonObject.getBigDecimal("last");
        return new Ticker(lastPrice, requestTime);
    }

    @Override
    protected int getBanIpSattusCode() {
        return banIpStatusCode;
    }
}
