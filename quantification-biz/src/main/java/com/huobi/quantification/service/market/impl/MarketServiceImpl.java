package com.huobi.quantification.service.market.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
@Transactional
public class MarketServiceImpl implements MarketService {

    @Autowired
    private HttpService httpService;

    @Override
    public Object getOkTicker(String symbol, String contractType) {
        String url = HttpConstant.OK_TICKER + "?symbol=" + symbol + "&contract_type=" + contractType;
        String body = httpService.doGet(url);
        JSONObject jsonObject = JSON.parseObject(body);
        return null;
    }

    @Override
    public Object getOkDepth(String symbol, String contractType) {
        String url = HttpConstant.OK_DEPTH + "?symbol=" + symbol + "&contract_type=" + contractType;
        return httpService.doGet(url);
    }

}
