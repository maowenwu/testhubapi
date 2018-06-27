package com.huobi.quantification.service.market.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanDepthDetailMapper;
import com.huobi.quantification.dao.QuanDepthMapper;
import com.huobi.quantification.dao.QuanTickerMapper;
import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanDepthDetail;
import com.huobi.quantification.entity.QuanTicker;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.MarketService;
import com.sun.scenario.effect.impl.prism.PrImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private QuanDepthMapper quanDepthMapper;

    @Autowired
    private QuanDepthDetailMapper quanDepthDetailMapper;

    @Override
    public Object getOkTicker(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        String body = httpService.doGet(HttpConstant.OK_TICKER, params);
        QuanTicker quanTicker = parseAndSaveQuanTicker(body);
        return quanTicker;
    }

    private QuanTicker parseAndSaveQuanTicker(String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        QuanTicker quanTicker = new QuanTicker();
        quanTicker.setExchangeId(1L);
        quanTicker.setTs(new Date(jsonObject.getLong("date") * 1000L));
        JSONObject ticker = jsonObject.getJSONObject("ticker");
        quanTicker.setLastPrice(ticker.getBigDecimal("last"));
        quanTicker.setBidPrice(ticker.getBigDecimal("buy"));
        quanTicker.setAskPrice(ticker.getBigDecimal("sell"));
        quanTicker.setBaseCoin("BTC");
        quanTicker.setQuoteCoin("USD");
        quanTicker.setHighPrice(ticker.getBigDecimal("high"));
        quanTicker.setLowPrice(ticker.getBigDecimal("low"));
        quanTickerMapper.insert(quanTicker);
        return quanTicker;
    }

    @Override
    public Object getOkDepth(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        params.put("size", "5");
        params.put("merge", "1");
        String body = httpService.doGet(HttpConstant.OK_DEPTH, params);
        parseAndSaveQuanDepth(body);
        return null;
    }

    private void parseAndSaveQuanDepth(String body) {
        QuanDepth quanDepth = new QuanDepth();
        quanDepth.setExchangeId(1L);
        quanDepth.setDepthTs(new Date());
        quanDepth.setBaseCoin("BTC");
        quanDepth.setQuoteCoin("USD");
        quanDepthMapper.insert(quanDepth);
        JSONObject jsonObject = JSON.parseObject(body);
        List<QuanDepthDetail> list = new ArrayList<>();
        JSONArray asks = jsonObject.getJSONArray("asks");
        for (int i = 0; i < asks.size(); i++) {
            JSONArray item = asks.getJSONArray(i);
            QuanDepthDetail depthDetail = new QuanDepthDetail();
            depthDetail.setDepthId(quanDepth.getId());
            depthDetail.setDetailType(0);
            depthDetail.setDetailPrice(item.getBigDecimal(0));
            depthDetail.setDetailAmount(item.getDouble(1));
            depthDetail.setDateUpdate(new Date());
            list.add(depthDetail);
        }
        JSONArray bids = jsonObject.getJSONArray("bids");
        for (int i = 0; i < bids.size(); i++) {
            JSONArray item = bids.getJSONArray(i);
            QuanDepthDetail depthDetail = new QuanDepthDetail();
            depthDetail.setDepthId(quanDepth.getId());
            depthDetail.setDetailType(1);
            depthDetail.setDetailPrice(item.getBigDecimal(0));
            depthDetail.setDetailAmount(item.getDouble(1));
            depthDetail.setDateUpdate(new Date());
            list.add(depthDetail);
        }

        //quanDepthDetailMapper
    }
}
