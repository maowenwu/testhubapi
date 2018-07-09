package com.huobi.quantification.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.entity.QuanOrderMatchResult;
import com.huobi.quantification.enums.OrderStatus;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.HuobiOrderService;
import com.huobi.quantification.service.redis.RedisService;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public class HuobiOrderServiceImpl implements HuobiOrderService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private HttpService httpService;
	@Autowired
	private QuanOrderMapper quanOrderMapper;
	@Autowired
	private RedisService redisService;

	/**
	 * 获取订单信息
	 *
	 * @return
	 */
	public Object getHuobiOrderInfo() {
		Map<String, String> params = new HashMap<>();
		String body = httpService.doPost(HttpConstant.HUOBI_ORDERDETAIL, params);
		parseAndSaveOrderInfo(body);
		return null;
	}

	/**
	 * 批量获取订单信息
	 *
	 * @return
	 */
	public Object getHuobiOrdersInfo() {
		return null;
	}

	/**
	 * 查询成交（时间顺序）
	 *
	 * @return
	 */
	public Object getHuobiOrdersHistory() {
		String body = httpService.doGet(HttpConstant.HUOBI_MATCHRESULTS.replaceAll("//{order-id}//", "123"));
		parseAndSaveOrderMatchResult(body);
		return null;
	}

	private void parseAndSaveOrderMatchResult(String body) {
		JSONObject parseObject = JSON.parseObject(body);
		JSONArray jsonArray = parseObject.getJSONArray("data");
		QuanOrderMatchResult matchResult = new QuanOrderMatchResult();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject dataObject = jsonArray.getJSONObject(i);
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

	/**
	 * 下单
	 *
	 * @return
	 */
	public Object placeHuobiOrder() {
		Map<String, String> params = new HashMap<>();
		params.put("account-id", "100009");
		params.put("amount", "10.1");
		params.put("price", "100.1");
		params.put("source", "api");
		params.put("symbol", "ethusdt");
		params.put("type", "buy-limit");
		String result = httpService.doPost(HttpConstant.HUOBI_ORDER_PLACE, params);
		JSONObject parseObject = JSON.parseObject(result);
		if (parseObject.getString("status").equals("ok")) {
			String data = parseObject.getString("data");
			Long orderId = Long.parseLong(data);
			String body = httpService.doGet(HttpConstant.HUOBI_ORDERDETAIL.replaceAll("//{order-id//}", orderId+""));
			parseAndSaveOrderInfo(body);
		}
		return null;
	}

	/**
	 * 批量下单
	 *
	 * @return
	 */
	public Object placeHuobikOrders() {
		return null;
	}

	/**
	 * 撤单
	 *
	 * @return
	 */
	public Object cancelHuobiOrder() {
		httpService.doGet(HttpConstant.HUOBI_SUBMITCANCEL.replaceAll("//{order-id//}", "123"));
		return null;
	}

	/**
	 * 批量撤单
	 *
	 * @return
	 */
	public Object cancelHuobiOrders() {
		Map<String, String> params = new HashMap<>();
		List<Long> orderIds = new ArrayList<Long>();
		orderIds.add(1L);
		orderIds.add(2L);
		orderIds.add(3L);
		String jsonOrderIds = JSON.toJSONString(orderIds);
		params.put("order-ids", jsonOrderIds);
		httpService.doGet(HttpConstant.HUOBI_BATCHCANCEL, params);
		return null;
	}

	private void parseAndSaveOrderInfo(String body) {
		JSONObject jsonObject = JSON.parseObject(body);
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
		redisService.saveHuobiOrder(quanOrder);
	}

	@Override
	public void updateHuobiOrder(Long orderId) {
		Stopwatch started = Stopwatch.createStarted();
		logger.info("[HuobiOrder][orderId={}]任务开始", orderId);
		Map<String, String> params = new HashMap<>();
		params.put("order-id", orderId + "");
		String body = httpService.doPost(HttpConstant.HUOBI_ORDERDETAIL, params);
		parseAndSaveOrderInfo(body);
		logger.info("[HuobiOrder][orderId={}]任务结束，耗时：" + started, orderId);
	}

	/**
	 * 获取所有当前帐号下未成交订单
	 * 
	 * @return
	 */
	@Override
	public Object getHuobiOpenOrders() {
		Map<String, String> params = new HashMap<>();
		params.put("account_id", "1");
		params.put("symbol", "ethusdt");
		//params.put("side", "sell");
		params.put("size", "500");
		String body = httpService.doGet(HttpConstant.HUOBI_OPENORDERS, params);
		parseAndSaveOpenOrders(body);
		return null;
	}

	private void parseAndSaveOpenOrders(String body) {
		JSONObject parseObject = JSON.parseObject(body);
		JSONArray jsonArray = parseObject.getJSONArray("data");
		QuanOrder quanOrder = new QuanOrder();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject dataObject = jsonArray.getJSONObject(i);
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
		}
	}
}
