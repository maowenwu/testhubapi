package com.huobi.quantification.service.market.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huobi.quantification.enums.ExchangeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.dao.QuanDepthFutureDetailMapper;
import com.huobi.quantification.dao.QuanDepthFutureMapper;
import com.huobi.quantification.dao.QuanKlineFutureMapper;
import com.huobi.quantification.dao.QuanTickerFutureMapper;
import com.huobi.quantification.entity.QuanDepthFuture;
import com.huobi.quantification.entity.QuanDepthFutureDetail;
import com.huobi.quantification.entity.QuanIndexFuture;
import com.huobi.quantification.entity.QuanKlineFuture;
import com.huobi.quantification.entity.QuanTickerFuture;
import com.huobi.quantification.entity.QuanTradeFuture;
import com.huobi.quantification.enums.DepthEnum;
import com.huobi.quantification.enums.OkSymbolEnum;
import com.huobi.quantification.response.future.OKFutureCurrentPriceResponse;
import com.huobi.quantification.response.future.OKFutureIndexResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.OkFutureMarketService;
import com.huobi.quantification.service.redis.RedisService;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
@Transactional
public class OkFutureMarketServiceImpl implements OkFutureMarketService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpService httpService;
    @Autowired
    private QuanDepthFutureMapper quanDepthFutureMapper;
    @Autowired
    private QuanDepthFutureDetailMapper quanDepthFutureDetailMapper;
    @Autowired
    private QuanKlineFutureMapper quanKlineFutureMapper;
    @Autowired
    private RedisService redisService;



    @Override
    public ServiceResult getOkDepth(String symbol, String contractType) {

        return null;
    }


    public void updateOkDepth(String symbol, String contractType) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[OK深度][symbol={},contractType={}]任务开始", symbol, contractType);
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        params.put("size", "5");
        params.put("merge", "0");
        String body = httpService.doGet(HttpConstant.OK_DEPTH, params);
        parseAndSaveQuanDepth(body, OkSymbolEnum.valueSymbolOf(symbol), contractType);
        logger.info("[OK深度][symbol={},contractType={}]任务结束，耗时：" + started, symbol, contractType);
    }

    private void parseAndSaveQuanDepth(String body, OkSymbolEnum symbolEnum, String contractType) {
        QuanDepthFuture quanDepthFuture = new QuanDepthFuture();
        quanDepthFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
        quanDepthFuture.setDepthTs(new Date());
        quanDepthFuture.setBaseCoin(symbolEnum.getBaseCoin());
        quanDepthFuture.setQuoteCoin(symbolEnum.getQuoteCoin());
        quanDepthFuture.setSymbol(symbolEnum.getSymbol());
        quanDepthFuture.setContractType(contractType);
        quanDepthFutureMapper.insertAndGetId(quanDepthFuture);

        JSONObject jsonObject = JSON.parseObject(body);
        List<QuanDepthFutureDetail> list = new ArrayList<>();
        JSONArray asks = jsonObject.getJSONArray("asks");
        for (int i = 0; i < asks.size(); i++) {
            JSONArray item = asks.getJSONArray(i);
            QuanDepthFutureDetail depthDetail = new QuanDepthFutureDetail();
            depthDetail.setDepthFutureId(quanDepthFuture.getId());
            depthDetail.setDetailType(DepthEnum.ASKS.getIntType());
            depthDetail.setDetailPrice(item.getBigDecimal(0));
            depthDetail.setDetailAmount(item.getBigDecimal(1));
            depthDetail.setDateUpdate(new Date());
            list.add(depthDetail);
        }
        JSONArray bids = jsonObject.getJSONArray("bids");
        for (int i = 0; i < bids.size(); i++) {
            JSONArray item = bids.getJSONArray(i);
            QuanDepthFutureDetail depthDetail = new QuanDepthFutureDetail();
            depthDetail.setDepthFutureId(quanDepthFuture.getId());
            depthDetail.setDetailType(DepthEnum.BIDS.getIntType());
            depthDetail.setDetailPrice(item.getBigDecimal(0));
            depthDetail.setDetailAmount(item.getBigDecimal(1));
            depthDetail.setDateUpdate(new Date());
            list.add(depthDetail);
        }
        for (QuanDepthFutureDetail detail : list) {
            quanDepthFutureDetailMapper.insert(detail);
        }
        redisService.saveDepthFuture(ExchangeEnum.OKEX.getExId(), symbolEnum.getSymbol(), contractType, list);
    }


    @Override
    public ServiceResult getOkKline(String symbol, String type, String contractType, int size, long since) {
        List<QuanKlineFuture> list = queryOkFutureKlineByAPI(symbol, type, contractType, size, since);
        for (QuanKlineFuture klineFuture : list) {
            quanKlineFutureMapper.insert(klineFuture);
        }
        return null;
    }


    private List<QuanKlineFuture> queryOkFutureKlineByAPI(String symbol, String type, String contractType, int size, long since) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("type", type);
        params.put("contract_type", contractType);
        params.put("size", String.valueOf(size));
        params.put("since", String.valueOf(since));
        String body = httpService.doGet(HttpConstant.OK_KLINE, params);
        List<QuanKlineFuture> list = parseAndSaveFutureKline(body);
        for (QuanKlineFuture klineFuture : list) {
            klineFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
            klineFuture.setType(type);
            klineFuture.setSymbol(symbol);
            klineFuture.setContractType(contractType);
        }
        return list;
    }


    private List<QuanKlineFuture> parseAndSaveFutureKline(String body) {
        JSONArray jsonArray = JSON.parseArray(body);
        List<QuanKlineFuture> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray kline = jsonArray.getJSONArray(i);
            list.add(parseFutureKline(kline));
        }
        return list;
    }

    private QuanKlineFuture parseFutureKline(JSONArray kline) {
        QuanKlineFuture klineFuture = new QuanKlineFuture();
        klineFuture.setTs(new Date(kline.getLong(0)));
        klineFuture.setOpen(kline.getBigDecimal(1));
        klineFuture.setHigh(kline.getBigDecimal(2));
        klineFuture.setLow(kline.getBigDecimal(3));
        klineFuture.setClose(kline.getBigDecimal(4));
        klineFuture.setAmount(kline.getBigDecimal(5));
        return klineFuture;
    }

    public void updateOkFutureKline(String symbol, String type, String contractType) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[Kline][symbol={},contractType={}]任务开始", symbol, contractType);
        Date sinceDate = DateUtils.plusMinutes(new Date(), -60 * 24);
        List<QuanKlineFuture> redisKline = queryOkFutureKlineByAPI(symbol, type, contractType, 100, sinceDate.getTime());
        redisService.saveKlineFuture(ExchangeEnum.OKEX.getExId(), symbol, type, contractType, redisKline);
        logger.info("[Kline][symbol={},contractType={}]任务结束，耗时：" + started, symbol, contractType);
    }


    @Override
    public void updateOkIndex(String symbol) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[OkIndex][symbol={}]任务开始", symbol);
        Date now = new Date();
        OKFutureIndexResponse indexResponse = queryOkFutureIndexByAPI(symbol);
        QuanIndexFuture quanIndexFuture = new QuanIndexFuture();
        quanIndexFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
        quanIndexFuture.setSymbol(symbol);
        quanIndexFuture.setFutureIndex(indexResponse.getFutureIndex());
        quanIndexFuture.setCreateTime(now);
        quanIndexFuture.setUpdateTime(now);
        redisService.saveIndexFuture(quanIndexFuture);
        logger.info("[OkIndex][symbol={}]任务结束，耗时：" + started, symbol);
    }

    private OKFutureIndexResponse queryOkFutureIndexByAPI(String symbol) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        String body = httpService.doGet(HttpConstant.OK_INDEX, params);
        return JSON.parseObject(body, OKFutureIndexResponse.class);
    }

    @Override
    public void updateOkCurrentPrice(String symbol, String contractType) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[OkCurrentPrice][symbol={},contractType={}]任务开始", symbol, contractType);
        List<OKFutureCurrentPriceResponse> priceResponses = queryOkFutureCurrentPriceByAPI(symbol, contractType);
        OKFutureCurrentPriceResponse latestPrice = priceResponses.get(priceResponses.size() - 1);
        QuanTradeFuture tradeFuture = new QuanTradeFuture();
        tradeFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
        tradeFuture.setSymbol(symbol);
        tradeFuture.setContractType(contractType);
        tradeFuture.setType(latestPrice.getType());
        tradeFuture.setPrice(latestPrice.getPrice());
        tradeFuture.setAmount(latestPrice.getAmount());
        tradeFuture.setCreateDate(new Date());
        tradeFuture.setUpdateTime(latestPrice.getTs());
        redisService.saveCurrentPriceFuture(ExchangeEnum.OKEX.getExId(), symbol, contractType,tradeFuture);
        logger.info("[OkCurrentPrice][symbol={},contractType={}]任务结束，耗时：" + started, symbol, contractType);
    }

    private List<OKFutureCurrentPriceResponse> queryOkFutureCurrentPriceByAPI(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        String body = httpService.doGet(HttpConstant.OK_TRADES, params);
        return JSON.parseArray(body, OKFutureCurrentPriceResponse.class);
    }
}
