package com.huobi.quantification.service.market;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanDepthDetailMapper;
import com.huobi.quantification.dao.QuanDepthMapper;
import com.huobi.quantification.dao.QuanTickerMapper;
import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanDepthDetail;
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

		String str = "{\r\n" + "  \"status\": \"ok\",\r\n" + "  \"ch\": \"market.btcusdt.depth.step1\",\r\n"
				+ "  \"ts\": 1489472598812,\r\n" + "  \"tick\": {\r\n" + "    \"id\": 1489464585412,\r\n"
				+ "    \"ts\": 1489464585407,\r\n" + "    \"bids\": [\r\n"
				+ "      [7964, 0.0678], // [price, amount]\r\n" + "      [7963, 0.9162],\r\n"
				+ "      [7961, 0.1],\r\n" + "      [7960, 12.8898],\r\n" + "      [7958, 1.2],\r\n"
				+ "      [7955, 2.1009],\r\n" + "      [7954, 0.4708],\r\n" + "      [7953, 0.0564],\r\n"
				+ "      [7951, 2.8031],\r\n" + "      [7950, 13.7785],\r\n" + "      [7949, 0.125],\r\n"
				+ "      [7948, 4],\r\n" + "      [7942, 0.4337],\r\n" + "      [7940, 6.1612],\r\n"
				+ "      [7936, 0.02],\r\n" + "      [7935, 1.3575],\r\n" + "      [7933, 2.002],\r\n"
				+ "      [7932, 1.3449],\r\n" + "      [7930, 10.2974],\r\n" + "      [7929, 3.2226]\r\n" + "    ],\r\n"
				+ "    \"asks\": [\r\n" + "      [7979, 0.0736],\r\n" + "      [7980, 1.0292],\r\n"
				+ "      [7981, 5.5652],\r\n" + "      [7986, 0.2416],\r\n" + "      [7990, 1.9970],\r\n"
				+ "      [7995, 0.88],\r\n" + "      [7996, 0.0212],\r\n" + "      [8000, 9.2609],\r\n"
				+ "      [8002, 0.02],\r\n" + "      [8008, 1],\r\n" + "      [8010, 0.8735],\r\n"
				+ "      [8011, 2.36],\r\n" + "      [8012, 0.02],\r\n" + "      [8014, 0.1067],\r\n"
				+ "      [8015, 12.9118],\r\n" + "      [8016, 2.5206],\r\n" + "      [8017, 0.0166],\r\n"
				+ "      [8018, 1.3218],\r\n" + "      [8019, 0.01],\r\n" + "      [8020, 13.6584]\r\n" + "    ]\r\n"
				+ "  }\r\n" + "}";
//		 JSONObject jsonObject = JSON.parseObject(str);
//		 String data=jsonObject.getString("tick");
//		 QuanDepth quanDepth=new QuanDepth();
//		 quanDepthMapper.insert(quanDepth);
//		 JSONObject temp=JSON.parseObject(data);
//		 JSONArray jsarr=temp.getJSONArray("asks");
//		 for (int i = 0; i < jsarr.size(); i++) {
//			 JSONArray item = jsarr.getJSONArray(i);
//			 QuanDepthDetail depthDetail=new QuanDepthDetail();
//			 depthDetail.setDetailPrice(item.getBigDecimal(0));
//			 // depthDetail.setDetailAmount(item.getDouble(1));
//			 depthDetail.setDepthId(quanDepth.getId());
//			 depthDetail.setDetailType(new Byte((byte) 1));
//			 quanDepthDetailMapper.insert(depthDetail);
//		 }
		Gson gson = new Gson();
		DepthResponse marketDepth = gson.fromJson(str, DepthResponse.class);
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
	}
	
	@Test
	public void getTicker() {
		String str = "{\r\n" + "\"status\":\"ok\",\r\n" + "\"ch\":\"market.ethusdt.detail.merged\",\r\n"
				+ "\"ts\":1499225276950,\r\n" + "\"tick\":{\r\n" + "  \"id\":1499225271,\r\n"
				+ "  \"ts\":1499225271000,\r\n" + "  \"close\":1885.0000,\r\n" + "  \"open\":1960.0000,\r\n"
				+ "  \"high\":1985.0000,\r\n" + "  \"low\":1856.0000,\r\n" + "  \"amount\":81486.2926,\r\n"
				+ "  \"count\":42122,\r\n" + "  \"vol\":157052744.85708200,\r\n" + "  \"ask\":[1885.0000,21.8804],\r\n"
				+ "  \"bid\":[1884.0000,1.6702]\r\n" + "  }\r\n" + "}";
		QuanTicker quanTicker = new QuanTicker();
		JSONObject jsonObject = JSON.parseObject(str);
		String data = jsonObject.getString("tick");
		JSONObject temp = JSON.parseObject(data);
		JSONArray jsonArray = temp.getJSONArray("ask");
		double ask = Double.parseDouble(jsonArray.get(0).toString());
		quanTicker.setAskPrice(BigDecimal.valueOf(ask));
		JSONArray jsonArray2 = temp.getJSONArray("bid");
		double bid = Double.parseDouble(jsonArray2.get(0).toString());
		quanTicker.setBidPrice(BigDecimal.valueOf(bid));
		Gson gson = new Gson();
		Merged merged = gson.fromJson(str, Merged.class);
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
	}
}
