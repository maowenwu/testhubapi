package com.huobi.quantification.service.market.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
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
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.HuobiMarketService;
import com.huobi.quantification.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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



	public Object getDepth(String symbol, String type) {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", symbol);
		params.put("type", type);
		String body = httpService.doGet(HttpConstant.HUOBI_DEPTH, params);
		parseAndSaveDepth(symbol, body , type);
		return null;
	}

	private void parseAndSaveDepth(String symbol, String body, String type) {
		JSONObject jsonObject = JSON.parseObject(body);
		QuanDepth quanDepth = new QuanDepth();
		quanDepth.setExchangeId(ExchangeEnum.HUOBI.getExId());
		String ch = jsonObject.getString("ch");
		//dbch：从数据库查得的symbol
		ArrayList<String> dbch = new ArrayList<String>();
		dbch.add("btc-usdt");
		dbch.add("eth-usdt");
		String[] chSplit = ch.split("\\.");
		String baseCoinAndQuoteCoin = chSplit[1];
		String baseCoin = "";
		String quoteCoin = "";
		for (String string : dbch) {
			String[] split = string.split("-");
			String ch2 = split[0].concat(split[1]);
			if (ch2.equals(baseCoinAndQuoteCoin)) {
				baseCoin = split[0];
				quoteCoin = split[1];
			}
		}
		quanDepth.setBaseCoin(baseCoin);
		quanDepth.setQuoteCoin(quoteCoin);
		quanDepth.setDepthTs(jsonObject.getDate("ts"));
		quanDepthMapper.insertAndGetId(quanDepth);
		List<QuanDepthDetail> list = new ArrayList<>();
        JSONArray asks = jsonObject.getJSONObject("tick").getJSONArray("asks");
        for (int i = 0; i < asks.size(); i++) {
            JSONArray item = asks.getJSONArray(i);
            QuanDepthDetail depthDetail = new QuanDepthDetail();
            depthDetail.setDepthId(quanDepth.getId());
            depthDetail.setDetailType(DepthEnum.ASKS.getIntType());
            depthDetail.setDetailPrice(item.getBigDecimal(0));
            depthDetail.setDetailAmount(item.getBigDecimal(1));
            depthDetail.setDateUpdate(new Date());
            list.add(depthDetail);
        }
        JSONArray bids = jsonObject.getJSONObject("tick").getJSONArray("bids");
        for (int i = 0; i < bids.size(); i++) {
            JSONArray item = bids.getJSONArray(i);
            QuanDepthDetail depthDetail = new QuanDepthDetail();
            depthDetail.setDepthId(quanDepth.getId());
            depthDetail.setDetailType(DepthEnum.BIDS.getIntType());
            depthDetail.setDetailPrice(item.getBigDecimal(0));
            depthDetail.setDetailAmount(item.getBigDecimal(1));
            depthDetail.setDateUpdate(new Date());
            list.add(depthDetail);
        }
        for (QuanDepthDetail detail : list) {
            quanDepthDetailMapper.insert(detail);
        }
		redisService.saveDepthSpot(ExchangeEnum.HUOBI.getExId(),symbol, list);
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
	public void updateHuobiDepth(String symbol,String type) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		logger.info("[HuobiSpotDepth][symbol={},type={}]任务开始",symbol,type);
		getDepth(symbol, type);
		logger.info("[HuobiSpotDepth][symbol={},type={}]任务结束，耗时：" + stopwatch , symbol , type);
	}

	@Override
	public void updateCurrentPrice(String symbol) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		logger.info("[HuobiSpotCurrentPrice][symbol={}]任务开始",symbol);
		TradeResponse trade = queryCurrentPriceByApi(symbol);
		parseAndSaveTrade(trade, symbol);
		logger.info("[HuobiSpotCurrentPrice][symbol={}]任务结束，耗时：" + stopwatch , symbol);
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
		redisService.saveCurrentPriceSpot(ExchangeEnum.HUOBI.getExId(),symbol, quanTrade);
		quanTradeMapper.insert(quanTrade);
	}

	/**
	 * 查询当前最新成交价，并转化返回一个对象
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
		logger.info("[HuobiSpotKline][symbol={},KlineType={},size={}]任务开始",symbol,KlineType,size);
		getKline(symbol, KlineType, size);
		logger.info("[HuobiSpotKline][symbol={},KlineType={},size={}]任务结束，耗时：" + stopwatch , symbol,KlineType,size);
	}
}