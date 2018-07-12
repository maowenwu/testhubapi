package com.huobi.quantification.service.market;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanDepthDetailMapper;
import com.huobi.quantification.dao.QuanDepthMapper;
import com.huobi.quantification.dao.QuanTickerMapper;
import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanTicker;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.huobi.response.DepthResponse;
import com.huobi.quantification.huobi.response.Merged;
import com.huobi.quantification.service.http.HttpService;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class MarketHuobiServiceTest {
	@Autowired
	private QuanDepthDetailMapper quanDepthDetailMapper;
	@Autowired
	private QuanDepthMapper quanDepthMapper;
	@Autowired
	private QuanTickerMapper quanTickerMapper;
	@Autowired
	private HttpService httpService;

	@Test
	public void getDepth() {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", "ethusdt");
		params.put("type", "step1");
		String body = httpService.doHuobiGet(HttpConstant.HUOBI_DEPTH, params);
		DepthResponse marketDepth = JSON.parseObject(body, DepthResponse.class);
		QuanDepth quanDepth = new QuanDepth();
		quanDepth.setExchangeId(ExchangeEnum.HUOBI.getExId());
		String ch = marketDepth.getCh();
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
	}
	
	@Test
	public void getTicker() {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", "ethusdt");
		String body = httpService.doHuobiGet(HttpConstant.HUOBI_TICKER, params);
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
		String ch = jsonObject.getString("ch");
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
	}
}
