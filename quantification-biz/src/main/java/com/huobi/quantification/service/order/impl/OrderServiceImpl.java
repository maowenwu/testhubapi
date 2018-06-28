package com.huobi.quantification.service.order.impl;

import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private HttpService httpService;

    @Override
    public Object getOkOrderInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("status", "1");
        params.put("order_id", "-1");
        params.put("current_page", "1");
        params.put("page_length", "50");
        String result = httpService.okSignedPost(HttpConstant.OK_ORDER_INFO, params);
        return null;
    }

    @Override
    public Object getOkOrdersInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("order_id", "11");
        String result = httpService.okSignedPost(HttpConstant.OK_ORDERS_INFO, params);
        return null;
    }

    @Override
    public Object getOkOrdersHistory() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("date", "2018-06-29");
        params.put("since", "1");
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
