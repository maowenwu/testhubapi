package com.huobi.quantification.service.market.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huobi.quantification.common.util.StorageSupport;
import com.huobi.quantification.enums.ExchangeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanDepthFutureDetailMapper;
import com.huobi.quantification.dao.QuanDepthFutureMapper;
import com.huobi.quantification.entity.QuanDepthFuture;
import com.huobi.quantification.entity.QuanDepthFutureDetail;
import com.huobi.quantification.entity.QuanIndexFuture;
import com.huobi.quantification.entity.QuanKlineFuture;
import com.huobi.quantification.entity.QuanTradeFuture;
import com.huobi.quantification.enums.DepthEnum;
import com.huobi.quantification.enums.OkSymbolEnum;
import com.huobi.quantification.response.future.HuobiFutureDepthResponse;
import com.huobi.quantification.response.future.HuobiFutureIndexResponse;
import com.huobi.quantification.response.future.HuobiFutureKlineResponse;
import com.huobi.quantification.response.future.HuobiFutureTickerResponse;
import com.huobi.quantification.response.future.HuobiFutureTradeResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.HuobiFutureMarketService;
import com.huobi.quantification.service.redis.RedisService;

@Service
@Transactional
public class HuobiFutureMarketServiceImpl implements HuobiFutureMarketService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpService httpService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QuanDepthFutureMapper quanDepthFutureMapper;

    @Autowired
    private QuanDepthFutureDetailMapper quanDepthFutureDetailMapper;

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
        params.put("symbol", getHuobiSymbol(symbol, contractType));
        params.put("period", period);
        params.put("size", size + "");
        String body = httpService.doGet(HttpConstant.HUOBI_FUTURE_KLINE, params);
        return JSON.parseObject(body, HuobiFutureKlineResponse.class);
    }

    @Override
    public HuobiFutureDepthResponse queryDepthByAPI(String symbol, String contractType, String type) {
        Map<String, String> params = new HashMap<>();
        String baseCoin = symbol.split("_")[0].toUpperCase();
        if ("this_week".equalsIgnoreCase(contractType)) {
            contractType = "CW";
        } else if ("next_week".equalsIgnoreCase(contractType)) {
            contractType = "NW";
        } else {
            contractType = "CQ";
        }
        params.put("symbol", baseCoin + "_" + contractType);
        params.put("type", type);
        String body = httpService.doGet(HttpConstant.HUOBI_FUTURE_DEPTH, params);
        return JSON.parseObject(body, HuobiFutureDepthResponse.class);
    }

    @Override
    public void updateHuobiCurrentPrice(String symbol, String contractType) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiCurrentPrice][symbol={},contractType={}]任务开始", symbol, contractType);
        HuobiFutureTradeResponse latestPrice = queryCurrentPriceByAPI(symbol, contractType);
        HuobiFutureTradeResponse.DataBeanX.DataBean dataBean = latestPrice.getData().get(0).getData().get(0);
        QuanTradeFuture tradeFuture = new QuanTradeFuture();
        tradeFuture.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
        tradeFuture.setSymbol(symbol);
        tradeFuture.setContractType(contractType);
        tradeFuture.setType(dataBean.getDirection());
        tradeFuture.setPrice(dataBean.getPrice());
        tradeFuture.setAmount(dataBean.getAmount());
        tradeFuture.setCreateDate(new Date());
        tradeFuture.setUpdateTime(latestPrice.getTs());
        redisService.saveCurrentPriceFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), symbol, contractType, tradeFuture);
        logger.info("[HuobiCurrentPrice][symbol={},contractType={}]任务结束，耗时：" + started, symbol, contractType);
    }

    private HuobiFutureTradeResponse queryCurrentPriceByAPI(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", getHuobiSymbol(symbol, contractType));
        params.put("size", "1");
        String body = httpService.doGet(HttpConstant.HUOBI_FUTURE_TRADE, params);
        return JSON.parseObject(body, HuobiFutureTradeResponse.class);
    }

    private String getHuobiSymbol(String symbol, String contractType) {
        symbol = symbol.split("_")[0];
        if ("this_week".equalsIgnoreCase(contractType)) {
            return symbol + "_cw";
        } else if ("next_week".equalsIgnoreCase(contractType)) {
            return symbol + "_nw";
        } else if ("quarter".equalsIgnoreCase(contractType)) {
            return symbol + "_cq";
        }
        throw new IllegalStateException("contractType类型不支持,contractType=" + contractType);
    }

    @Override
    public void updateHuobiDepth(String symbol, String contractType, String depthType) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[Huobi深度][symbol={},contractType={}]任务开始", symbol, contractType);
        HuobiFutureDepthResponse response = queryDepthByAPI(symbol, contractType, depthType);
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            parseAndSaveDepth(response, symbol, contractType, depthType);
        } else {
            logger.error("获取火币深度异常，api返回为：{}", JSON.toJSONString(response));
        }
        logger.info("[Huobi深度][symbol={},contractType={}]任务结束，耗时：" + started, symbol, contractType);
    }


    private void parseAndSaveDepth(HuobiFutureDepthResponse response, String symbol, String contractType, String depthType) {
        QuanDepthFuture quanDepthFuture = new QuanDepthFuture();
        quanDepthFuture.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
        quanDepthFuture.setDepthTs(new Date());
        String[] split = symbol.split("_");
        quanDepthFuture.setBaseCoin(split[0]);
        quanDepthFuture.setQuoteCoin(split[1]);
        quanDepthFuture.setSymbol(symbol);
        quanDepthFuture.setContractType(contractType);
        boolean isSave = StorageSupport.getInstance("SaveFutureDepth").checkSavepoint();
        if (isSave) {
            quanDepthFutureMapper.insertAndGetId(quanDepthFuture);
        }

        List<QuanDepthFutureDetail> list = new ArrayList<>();
        HuobiFutureDepthResponse.TickBean tickBean = response.getTick();
        List<List<BigDecimal>> asks = tickBean.getAsks();
        List<List<BigDecimal>> bids = tickBean.getBids();

        if (CollectionUtils.isNotEmpty(asks)) {
            for (int i = 0; i < asks.size(); i++) {
                List<BigDecimal> askItem = asks.get(i);
                QuanDepthFutureDetail depthDetail = new QuanDepthFutureDetail();
                depthDetail.setDepthFutureId(quanDepthFuture.getId());
                depthDetail.setDetailType(DepthEnum.ASKS.getIntType());
                depthDetail.setDetailPrice(askItem.get(0));
                depthDetail.setDetailAmount(askItem.get(1));
                depthDetail.setDateUpdate(new Date());
                list.add(depthDetail);
            }
        }

        if (CollectionUtils.isNotEmpty(bids)) {
            for (int i = 0; i < bids.size(); i++) {
                List<BigDecimal> bidItem = bids.get(i);
                QuanDepthFutureDetail depthDetail = new QuanDepthFutureDetail();
                depthDetail.setDepthFutureId(quanDepthFuture.getId());
                depthDetail.setDetailType(DepthEnum.BIDS.getIntType());
                depthDetail.setDetailPrice(bidItem.get(0));
                depthDetail.setDetailAmount(bidItem.get(1));
                depthDetail.setDateUpdate(new Date());
                list.add(depthDetail);
            }
        }
        if (isSave) {
            for (QuanDepthFutureDetail detail : list) {
                quanDepthFutureDetailMapper.insert(detail);
            }
        }
        redisService.saveDepthFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), symbol, contractType, depthType, list);
    }


    @Override
    public void updateHuobiKline(String symbol, String contractType, String period) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiKline][symbol={},contractType={}]任务开始", symbol, contractType);
        HuobiFutureKlineResponse klineResponse = queryKlineByAPI(symbol, contractType, period, 100);
        List<QuanKlineFuture> redisKline = genRedisKline(klineResponse);
        for (QuanKlineFuture klineFuture : redisKline) {
            klineFuture.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
            klineFuture.setType(period);
            klineFuture.setSymbol(symbol);
            klineFuture.setContractType(contractType);
        }
        redisService.saveKlineFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), symbol, period, contractType, redisKline);
        logger.info("[HuobiKline][symbol={},contractType={}]任务结束，耗时：" + started, symbol, contractType);
    }

    private List<QuanKlineFuture> genRedisKline(HuobiFutureKlineResponse klineResponse) {
        List<QuanKlineFuture> futures = new ArrayList<>();
        for (HuobiFutureKlineResponse.DataBean dataBean : klineResponse.getData()) {
            QuanKlineFuture klineFuture = new QuanKlineFuture();
            klineFuture.setTs(klineResponse.getTs());
            klineFuture.setOpen(dataBean.getOpen());
            klineFuture.setHigh(dataBean.getHigh());
            klineFuture.setLow(dataBean.getLow());
            klineFuture.setClose(dataBean.getClose());
            klineFuture.setAmount(dataBean.getAmount());
        }
        return futures;
    }

    @Override
    public void updateHuobiIndex(String symbol) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiIndex][symbol={}]任务开始", symbol);
        Date now = new Date();
        HuobiFutureIndexResponse indexResponse = queryHuobiIndexByAPI(symbol);
        QuanIndexFuture quanIndexFuture = new QuanIndexFuture();
        quanIndexFuture.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
        quanIndexFuture.setSymbol(symbol);
        quanIndexFuture.setFutureIndex(indexResponse.getFutureIndex());
        quanIndexFuture.setCreateTime(now);
        quanIndexFuture.setUpdateTime(now);
        redisService.saveIndexFuture(quanIndexFuture);
        logger.info("[HuobiIndex][symbol={}]任务结束，耗时：" + started, symbol);
    }

    private HuobiFutureIndexResponse queryHuobiIndexByAPI(String symbol) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        // todo 改url地址
        String body = httpService.doGet(HttpConstant.OK_INDEX, params);
        return JSON.parseObject(body, HuobiFutureIndexResponse.class);
    }
}
