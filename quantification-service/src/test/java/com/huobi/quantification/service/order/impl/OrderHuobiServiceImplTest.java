package com.huobi.quantification.service.order.impl;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.entity.QuanOrderMatchResult;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class OrderHuobiServiceImplTest {
	
	@Autowired
	private QuanOrderMapper quanOrderMapper;
	
	@Test
	public void getOrder() {
		String string = "{\r\n" + 
				"  \"status\": \"ok\",\r\n" + 
				"  \"data\": {\r\n" + 
				"    \"id\": 59378,\r\n" + 
				"    \"symbol\": \"ethusdt\",\r\n" + 
				"    \"account-id\": 100009,\r\n" + 
				"    \"amount\": \"10.1000000000\",\r\n" + 
				"    \"price\": \"100.1000000000\",\r\n" + 
				"    \"created-at\": 1494901162595,\r\n" + 
				"    \"type\": \"buy-limit\",\r\n" + 
				"    \"field-amount\": \"10.1000000000\",\r\n" + 
				"    \"field-cash-amount\": \"1011.0100000000\",\r\n" + 
				"    \"field-fees\": \"0.0202000000\",\r\n" + 
				"    \"finished-at\": 1494901400468,\r\n" + 
				"    \"user-id\": 1000,\r\n" + 
				"    \"source\": \"api\",\r\n" + 
				"    \"state\": \"filled\",\r\n" + 
				"    \"canceled-at\": 0,\r\n" + 
				"    \"exchange\": \"huobi\",\r\n" + 
				"    \"batch\": \"\"\r\n" + 
				"  }\r\n" + 
				"}";
		JSONObject jsonObject = JSON.parseObject(string);
		JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
		QuanOrder quanOrder = new QuanOrder();
		quanOrder.setOrderSourceId(jsonObjectdata.getLong("id"));
		quanOrder.setOrderSymbol(jsonObjectdata.getString("symbol"));
		quanOrder.setOrderAccountId(jsonObjectdata.getLong("account-id"));
		quanOrder.setOrderAmount(new BigDecimal(jsonObjectdata.getString("amount")));
		quanOrder.setOrderPrice(new BigDecimal(jsonObjectdata.getString("price")));
		quanOrder.setOrderCreatedAt(jsonObjectdata.getDate("created-at"));
		quanOrder.setOrderType(jsonObjectdata.getString("type"));
		quanOrder.setOrderFieldAmount(new BigDecimal(jsonObjectdata.getString("field-amount")));
		quanOrder.setOrderFieldCashAmount(new BigDecimal(jsonObjectdata.getString("field-cash-amount")));
		quanOrder.setOrderFieldFees(new BigDecimal(jsonObjectdata.getString("field-fees")));
		quanOrder.setOrderFinishedAt(jsonObjectdata.getDate("finished-at"));
		quanOrder.setOrderAccountId(jsonObjectdata.getLong("user-id"));
		quanOrder.setOrderSource(jsonObjectdata.getString("source"));
		quanOrder.setOrderState(jsonObjectdata.getString("state"));
		quanOrder.setOrderCanceledAt(jsonObjectdata.getDate("canceled-at"));
		quanOrderMapper.insert(quanOrder);
	}
	
	@Test
	public void testParseOpenOrder() {
		String body = "{\r\n" + 
				"  \"status\": \"ok\",\r\n" + 
				"  \"data\": [\r\n" + 
				"    {\r\n" + 
				"      \"id\": 5454937,\r\n" + 
				"      \"symbol\": \"ethusdt\",\r\n" + 
				"      \"account-id\": 30925,\r\n" + 
				"      \"amount\": \"1.000000000000000000\",\r\n" + 
				"      \"price\": \"0.453000000000000000\",\r\n" + 
				"      \"created-at\": 1530604762277,\r\n" + 
				"      \"type\": \"sell-limit\",\r\n" + 
				"      \"filled-amount\": \"0.0\",\r\n" + 
				"      \"filled-cash-amount\": \"0.0\",\r\n" + 
				"      \"filled-fees\": \"0.0\",\r\n" + 
				"      \"source\": \"web\",\r\n" + 
				"      \"state\": \"submitted\"\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
		JSONObject parseObject = JSON.parseObject(body);
		JSONArray jsonArray = parseObject.getJSONArray("data");
		JSONObject dataObject = jsonArray.getJSONObject(0);
		QuanOrder quanOrder = new QuanOrder();
		quanOrder.setOrderSourceId(dataObject.getLong("id"));
		quanOrder.setOrderSymbol(dataObject.getString("symbol"));
		quanOrder.setOrderPrice(dataObject.getBigDecimal("price"));
		quanOrder.setOrderAmount(dataObject.getBigDecimal("amount"));
		quanOrder.setOrderCreatedAt(dataObject.getDate("created-at"));
		quanOrder.setOrderType(dataObject.getString("type"));
		quanOrder.setOrderFieldAmount(dataObject.getBigDecimal("filled-amount"));
		quanOrder.setOrderFieldCashAmount(dataObject.getBigDecimal("filled-fees"));
		quanOrder.setOrderSource(dataObject.getString("source"));
		quanOrder.setOrderState(dataObject.getString("state"));
		quanOrderMapper.insert(quanOrder);
	}
	
	@Test
	public void testParseMatchResult() {
		String body = "{\r\n" + 
				"  \"status\": \"ok\",\r\n" + 
				"  \"data\": [\r\n" + 
				"    {\r\n" + 
				"      \"id\": 29553,\r\n" + 
				"      \"order-id\": 59378,\r\n" + 
				"      \"match-id\": 59335,\r\n" + 
				"      \"symbol\": \"ethusdt\",\r\n" + 
				"      \"type\": \"buy-limit\",\r\n" + 
				"      \"source\": \"api\",\r\n" + 
				"      \"price\": \"100.1000000000\",\r\n" + 
				"      \"filled-amount\": \"9.1155000000\",\r\n" + 
				"      \"filled-fees\": \"0.0182310000\",\r\n" + 
				"      \"created-at\": 1494901400435\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
		JSONObject parseObject = JSON.parseObject(body);
		JSONArray jsonArray = parseObject.getJSONArray("data");
		JSONObject dataObject = jsonArray.getJSONObject(0);
		QuanOrderMatchResult matchResult = new QuanOrderMatchResult();
		matchResult.setMatchId(dataObject.getLong("match-id"));
		matchResult.setMatchResultId(dataObject.getLong("id"));
		matchResult.setOrderCreatedAt(dataObject.getDate("created-at"));
		matchResult.setOrderFilledAmount(dataObject.getBigDecimal("filled-amount"));
		matchResult.setOrderFilledFees(dataObject.getBigDecimal("filled-fees"));
		matchResult.setOrderId(dataObject.getLong("order-id"));
		matchResult.setOrderPrice(dataObject.getBigDecimal("price"));
		matchResult.setOrderSource(dataObject.getString("source"));
		matchResult.setOrderSymbol(dataObject.getString("symbol"));
		matchResult.setOrderType(dataObject.getString("type"));
	}
}
