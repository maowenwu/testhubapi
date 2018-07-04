package com.huobi.quantification.service.market.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
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
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.MarketHuobiService;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public class MarketHuobiServiceImpl implements MarketHuobiService {

	@Autowired
	private HttpService httpService;
	@Autowired
	private QuanDepthDetailMapper quanDepthDetailMapper;
	@Autowired
	private QuanDepthMapper quanDepthMapper;
	@Autowired
	private QuanTickerMapper quanTickerMapper;

	public Object getTicker(String symbol) {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", symbol);
		String body = httpService.doGet(HttpConstant.HUOBI_TICKER, params);
		QuanTicker quanTicker = new QuanTicker();
		JSONObject jsonObject = JSON.parseObject(body);
		String data = jsonObject.getString("tick");
		JSONObject temp = JSON.parseObject(data);
		JSONArray jsonArray = temp.getJSONArray("ask");
		double ask = Double.parseDouble(jsonArray.get(0).toString());
		quanTicker.setAskPrice(BigDecimal.valueOf(ask));
		JSONArray jsonArray2 = temp.getJSONArray("bid");
		double bid = Double.parseDouble(jsonArray2.get(0).toString());
		quanTicker.setBidPrice(BigDecimal.valueOf(bid));
		Gson gson = new Gson();
		Merged merged = gson.fromJson(body, Merged.class);
		quanTicker.setExchangeId(ExchangeEnum.HUOBI.getExId());
		String ch = merged.getCh();
		BigDecimal low = temp.getBigDecimal("low");
		quanTicker.setLowPrice(low);
		BigDecimal high = temp.getBigDecimal("high");
		quanTicker.setHighPrice(high);
		BigDecimal close = temp.getBigDecimal("close");
		quanTicker.setLastPrice(close);
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
		return null;
	}

	public Object getDepth(String symbol, String type) {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", symbol);
		params.put("type", type);
		String body = httpService.doGet(HttpConstant.HUOBI_DEPTH, params);
		Gson gson = new Gson();
		DepthResponse marketDepth = gson.fromJson(body, DepthResponse.class);
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
}