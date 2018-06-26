package com.huobi.contract.index.taskjob.grab.http;

import com.alibaba.fastjson.JSON;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.Ticker;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class GdaxIndexGrabJob extends AbstractHttpGrab {
    /**
     * 该交易所被封禁的HTTP响应码
     */
    private  final static int banIpStatusCode = -1;

    @Override
    public ExchangeEnum getExchange() {
        return ExchangeEnum.GDAX;
    }

    @Override
    protected Ticker parseIndexPrice(String response, ExchangeIndex exchangeIndex,Date requestTime) {
        //{"trade_id":35399478,"price":"622.33000000","size":"0.02343890","bid":"622.23","ask":"622.24","volume":"57288.52191465","time":"2018-06-04T01:31:57.376000Z"}
        BigDecimal sourcePrice = JSON.parseObject(response).getBigDecimal("price");
        String timeStr = JSON.parseObject(response).getString("time");
        Date reponseTime = DateUtil.parseUTCTimeToDate(timeStr.substring(0, timeStr.indexOf(".")));
        return new Ticker(sourcePrice, reponseTime);
    }
    @Override
    protected int getBanIpSattusCode() {
        return banIpStatusCode;
    }

}
