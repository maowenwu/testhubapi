package com.huobi.quantification.service.market;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.huobi.quantification.dao.QuanKlineMapper;
import com.huobi.quantification.dao.QuanTickerMapper;
import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanDepthDetail;
import com.huobi.quantification.entity.QuanKline;
import com.huobi.quantification.entity.QuanTicker;
import com.huobi.quantification.enums.DepthEnum;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.huobi.response.Merged;
import com.huobi.quantification.huobi.response.Trade;
import com.huobi.quantification.huobi.response.TradeDetail;
import com.huobi.quantification.huobi.response.TradeResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;

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
	private QuanKlineMapper quanKlineMapper;
	@Autowired
	private HttpService httpService;
	@Autowired
	private RedisService redisService;

	@Test
	public void getDepth() {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", "ethusdt");
		params.put("type", "step1");
		String body = httpService.doGet(HttpConstant.HUOBI_DEPTH, params);
		JSONObject jsonObject = JSON.parseObject(body);
		QuanDepth quanDepth = new QuanDepth();
		quanDepth.setExchangeId(ExchangeEnum.HUOBI.getExId());
		String ch = jsonObject.getString("ch");
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
		redisService.saveHuobiDepth(ExchangeEnum.HUOBI.getExId(),"ethusdt", list);
	}
	
	@Test
	public void getTicker() {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", "ethusdt");
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
	
	@Test
	public void getTrade() {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", "xrpbtc");
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
		tick.setId(jsonObject.getInteger("id"));
		tick.setTs(jsonObject.getDate("ts"));
		trade.setTick(tick);
		trade.setCh(parseObject.getString("ch"));
		trade.setStatus("status");
		trade.setTs(parseObject.getDate("ts"));
		System.err.println("price:"+data.getPrice());
		System.err.println("tick.ts:"+tick.getTs());
		System.err.println("ch:"+trade.getCh());
		redisService.setHuobiCurrentPrice(ExchangeEnum.HUOBI.getExId(), "xrpbtc", trade);
	}
	
	@Test
	public void getKline() {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", "btcusdt");
		params.put("period", "5min");
		params.put("size", "200");
		String body = httpService.doGet(HttpConstant.HUOBI_KLINE, params);
		JSONObject jsonObject = JSON.parseObject(body);
		if (jsonObject.getString("status").equals("ok")) {
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			QuanKline quanKline = new QuanKline();
			List<QuanKline> arrayList = new ArrayList<>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject klineObject = jsonArray.getJSONObject(i);
				quanKline.setId(klineObject.getLong("id"));
				quanKline.setAmount(klineObject.getBigDecimal("amount"));
				quanKline.setClose(klineObject.getBigDecimal("close"));
				quanKline.setCount(klineObject.getBigDecimal("count"));
				quanKline.setExchangeId(1);
				quanKline.setHigh(klineObject.getBigDecimal("high"));
				quanKline.setLow(klineObject.getBigDecimal("low"));
				quanKline.setOpen(klineObject.getBigDecimal("open"));
				quanKline.setPeriod("1day");
				quanKline.setSize(Long.parseLong("200"));
				quanKline.setSymbol("btcusdt");
				quanKline.setTs(jsonObject.getDate("ts"));
				quanKline.setVol(klineObject.getBigDecimal("vol"));
				quanKlineMapper.insert(quanKline);
				arrayList.add(quanKline);
			}
			redisService.saveKline(ExchangeEnum.HUOBI.getExId(), "btcusdt", "5min", arrayList);
		}
	}
}
