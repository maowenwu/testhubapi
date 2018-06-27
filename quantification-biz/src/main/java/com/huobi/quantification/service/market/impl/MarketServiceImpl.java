package com.huobi.quantification.service.market.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanTickerMapper;
import com.huobi.quantification.entity.QuanTicker;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
@Transactional
public class MarketServiceImpl implements MarketService {

    @Autowired
    private HttpService httpService;

    @Autowired
    private QuanTickerMapper quanTickerMapper;

    @Override
    public Object getOkTicker(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        String body = httpService.doGet(HttpConstant.OK_TICKER, params);
        JSONObject jsonObject = JSON.parseObject(body);
        QuanTicker quanTicker = parseQuanTicker(jsonObject);
        quanTickerMapper.insert(quanTicker);
        return quanTicker;
    }

    private QuanTicker parseQuanTicker(JSONObject jsonObject) {
        QuanTicker quanTicker = new QuanTicker();
        quanTicker.setTs(new Date(jsonObject.getLong("date")));
        JSONObject ticker = jsonObject.getJSONObject("ticker");

        return quanTicker;
    }

    @Override
    public Object getOkDepth(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        params.put("size", "100");
        params.put("merge", "1");
        String body = httpService.doGet(HttpConstant.OK_DEPTH, params);
        return null;
    }

}
