package com.huobi.quantification.service.market.impl;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.response.future.HuobiFutureDepthResponse;
import com.huobi.quantification.response.future.HuobiFutureKlineResponse;
import com.huobi.quantification.response.future.HuobiFutureTickerResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.HuobiFutureMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class HuobiFutureMarketServiceImpl implements HuobiFutureMarketService {

    @Autowired
    private HttpService httpService;

    @Override
    public HuobiFutureTickerResponse queryTickerByAPI(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol + "_" + contractType);
        String body = httpService.doGet(HttpConstant.HUOBI_FUTURE_TICKER, params);
        return JSON.parseObject(body, HuobiFutureTickerResponse.class);
    }

    @Override
    public HuobiFutureKlineResponse queryKlineByAPI(String symbol, String contractType, String period, int size) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol + "_" + contractType);
        params.put("period", period);
        params.put("size", size + "");
        String body = httpService.doGet(HttpConstant.HUOBI_FUTURE_KLINE, params);
        return JSON.parseObject(body, HuobiFutureKlineResponse.class);
    }

    @Override
    public HuobiFutureDepthResponse queryDepthByAPI(String symbol, String contractType, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol + "_" + contractType);
        params.put("type", type);
        String body = httpService.doGet(HttpConstant.HUOBI_FUTURE_DEPTH, params);
        return JSON.parseObject(body, HuobiFutureDepthResponse.class);
    }
}
