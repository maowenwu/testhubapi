package com.huobi.quantification.service.market.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanDepthFutureDetailMapper;
import com.huobi.quantification.dao.QuanDepthFutureMapper;
import com.huobi.quantification.dao.QuanTickerFutureMapper;
import com.huobi.quantification.entity.QuanDepthFuture;
import com.huobi.quantification.entity.QuanDepthFutureDetail;
import com.huobi.quantification.entity.QuanTickerFuture;
import com.huobi.quantification.enums.DepthDirectionEnum;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OkSymbolEnum;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.MarketService;
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
    private QuanTickerFutureMapper quanTickerFutureMapper;

    @Autowired
    private QuanDepthFutureMapper quanDepthFutureMapper;

    @Autowired
    private QuanDepthFutureDetailMapper quanDepthFutureDetailMapper;

    @Override
    public Object getOkTicker(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        String body = httpService.doGet(HttpConstant.OK_TICKER, params);
        QuanTickerFuture quanTicker = parseAndSaveQuanTicker(body, OkSymbolEnum.valueSymbolOf(symbol), contractType);
        return quanTicker;
    }

    private QuanTickerFuture parseAndSaveQuanTicker(String body, OkSymbolEnum symbolEnum, String contractType) {
        JSONObject jsonObject = JSON.parseObject(body);
        QuanTickerFuture quanTickerFuture = new QuanTickerFuture();
        quanTickerFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
        quanTickerFuture.setTs(new Date(jsonObject.getLong("date") * 1000L));
        JSONObject ticker = jsonObject.getJSONObject("ticker");
        quanTickerFuture.setLastPrice(ticker.getBigDecimal("last"));
        quanTickerFuture.setBidPrice(ticker.getBigDecimal("buy"));
        quanTickerFuture.setAskPrice(ticker.getBigDecimal("sell"));
        quanTickerFuture.setContractCode(ticker.getString("contract_id"));
        quanTickerFuture.setContractName(contractType);
        quanTickerFuture.setBaseCoin(symbolEnum.getBaseCoin());
        quanTickerFuture.setQuoteCoin(symbolEnum.getQuoteCoin());
        quanTickerFuture.setHighPrice(ticker.getBigDecimal("high"));
        quanTickerFuture.setLowPrice(ticker.getBigDecimal("low"));
        quanTickerFutureMapper.insert(quanTickerFuture);
        return quanTickerFuture;
    }

    @Override
    public Object getOkDepth(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        params.put("size", "5");
        params.put("merge", "1");
        String body = httpService.doGet(HttpConstant.OK_DEPTH, params);
        parseAndSaveQuanDepth(body, OkSymbolEnum.valueSymbolOf(symbol), contractType);
        return null;
    }

    private void parseAndSaveQuanDepth(String body, OkSymbolEnum symbolEnum, String contractType) {
        QuanDepthFuture quanDepthFuture = new QuanDepthFuture();
        quanDepthFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
        quanDepthFuture.setDepthTs(new Date());
        quanDepthFuture.setBaseCoin(symbolEnum.getBaseCoin());
        quanDepthFuture.setQuoteCoin(symbolEnum.getQuoteCoin());
        // todo 接口没返回呢
        quanDepthFuture.setContractCode("");
        quanDepthFuture.setContractName(contractType);
        quanDepthFutureMapper.insertAndGetId(quanDepthFuture);

        JSONObject jsonObject = JSON.parseObject(body);
        List<QuanDepthFutureDetail> list = new ArrayList<>();
        JSONArray asks = jsonObject.getJSONArray("asks");
        for (int i = 0; i < asks.size(); i++) {
            JSONArray item = asks.getJSONArray(i);
            QuanDepthFutureDetail depthDetail = new QuanDepthFutureDetail();
            depthDetail.setDepthFutureId(quanDepthFuture.getId());
            depthDetail.setDetailType(DepthDirectionEnum.ASKS.getIntType());
            depthDetail.setDetailPrice(item.getBigDecimal(0));
            depthDetail.setDetailAmount(item.getDouble(1));
            depthDetail.setDateUpdate(new Date());
            list.add(depthDetail);
        }
        JSONArray bids = jsonObject.getJSONArray("bids");
        for (int i = 0; i < bids.size(); i++) {
            JSONArray item = bids.getJSONArray(i);
            QuanDepthFutureDetail depthDetail = new QuanDepthFutureDetail();
            depthDetail.setDepthFutureId(quanDepthFuture.getId());
            depthDetail.setDetailType(DepthDirectionEnum.BIDS.getIntType());
            depthDetail.setDetailPrice(item.getBigDecimal(0));
            depthDetail.setDetailAmount(item.getDouble(1));
            depthDetail.setDateUpdate(new Date());
            list.add(depthDetail);
        }

        for (QuanDepthFutureDetail detail : list) {
            quanDepthFutureDetailMapper.insert(detail);
        }
    }
}
