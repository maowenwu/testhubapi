package com.huobi.quantification.service.market.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.huobi.quantification.dao.QuanDepthDetailMapper;
import com.huobi.quantification.dao.QuanDepthMapper;
import com.huobi.quantification.dao.QuanTickerMapper;
import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanDepthDetail;
import com.huobi.quantification.entity.QuanTicker;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OkSymbolEnum;
import com.huobi.quantification.huobi.response.DepthResponse;
import com.huobi.quantification.huobi.response.Merged;
import com.huobi.quantification.huobi.response.Trade;
import com.huobi.quantification.huobi.response.TradeDetail;
import com.huobi.quantification.huobi.response.TradeResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.HuobiMarketService;
import com.huobi.quantification.service.redis.RedisService;

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
	private QuanTickerMapper quanTickerMapper;
	@Autowired
	private RedisService redisService;

	public Object getTicker(String symbol) {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", symbol);
		String body = httpService.doHuobiGet(HttpConstant.HUOBI_TICKER, params);
		QuanTicker quanTicker = new QuanTicker();
		JSONObject jsonObject = JSON.parseObject(body);
		String data = jsonObject.getString("tick");
		JSONObject temp = JSON.parseObject(data);
		JSONArray jsonArray = temp.getJSONArray("ask");
		quanTicker.setAskPrice(jsonArray.getBigDecimal(0));
		JSONArray jsonArray2 = temp.getJSONArray("bid");
		quanTicker.setBidPrice(jsonArray2.getBigDecimal(0));
		Merged merged = JSON.parseObject(body, Merged.class);
		quanTicker.setExchangeId(ExchangeEnum.HUOBI.getExId());
		String ch = jsonObject.getString("ch");
		quanTicker.setLowPrice(temp.getBigDecimal("low"));
		quanTicker.setHighPrice(temp.getBigDecimal("high"));
		quanTicker.setLastPrice(temp.getBigDecimal("close"));
		ArrayList<String> dbch = new ArrayList<String>();
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
		quanTicker.setBaseCoin(baseCoin);
		quanTicker.setQuoteCoin(quoteCoin);
		long ts = merged.getTs();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(ts);
		String format = formatter.format(date);
		Date parse = null;
		try {
			parse = formatter.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		quanTicker.setTs(parse);
		quanTickerMapper.insert(quanTicker);
		redisService.saveHuobiTicker(symbol, quanTicker);
		return null;
	}

	public Object getDepth(String symbol, String type) {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", symbol);
		params.put("type", type);
		String body = httpService.doHuobiGet(HttpConstant.HUOBI_DEPTH, params);
		DepthResponse marketDepth = JSON.parseObject(body, DepthResponse.class);
		QuanDepth quanDepth = new QuanDepth();
		quanDepth.setExchangeId(ExchangeEnum.HUOBI.getExId());
		String ch = marketDepth.getCh();
		ArrayList<String> dbch = new ArrayList<String>();
		dbch.add("btc-usdt");
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
		String ts = marketDepth.getTs();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(ts);
		Date date = new Date(lt);
		String format = formatter.format(date);
		Date parse = null;
		try {
			parse = formatter.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		quanDepth.setDepthTs(parse);
		quanDepthMapper.insert(quanDepth);
		redisService.saveHuobiDepth(symbol, type, quanDepth);
		return null;
	}

	public Object getKline(String symbol, String period, String size) {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", symbol);
		params.put("period", period);
		params.put("size", size);
		String body = httpService.doGet(HttpConstant.HUOBI_KLINE, params);
		return null;
	}

	private void parseAndSaveDepth(String res, OkSymbolEnum symbolEnum, String type) {

		JSONObject jsonObject = JSON.parseObject(res);
		String data = jsonObject.getString("tick");
		QuanDepth quanDepth = new QuanDepth();
		quanDepthMapper.insert(quanDepth);
		JSONObject temp = JSON.parseObject(data);
		JSONArray jsarr = temp.getJSONArray("asks");
		for (int i = 0; i < jsarr.size(); i++) {
			JSONArray item = jsarr.getJSONArray(i);
			QuanDepthDetail depthDetail = new QuanDepthDetail();
			depthDetail.setDetailPrice(item.getBigDecimal(0));
			// depthDetail.setDetailAmount(item.getDouble(1));
			depthDetail.setDepthId(quanDepth.getId());
			quanDepthDetailMapper.insert(depthDetail);
		}

	}

	@Override
	public void updateHuobiTicker(String symbol) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		logger.info("[Ticker][symbol={}]任务开始" ,symbol);
		getTicker(symbol);
		logger.info("[Ticker][symbol={}]任务结束，耗时：" + stopwatch , symbol);
	}

	@Override
	public void updateHuobiDepth(String symbol,String type) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		logger.info("[Depth][symbol={},type={}]任务开始",symbol,type);
		getDepth(symbol, type);
		logger.info("[Ticker][symbol={},type={}]任务结束，耗时：" + stopwatch , symbol , type);
	}

	@Override
	public void updateCurrentPrice(String symbol) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		logger.info("[Depth][symbol={},type={}]任务开始",symbol);
		TradeResponse trade = queryCurrentPriceByApi(symbol);
		redisService.setHuobiCurrentPrice(ExchangeEnum.HUOBI.getExId(),symbol, trade);
		logger.info("[Ticker][symbol={},type={}]任务结束，耗时：" + stopwatch , symbol);
	}

	private TradeResponse queryCurrentPriceByApi(String symbol) {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", symbol);
		String body = httpService.doHuobiGet(HttpConstant.HUOBI_TRADE, params);
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
		tick.setId(jsonObject.getInteger("id"));
		tick.setTs(jsonObject.getDate("ts"));
		trade.setTick(tick);
		trade.setCh(parseObject.getString("ch"));
		trade.setStatus("status");
		trade.setTs(parseObject.getDate("ts"));
		return trade;
	}

}