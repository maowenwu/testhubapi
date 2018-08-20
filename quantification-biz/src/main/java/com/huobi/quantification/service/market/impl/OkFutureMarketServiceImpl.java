package com.huobi.quantification.service.market.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanDepthFutureDetailMapper;
import com.huobi.quantification.dao.QuanDepthFutureMapper;
import com.huobi.quantification.dao.QuanKlineFutureMapper;
import com.huobi.quantification.entity.QuanIndexFuture;
import com.huobi.quantification.entity.QuanKlineFuture;
import com.huobi.quantification.entity.QuanTradeFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.response.future.OKFutureCurrentPriceResponse;
import com.huobi.quantification.response.future.OKFutureIndexResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.OkFutureMarketService;
import com.huobi.quantification.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
