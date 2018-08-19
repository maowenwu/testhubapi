package com.huobi.quantification.service.market.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.util.StorageSupport;
import com.huobi.quantification.dao.QuanDepthDetailMapper;
import com.huobi.quantification.dao.QuanDepthMapper;
import com.huobi.quantification.dao.QuanKlineMapper;
import com.huobi.quantification.dao.QuanTradeMapper;
import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanDepthDetail;
import com.huobi.quantification.entity.QuanKline;
import com.huobi.quantification.entity.QuanTrade;
import com.huobi.quantification.enums.DepthEnum;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.huobi.response.Trade;
import com.huobi.quantification.huobi.response.TradeDetail;
import com.huobi.quantification.huobi.response.TradeResponse;
import com.huobi.quantification.response.spot.HuobiSpotDepthResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.HuobiMarketService;
import com.huobi.quantification.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
@Service
@Transactional
public class HuobiMarketServiceImpl implements HuobiMarketService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpService httpService;
    @Autowired
    private QuanDepthDetailMapper quanDepthDetailMapper;
    @Autowired
    private QuanDepthMapper quanDepthMapper;
    @Autowired
    private QuanKlineMapper quanKlineMapper;
    @Autowired
    private QuanTradeMapper quanTradeMapper;
    @Autowired
    private RedisService redisService;


    public HuobiSpotDepthResponse queryDepthByAPI(String symbol, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol.replace("_", ""));
        params.put("type", type);
        String body = httpService.doGet(HttpConstant.HUOBI_DEPTH, params);
        return JSON.parseObject(body, HuobiSpotDepthResponse.class);
    }

    private void saveSpotDepth(HuobiSpotDepthResponse response, String symbol) {
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            QuanDepth quanDepth = new QuanDepth();
            quanDepth.setExchangeId(ExchangeEnum.HUOBI.getExId());
            String[] quots = symbol.split("_");
            quanDepth.setBaseCoin(quots[0]);
            quanDepth.setQuoteCoin(quots[1]);
            quanDepth.setDepthTs(response.getTs());
            boolean isSave = StorageSupport.getInstance("getSpotDepth").checkSavepoint();
            if (isSave) {
                quanDepthMapper.insertAndGetId(quanDepth);
            }

            List<QuanDepthDetail> list = new ArrayList<>();
            List<List<BigDecimal>> asks = response.getTick().getAsks();
            List<List<BigDecimal>> bids = response.getTick().getBids();
            for (int i = 0; i < asks.size(); i++) {
                List<BigDecimal> depth = asks.get(i);
                QuanDepthDetail depthDetail = new QuanDepthDetail();
                depthDetail.setDepthId(quanDepth.getId());
                depthDetail.setDetailType(DepthEnum.ASKS.getIntType());
                depthDetail.setDetailPrice(depth.get(0));
                depthDetail.setDetailAmount(depth.get(1));
                depthDetail.setDateUpdate(new Date());
                list.add(depthDetail);
            }
            for (int i = 0; i < bids.size(); i++) {
                List<BigDecimal> depth = bids.get(i);
                QuanDepthDetail depthDetail = new QuanDepthDetail();
                depthDetail.setDepthId(quanDepth.getId());
                depthDetail.setDetailType(DepthEnum.BIDS.getIntType());
                depthDetail.setDetailPrice(depth.get(0));
                depthDetail.setDetailAmount(depth.get(1));
                depthDetail.setDateUpdate(new Date());
                list.add(depthDetail);
            }

            for (QuanDepthDetail detail : list) {
                if (isSave) {
                    quanDepthDetailMapper.insert(detail);
                }
            }
            redisService.saveDepthSpot(ExchangeEnum.HUOBI.getExId(), symbol, list);
        }

    }

    public Object getKline(String symbol, String period, String size) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("period", period);
        params.put("size", size);
        String body = httpService.doGet(HttpConstant.HUOBI_KLINE, params);
        parseAndSaveKline(body, symbol, ExchangeEnum.HUOBI.getExId(), period, size);
        return null;
    }

    private void parseAndSaveKline(String jsonStr, String symbol, int exchangeId, String period, String size) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        ArrayList<QuanKline> klineList = new ArrayList<QuanKline>();
        if (jsonObject.getString("status").equals("ok")) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            QuanKline quanKline = new QuanKline();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject klineObject = jsonArray.getJSONObject(i);
                quanKline.setId(klineObject.getLong("id"));
                quanKline.setAmount(klineObject.getBigDecimal("amount"));
                quanKline.setClose(klineObject.getBigDecimal("close"));
                quanKline.setCount(klineObject.getBigDecimal("count"));
                quanKline.setExchangeId(exchangeId);
                quanKline.setHigh(klineObject.getBigDecimal("high"));
                quanKline.setLow(klineObject.getBigDecimal("low"));
                quanKline.setOpen(klineObject.getBigDecimal("open"));
                quanKline.setPeriod(period);
                quanKline.setSize(Long.parseLong(size));
                quanKline.setSymbol(symbol);
                quanKline.setTs(jsonObject.getDate("ts"));
                quanKline.setVol(klineObject.getBigDecimal("vol"));
                quanKlineMapper.insert(quanKline);
                klineList.add(quanKline);
            }
            redisService.saveKlineSpot(exchangeId, symbol, period, klineList);
        }

    }

    @Override
    public void updateHuobiDepth(String symbol, String type) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.info("[HuobiSpotDepth][symbol={},type={}]任务开始", symbol, type);
        HuobiSpotDepthResponse response = queryDepthByAPI(symbol, type);
        saveSpotDepth(response, symbol);
        logger.info("[HuobiSpotDepth][symbol={},type={}]任务结束，耗时：" + stopwatch, symbol, type);
    }

    @Override
    public void updateCurrentPrice(String symbol) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.info("[HuobiSpotCurrentPrice][symbol={}]任务开始", symbol);
        TradeResponse trade = queryCurrentPriceByApi(symbol);
        parseAndSaveTrade(trade, symbol);
        logger.info("[HuobiSpotCurrentPrice][symbol={}]任务结束，耗时：" + stopwatch, symbol);
    }

    private void parseAndSaveTrade(TradeResponse trade, String symbol) {
        Trade tick = trade.getTick();
        TradeDetail data = tick.getData();
        QuanTrade quanTrade = new QuanTrade();
        quanTrade.setAmount(data.getAmount());
        quanTrade.setDirection(data.getDirection());
        quanTrade.setExchangeId(ExchangeEnum.HUOBI.getExId());
        quanTrade.setPrice(data.getPrice());
        quanTrade.setQueryId(tick.getId());
        quanTrade.setSymbol(symbol);
        quanTrade.setTradeId(data.getId());
        quanTrade.setTs(data.getTs());
        redisService.saveCurrentPriceSpot(ExchangeEnum.HUOBI.getExId(), symbol, quanTrade);
        quanTradeMapper.insert(quanTrade);
    }

    /**
     * 查询当前最新成交价，并转化返回一个对象
     *
     * @param symbol
     * @return
     */
    private TradeResponse queryCurrentPriceByApi(String symbol) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        String body = httpService.doGet(HttpConstant.HUOBI_TRADE, params);
        TradeResponse trade = new TradeResponse();
        Trade tick = new Trade();
        TradeDetail data = new TradeDetail();
        JSONObject parseObject = JSON.parseObject(body);
        JSONObject jsonObject = parseObject.getJSONObject("tick");
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
        data.setAmount(jsonObject2.getBigDecimal("amount"));
        data.setDirection(jsonObject2.getString("direction"));
        data.setId(jsonObject2.getLong("id"));
        data.setPrice(jsonObject2.getBigDecimal("price"));
        data.setTs(jsonObject2.getDate("ts"));
        tick.setData(data);
        tick.setId(jsonObject.getLong("id"));
        tick.setTs(jsonObject.getDate("ts"));
        trade.setTick(tick);
        trade.setCh(parseObject.getString("ch"));
        trade.setStatus("status");
        trade.setTs(parseObject.getDate("ts"));
        return trade;
    }

    @Override
    public void updateKline(String symbol, String KlineType, String size) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.info("[HuobiSpotKline][symbol={},KlineType={},size={}]任务开始", symbol, KlineType, size);
        getKline(symbol, KlineType, size);
        logger.info("[HuobiSpotKline][symbol={},KlineType={},size={}]任务结束，耗时：" + stopwatch, symbol, KlineType, size);
    }
}