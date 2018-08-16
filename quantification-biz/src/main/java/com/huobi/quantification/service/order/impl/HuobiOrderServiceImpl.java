package com.huobi.quantification.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.constant.OrderStatusTable.HuobiOrderStatus;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.dto.HuobiTradeOrderDto;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.entity.QuanOrderMatchResult;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.huobi.request.CreateOrderRequest;
import com.huobi.quantification.huobi.request.HuobiOpenOrderRequest;
import com.huobi.quantification.response.spot.HuobiSpotOrderResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.HuobiOrderService;
import com.huobi.quantification.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
@Service
@Transactional
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
    public Object getHuobiOrderInfo(Long accountId) {
        Map<String, String> params = new HashMap<>();
        String body = httpService.doHuobiSpotPost(accountId, HttpConstant.HUOBI_ORDERDETAIL, params);
        parseAndSaveOrderInfo(body);
        return null;
    }

    /**
     * 批量获取订单信息
     *
     * @return
     */
    public Object getHuobiOrdersInfo(Long accountId) {
        return null;
    }

    /**
     * 查询成交（时间顺序）
     *
     * @return
     */
    public Object getHuobiOrdersHistory(Long accountId, Long orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("order-id", orderId + "");
        String body = httpService.doHuobiSpotGet(accountId, HttpConstant.HUOBI_MATCHRESULTS.replaceAll("\\{order-id\\}", "" + orderId), params);
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
    public Long placeHuobiOrder(HuobiTradeOrderDto orderDto) {
        // todo
        return System.currentTimeMillis();
		/*HuobiSpotOrderResponse response = placeOkOrderByAPI(orderDto);
        if (response.getStatus().equals("ok")) {
            return response.getOrderId();
        } else {
            throw new RuntimeException(response.getErrorCode());
        }*/
    }

    private HuobiSpotOrderResponse placeOkOrderByAPI(HuobiTradeOrderDto orderDto) {
        CreateOrderRequest createOrderReq = new CreateOrderRequest();
        createOrderReq.accountId = String.valueOf(orderDto.getAccountId());
        createOrderReq.amount = orderDto.getAmount().toString();
        createOrderReq.price = orderDto.getPrice().toString();
        createOrderReq.symbol = orderDto.getSymbol();
        createOrderReq.type = orderDto.getType();
        createOrderReq.source = orderDto.getSource();
        String body = httpService.doHuobiSpotPost(orderDto.getAccountId(), HttpConstant.HUOBI_ORDER_PLACE, createOrderReq);
        return JSON.parseObject(body, HuobiSpotOrderResponse.class);
    }

    /**
     * 批量下单
     *
     * @return
     */
    public Object placeHuobikOrders(Long accountId) {
        return null;
    }

    /**
     * 撤单
     *
     * @return
     */
    public Object cancelHuobiOrder(Long accountId, Long orderId) {
        httpService.doHuobiSpotPost(accountId, HttpConstant.HUOBI_SUBMITCANCEL.replaceAll("\\{order-id\\}", orderId + ""), null);
        return null;
    }

    /**
     * 批量撤单
     *
     * @return
     */
    public Object cancelHuobiOrders(Long accountId) {
        Map<String, String> params = new HashMap<>();
        List<Long> orderIds = new ArrayList<Long>();
        orderIds.add(1L);
        orderIds.add(2L);
        orderIds.add(3L);
        String jsonOrderIds = JSON.toJSONString(orderIds);
        params.put("order-ids", jsonOrderIds);
        httpService.doHuobiSpotGet(accountId, HttpConstant.HUOBI_BATCHCANCEL, params);
        return null;
    }

    private void parseAndSaveOrderInfo(String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
        QuanOrder quanOrder = new QuanOrder();
        quanOrder.setExchangeId(ExchangeEnum.HUOBI.getExId());
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
        String state = jsonObjectdata.getString("state");
        quanOrder.setOrderState(HuobiOrderStatus.getOrderStatus(state).getOrderStatus());
        quanOrder.setOrderCanceledAt(jsonObjectdata.getDate("canceled-at"));
        quanOrderMapper.insert(quanOrder);
    }

	/*@Override
	public void updateHuobiOrder() {
		Stopwatch started = Stopwatch.createStarted();
		logger.info("[HuobiOrderUpdate]任务开始");
		List<Integer> states = new ArrayList<>();
		states.add(OrderStatusEnum.PRE_SUBMITTED.getOrderStatus());
		states.add(OrderStatusEnum.SUBMITTED.getOrderStatus());
		states.add(OrderStatusEnum.PARTIAL_FILLED.getOrderStatus());
		List<QuanOrder> list1 = quanOrderMapper.selectByOrderInfo(states);
		Map<String, String> params = new HashMap<>();
		for (QuanOrder quanOrder : list1) {
			Long orderSourceId = quanOrder.getOrderSourceId();
			params.put("order-id", orderSourceId + "");
			String body = httpService.doHuobiSpotGet(quanOrder.getOrderAccountId(), HttpConstant.HUOBI_ORDERDETAIL, params);
			updateHuobiOrderInfo(body, orderSourceId);
		}
		logger.info("[HuobiOrderUpdate]任务结束，耗时：" + started);
	}*/

    private void updateHuobiOrderInfo(String body, Long orderId) {
        JSONObject jsonObject = JSON.parseObject(body);
        JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
        QuanOrder quanOrder = new QuanOrder();
        quanOrder.setExchangeId(ExchangeEnum.HUOBI.getExId());
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
        quanOrderMapper.updateOrderByOrderId(quanOrder);

    }

    /**
     * 获取所有当前帐号下未成交订单
     *
     * @return
     */
    @Override
    public Object getHuobiOpenOrders(Long accountId) {
        HuobiOpenOrderRequest huobiOpenOrderRequest = new HuobiOpenOrderRequest();
        huobiOpenOrderRequest.accountId = "4232061";
        huobiOpenOrderRequest.symbol = "";
        huobiOpenOrderRequest.side = "buy";
        huobiOpenOrderRequest.size = "100";
        String doHuobiPost = httpService.doHuobiSpotPost(accountId, HttpConstant.HUOBI_OPENORDERS, huobiOpenOrderRequest);
        parseAndSaveOpenOrders(doHuobiPost);
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
            quanOrder.setOrderState(HuobiOrderStatus.getOrderStatus(dataObject.getString("state")).getOrderStatus());
        }
    }
}
