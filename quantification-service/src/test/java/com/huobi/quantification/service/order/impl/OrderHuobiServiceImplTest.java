package com.huobi.quantification.service.order.impl;

import java.math.BigDecimal;
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
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.constant.OrderStatusTable.HuobiOrderStatus;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.huobi.request.CreateOrderRequest;
import com.huobi.quantification.huobi.request.HuobiOpenOrderRequest;
import com.huobi.quantification.service.http.HttpService;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class OrderHuobiServiceImplTest {

	@Autowired
	private QuanOrderMapper quanOrderMapper;

	@Autowired
	private HttpService httpService;

	@Test
	public void CreateOrders() {
		CreateOrderRequest createOrderReq = new CreateOrderRequest();
		createOrderReq.accountId = "4295363";
		createOrderReq.amount = "1";
		createOrderReq.price = "0.02";
		createOrderReq.symbol = "eosusdt";
		createOrderReq.type = CreateOrderRequest.OrderType.BUY_LIMIT;
		createOrderReq.source = "api";
		String result = httpService.doHuobiPost(4295363L,HttpConstant.HUOBI_ORDER_PLACE, createOrderReq);
//		HuobiSpotOrderResponse parseObject = JSON.parseObject(result, HuobiSpotOrderResponse.class);
//		if (parseObject.getStatus().equals("ok")) {
//			System.err.println(parseObject.getStatus());
//			System.err.println(parseObject.getOrderId());
//		}else {
//			System.err.println(parseObject.getStatus());
//			System.err.println(parseObject.getErrorCode());
//		}
		System.out.println("result========="+result);
		JSONObject parseObject = JSON.parseObject(result);
		if (parseObject.getString("status").equals("ok")) {
			Map<String, String> params = new HashMap<>();
			String data = parseObject.getString("data");
			params.put("order-id", data);
			String body = httpService.doHuobiGet(4232061L,HttpConstant.HUOBI_ORDERDETAIL.replaceAll("\\{order-id\\}", data),params);
			System.err.println(body);
//			JSONObject jsonObject = JSON.parseObject(body);
//			JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
//			QuanOrder quanOrder = new QuanOrder();
//			quanOrder.setOrderSourceId(jsonObjectdata.getLong("id"));
//			quanOrder.setOrderSymbol(jsonObjectdata.getString("symbol"));
//			quanOrder.setOrderAccountId(jsonObjectdata.getLong("account-id"));
//			quanOrder.setOrderAmount(new BigDecimal(jsonObjectdata.getString("amount")));
//			quanOrder.setOrderPrice(new BigDecimal(jsonObjectdata.getString("price")));
//			quanOrder.setOrderCreatedAt(jsonObjectdata.getDate("created-at"));
//			quanOrder.setOrderType(jsonObjectdata.getString("type"));
//			quanOrder.setOrderFieldAmount(new BigDecimal(jsonObjectdata.getString("field-amount")));
//			quanOrder.setOrderFieldCashAmount(new BigDecimal(jsonObjectdata.getString("field-cash-amount")));
//			quanOrder.setOrderFieldFees(new BigDecimal(jsonObjectdata.getString("field-fees")));
//			quanOrder.setOrderFinishedAt(jsonObjectdata.getDate("finished-at"));
//			quanOrder.setOrderAccountId(jsonObjectdata.getLong("user-id"));
//			quanOrder.setOrderSource(jsonObjectdata.getString("source"));
//			quanOrder.setOrderState(HuobiOrderStatus.getOrderStatus(jsonObject.getString("state")).getOrderStatus());
//			quanOrder.setOrderCanceledAt(jsonObjectdata.getDate("canceled-at"));
//			quanOrderMapper.insert(quanOrder);
		}
	}
	
	@Test
	public void cancelOrder() {
		String doHuobiPost = httpService.doHuobiPost(4232061L,HttpConstant.HUOBI_SUBMITCANCEL.replaceAll("\\{order-id\\}", "7609870737"), null);
		JSONObject parseObject = JSON.parseObject(doHuobiPost);
		System.err.println("取消订单，" + parseObject.getString("status"));
	}

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
		quanOrder.setOrderState(HuobiOrderStatus.getOrderStatus(jsonObjectdata.getString("state")).getOrderStatus());
		quanOrder.setOrderCanceledAt(jsonObjectdata.getDate("canceled-at"));
		quanOrderMapper.insert(quanOrder);
	}

	@Test
	public void testParseOpenOrder() {
		HuobiOpenOrderRequest huobiOpenOrderRequest = new HuobiOpenOrderRequest();
		huobiOpenOrderRequest.accountId ="4232061";
		huobiOpenOrderRequest.symbol = "";
		huobiOpenOrderRequest.side ="buy";
		huobiOpenOrderRequest.size = "100";
		String doHuobiPost = httpService.doHuobiPost(4232061L,HttpConstant.HUOBI_OPENORDERS, huobiOpenOrderRequest);
		JSONObject parseObject = JSON.parseObject(doHuobiPost);
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
		quanOrder.setOrderState(HuobiOrderStatus.getOrderStatus(dataObject.getString("state")).getOrderStatus());
		quanOrderMapper.insert(quanOrder);
	}

	@Test
	public void testParseMatchResult() {
		Map<String, String> params = new HashMap<>();
		params.put("order-id", "7610858684");
		String body = httpService.doHuobiGet(4232061L,HttpConstant.HUOBI_MATCHRESULTS.replaceAll("\\{order-id\\}", ""), params);
		System.err.println(body);
//		JSONObject parseObject = JSON.parseObject(body);
//		JSONArray jsonArray = parseObject.getJSONArray("data");
//		JSONObject dataObject = jsonArray.getJSONObject(0);
//		QuanOrderMatchResult matchResult = new QuanOrderMatchResult();
//		matchResult.setMatchId(dataObject.getLong("match-id"));
//		matchResult.setMatchResultId(dataObject.getLong("id"));
//		matchResult.setOrderCreatedAt(dataObject.getDate("created-at"));
//		matchResult.setOrderFilledAmount(dataObject.getBigDecimal("filled-amount"));
//		matchResult.setOrderFilledFees(dataObject.getBigDecimal("filled-fees"));
//		matchResult.setOrderId(dataObject.getLong("order-id"));
//		matchResult.setOrderPrice(dataObject.getBigDecimal("price"));
//		matchResult.setOrderSource(dataObject.getString("source"));
//		matchResult.setOrderSymbol(dataObject.getString("symbol"));
//		matchResult.setOrderType(dataObject.getString("type"));
//		quanOrderMatchResultMapper.insert(matchResult);
	}
}
