package com.huobi.quantification.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.OrderService;
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
public class OrderServiceImpl implements OrderService {

    @Autowired
    private HttpService httpService;

    @Autowired
    private QuanOrderFutureMapper quanOrderFutureMapper;

    @Override
    public Object getOkOrderInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("status", "1");
        params.put("order_id", "-1");
        params.put("current_page", "1");
        params.put("page_length", "50");
        String body = httpService.okSignedPost(HttpConstant.OK_ORDER_INFO, params);
        parseAndSaveOrderInfo(body);
        return null;
    }

    private void parseAndSaveOrderInfo(String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        boolean b = jsonObject.getBoolean("result");
        if (b) {
            List<QuanOrderFuture> list = new ArrayList<>();
            JSONArray orders = jsonObject.getJSONArray("orders");
            for (int i = 0; i < orders.size(); i++) {
                JSONObject order = orders.getJSONObject(i);
                list.add(parseOrderFuture(order));
            }
            for (QuanOrderFuture orderFuture : list) {
                quanOrderFutureMapper.insert(orderFuture);
            }
        }
    }

    private QuanOrderFuture parseOrderFuture(JSONObject order) {
        QuanOrderFuture orderFuture = new QuanOrderFuture();
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
        return orderFuture;
    }

    @Override
    public Object getOkOrdersInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("order_id", "1015885804614656");
        String body = httpService.okSignedPost(HttpConstant.OK_ORDERS_INFO, params);
        parseAndSaveOrderInfo(body);
        return null;
    }

    @Override
    public Object getOkOrdersHistory() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("date", "2018-06-29");
        params.put("since", "1015885804614656");
        String result = httpService.okSignedPost(HttpConstant.OK_TRADES_HISTORY, params);
        return null;
    }

    @Override
    public Object placeOkOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("price", "6050");
        params.put("amount", "1");
        params.put("type", "1");
        params.put("match_price", "0");
        params.put("lever_rate", "10");
        String result = httpService.okSignedPost(HttpConstant.OK_TRADE, params);
        return null;
    }

    @Override
    public Object placeOkOrders() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("orders_data", "[{price:6050,amount:0.0001,type:1,match_price:0}]");
        params.put("lever_rate", "10");
        String result = httpService.okSignedPost(HttpConstant.OK_BATCH_TRADE, params);
        return null;
    }

    @Override
    public Object cancelOkOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("order_id", "1010682583139328");
        String result = httpService.okSignedPost(HttpConstant.OK_CANCEL, params);
        return null;
    }

    @Override
    public Object cancelOkOrders() {
        // order_id以，好分割
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "");
        params.put("contract_type", "");
        params.put("order_id", "");
        String result = httpService.okSignedPost(HttpConstant.OK_CANCEL, params);
        return null;
    }
}
