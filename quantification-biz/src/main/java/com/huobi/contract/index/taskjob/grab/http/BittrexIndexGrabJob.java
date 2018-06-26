package com.huobi.contract.index.taskjob.grab.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.ContractPriceIndexHis;
import com.huobi.contract.index.entity.Ticker;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class BittrexIndexGrabJob extends AbstractHttpGrab {
    /**
     * 该交易所被封禁的HTTP响应码
     */
    private  final static int banIpStatusCode = -1;
    @Override
    public ExchangeEnum getExchange() {
        return ExchangeEnum.BITTREX;
    }

    @Override
    protected Ticker parseIndexPrice(String response, ExchangeIndex exchangeIndex,Date requestTime) {
        //{"success":true,"message":"","result":{"Bid":0.00209999,"Ask":0.00210990,"Last":0.00210990}}
        JSONObject resultObject = JSON.parseObject(response);
        JSONObject result = resultObject.getJSONObject("result");
        BigDecimal lastPrice = result.getBigDecimal("Last");
        return new Ticker(lastPrice, requestTime);
    }

    @Override
    protected int getBanIpSattusCode() {
        return banIpStatusCode;
    }
}
