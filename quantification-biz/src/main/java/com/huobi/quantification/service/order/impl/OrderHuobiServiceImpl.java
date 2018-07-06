package com.huobi.quantification.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.OrderStatus;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.OrderHuobiService;
import com.huobi.quantification.service.redis.RedisService;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public class OrderHuobiServiceImpl implements OrderHuobiService {
	
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
		return null;
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
		return null;
	}

	/**
	 * 批量撤单
	 *
	 * @return
	 */
	public Object cancelHuobiOrders() {
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
	        logger.info("[HuobiOrder][orderId={}]任务结束，耗时：" + started,orderId);
	}
}
