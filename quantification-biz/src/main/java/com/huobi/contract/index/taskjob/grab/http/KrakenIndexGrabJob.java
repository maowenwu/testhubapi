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
public class KrakenIndexGrabJob extends AbstractHttpGrab {
    /**
     * 该交易所被封禁的HTTP响应码
     */
    private  final static int banIpStatusCode = -1;
    @Override
    public ExchangeEnum getExchange() {
        return ExchangeEnum.KRAKEN;
    }


    @Override
    protected Ticker parseIndexPrice(String response, ExchangeIndex exchangeIndex,Date requestTime) {
        //{"error":[],"result":{"XLTCZUSD":{"a":["126.24000","1","1.000"],"b":["126.23000","8","8.000"],"c":["126.23000","0.11400000"],"v":["399.31786264","9745.30027005"],"p":["125.95549","125.99348"],"t":[244,2316],"l":["125.20000","122.21000"],"h":["126.77000","127.90000"],"o":"125.20000"}}}
        String exchangeSymbol = exchangeIndex.getExchangeSymbol();
        JSONObject object = JSON.parseObject(response);
        String symbolKey = "X" + exchangeSymbol.substring(0, 3) + "Z" + exchangeSymbol.substring(3, 6);
        BigDecimal price = object.getJSONObject("result").getJSONObject(symbolKey).getJSONArray("c").getBigDecimal(0);
        return new Ticker(price, requestTime);
    }

    @Override
    protected int getBanIpSattusCode() {
        return banIpStatusCode;
    }
}
