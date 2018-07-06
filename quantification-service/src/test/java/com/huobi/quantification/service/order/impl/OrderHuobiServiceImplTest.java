package com.huobi.quantification.service.order.impl;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.entity.QuanOrder;

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
}
