package com.huobi.contract.index.taskjob.grab.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.Ticker;
import com.okcoin.rest.StringUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BitstampIndexGrabJob extends AbstractHttpGrab {

    /**
     * 该交易所被封禁的HTTP响应码
     */
    private  final static int banIpStatusCode = -1;

    @Override
    public ExchangeEnum getExchange() {
        return ExchangeEnum.BITSTAMP;
    }

    @Override
    protected Ticker parseIndexPrice(String response, ExchangeIndex exchangeIndex,Date requestTime) {
        JSONObject jsonObject = JSON.parseObject(response);
        String respTime = jsonObject.get("timestamp").toString();
        Date respDate = null;
        if(StringUtil.isEmpty(respTime)){
            respDate = DateUtil.parseStrTimestampSecondToDate(respTime);
        }else {
            respDate = requestTime;
        }
        Ticker ticker = new Ticker(jsonObject.getBigDecimal("last"), respDate);
        return ticker;
    }

    @Override
    protected int getBanIpSattusCode() {
        return banIpStatusCode;
    }
}
