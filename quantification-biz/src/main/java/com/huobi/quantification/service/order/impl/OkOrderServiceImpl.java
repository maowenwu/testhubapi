package com.huobi.quantification.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.constant.OkRestErrorCode;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OrderStatus;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.request.future.FutureOkBatchOrderRequest;
import com.huobi.quantification.request.future.FutureOkCancelOrderRequest;
import com.huobi.quantification.request.future.FutureOkOrderRequest;
import com.huobi.quantification.response.future.OKFutureCancelOrderResponse;
import com.huobi.quantification.response.future.OKFutureOrderResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.OkOrderService;
import com.huobi.quantification.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
@Transactional
public class OkOrderServiceImpl implements OkOrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpService httpService;

    @Autowired
    private QuanOrderFutureMapper quanOrderFutureMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public ServiceResult getOkOrderInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("status", "1");
        params.put("order_id", "-1");
        params.put("current_page", "1");
        params.put("page_length", "50");
        //String body = httpService.doOkSignedPost(HttpConstant.OK_ORDER_INFO, params);
        //parseAndSaveOrderInfo(body);
        return null;
    }

    public void updateOkOrderInfo(Long accountId, String symbol, String contractType) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[OkOrder][symbol={},contractType={}]任务开始", symbol, contractType);
        List<QuanOrderFuture> unfinishOrder = queryAllOkOrderInfoByStatus(accountId, symbol, contractType, OrderStatus.UNFINISH);
        // 查找出订单状态不为已完成、撤单的订单
        //List<Long> orderIds = quanOrderFutureMapper.selectUnfinishOrderSourceId();
       /* List<QuanOrderFuture> orderFutures = queryOkOrdersInfoByAPI(accountId, symbol, contractType, orderIds);*/

        List<QuanOrderFuture> updateOrders = new ArrayList<>();
        updateOrders.addAll(unfinishOrder);
        /*updateOrders.addAll(orderFutures);*/
        for (QuanOrderFuture orderFuture : updateOrders) {
            //quanOrderFutureMapper.insertOrUpdate(orderFuture);
        }
        logger.info("[OkOrder][symbol={},contractType={}]任务结束，耗时：" + started, symbol, contractType);
    }

    private List<QuanOrderFuture> updateRedisOkOrderInfo(Long accountId, String symbol, String contractType) {
        Map<String, QuanOrderFuture> ordersMap = redisService.getOkOrder(accountId, symbol, contractType);
        List<Long> orderIds = new ArrayList<>();
        for (QuanOrderFuture orderFuture : ordersMap.values()) {
           /* if (orderFuture.getOrderStatus() == null || !orderFuture.getOrderStatus().equals(2)) {
                orderIds.add(orderFuture.getOrderSourceId());
            }*/
        }
        if (CollectionUtils.isEmpty(orderIds)) {
            return new ArrayList<>();
        }
        List<QuanOrderFuture> orderFutures = queryOkOrdersInfoByAPI(accountId, symbol, contractType, orderIds);
        for (QuanOrderFuture orderFuture : orderFutures) {
            /*ordersMap.put(orderFuture.getOrderSourceId() + "", orderFuture);*/
        }
        return orderFutures;
    }


    public List<QuanOrderFuture> queryOkOrdersInfoByAPI(Long accountId, String symbol, String contractType, List<Long> orderIds) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        if (orderIds.size() <= 0) {
            return new ArrayList<>();
        }
        String orderId = Joiner.on(",").join(orderIds);
        params.put("order_id", orderId);
        String body = httpService.doOkSignedPost(accountId, HttpConstant.OK_ORDERS_INFO, params);
        return parseAndSaveOrderInfo(accountId, body);
    }

    private List<QuanOrderFuture> queryAllOkOrderInfo(Long accountId, String symbol, String contractType) {
        List<QuanOrderFuture> list = new ArrayList<>();
        Stopwatch started = Stopwatch.createStarted();
        List<QuanOrderFuture> finishOrder = queryAllOkOrderInfoByStatus(accountId, symbol, contractType, OrderStatus.FINISH);
        List<QuanOrderFuture> unfinishOrder = queryAllOkOrderInfoByStatus(accountId, symbol, contractType, OrderStatus.UNFINISH);
        logger.debug("单个订单查询耗时：" + started);
        list.addAll(finishOrder);
        list.addAll(unfinishOrder);
        return list;
    }

    /**
     * 按分页的方式读取所有订单
     *
     * @param accountId
     * @param symbol
     * @param contractType
     * @param status
     * @return
     */
    private List<QuanOrderFuture> queryAllOkOrderInfoByStatus(Long accountId, String symbol, String contractType, OrderStatus status) {
        int pageLength = 50;
        List<QuanOrderFuture> list = new ArrayList<>();
        int i = 1;
        while (true) {
            List<QuanOrderFuture> orderFutures = queryOkOrderInfoByAPI(accountId, symbol, contractType, status, String.valueOf(-1), i, pageLength);
            i++;
            if (orderFutures.size() <= 0) {
                break;
            }
            list.addAll(orderFutures);
        }
        return list;
    }


    private List<QuanOrderFuture> queryOkOrderInfoByAPI(Long accountId, String symbol, String contractType, OrderStatus status, String orderId, int currentPage, int pageLength) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        params.put("status", status.getIntStatus() + "");
        params.put("order_id", orderId);
        params.put("current_page", currentPage + "");
        params.put("page_length", pageLength + "");
        String body = httpService.doOkSignedPost(accountId, HttpConstant.OK_ORDER_INFO, params);
        return parseAndSaveOrderInfo(accountId, body);
    }

    private List<QuanOrderFuture> parseAndSaveOrderInfo(Long accountId, String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        List<QuanOrderFuture> list = new ArrayList<>();
        boolean b = jsonObject.getBoolean("result");
        if (b) {
            JSONArray orders = jsonObject.getJSONArray("orders");
            for (int i = 0; i < orders.size(); i++) {
                JSONObject order = orders.getJSONObject(i);
                QuanOrderFuture orderFuture = parseOkFutureOrder(order);
               /* orderFuture.setOrderAccountId(accountId);*/
                orderFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
                list.add(orderFuture);
            }
        }
        return list;
    }

    private QuanOrderFuture parseOkFutureOrder(JSONObject order) {
       /* QuanOrderFuture orderFuture = new QuanOrderFuture();
        orderFuture.setOrderAmount(order.getBigDecimal("amount"));
        orderFuture.setContractName(order.getString("contract_name"));
        orderFuture.setCreateDate(new Date(order.getLong("create_date")));
        orderFuture.setOrderDealAmount(order.getBigDecimal("deal_amount"));
        orderFuture.setOrderFee(order.getBigDecimal("fee"));
        orderFuture.setOrderSourceId(order.getLong("order_id"));
        orderFuture.setOrderPrice(order.getBigDecimal("price"));
        orderFuture.setOrderPriceAvg(order.getBigDecimal("price_avg"));
        orderFuture.setOrderStatus(order.getInteger("status"));
        orderFuture.setOrderSymbol(order.getString("symbol"));
        orderFuture.setOrderType(order.getInteger("type"));
        orderFuture.setUnitAmount(order.getBigDecimal("unit_amount"));
        orderFuture.setOrderLeverRate(order.getBigDecimal("lever_rate"));
        orderFuture.setUpdateDate(new Date());
        return orderFuture;*/
        return null;
    }

    @Override
    public ServiceResult getOkOrdersInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("order_id", "1015885804614656");
        // String body = httpService.doOkSignedPost(HttpConstant.OK_ORDERS_INFO, params);
        //parseAndSaveOrderInfo(body);
        return null;
    }

    @Override
    public Object storeOkOrdersHistory() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("date", "2018-06-29");
        params.put("since", "1015885804614656");
        //String result = httpService.doOkSignedPost(HttpConstant.OK_TRADES_HISTORY, params);
        return null;
    }

    @Override
    public Long placeOkOrder(FutureOkOrderRequest order) {
        OKFutureOrderResponse response = placeOkOrderByAPI(order);
        if (response.isResult()) {
            return response.getOrderId();
        } else {
            throw new RuntimeException(OkRestErrorCode.findErrorMessageByCode(response.getErrorCode()));
        }
    }

    private OKFutureOrderResponse placeOkOrderByAPI(FutureOkOrderRequest order) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", order.getSymbol());
        params.put("contract_type", order.getContractType());
        params.put("price", order.getPrice());
        params.put("amount", order.getAmount());
        params.put("type", order.getType() + "");
        params.put("match_price", order.getMatchPrice() + "");
        if (order.getLeverRate() != null) {
            params.put("lever_rate", String.valueOf(order.getLeverRate()));
        }
        String body = httpService.doOkSignedPost(order.getAccountId(), HttpConstant.OK_TRADE, params);
        return JSON.parseObject(body, OKFutureOrderResponse.class);
    }

    @Override
    public ServiceResult placeOkOrders(FutureOkBatchOrderRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("orders_data", "[{price:6050,amount:0.0001,type:1,match_price:0}]");
        params.put("lever_rate", "10");
        String result = httpService.doOkSignedPost(request.getAccountId(), HttpConstant.OK_BATCH_TRADE, params);
        return null;
    }

    @Override
    public OKFutureCancelOrderResponse cancelOkOrder(FutureOkCancelOrderRequest cancelOrderDto) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", cancelOrderDto.getSymbol());
        params.put("contract_type", cancelOrderDto.getContractType());
        params.put("order_id", cancelOrderDto.getOrderId());
        String body = httpService.doOkSignedPost(cancelOrderDto.getAccountId(), HttpConstant.OK_CANCEL, params);
        return JSON.parseObject(body, OKFutureCancelOrderResponse.class);
    }

    @Override
    public ServiceResult cancelOkOrders() {
        // order_id以，好分割
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "");
        params.put("contract_type", "");
        params.put("order_id", "");
        //String result = httpService.doOkSignedPost(HttpConstant.OK_CANCEL, params);
        return null;
    }
}
