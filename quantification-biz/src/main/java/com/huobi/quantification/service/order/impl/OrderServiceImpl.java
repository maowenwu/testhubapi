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
        params.put("symbol", "");
        params.put("contract_type", "");
        params.put("status", "");
        params.put("order_id", "");
        params.put("current_page", "");
        params.put("page_length", "");
        String result = httpService.okSignedPost(HttpConstant.OK_ORDER_INFO, params);
        return null;
    }

    @Override
    public Object getOkOrdersInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "");
        params.put("contract_type", "");
        params.put("order_id", "");
        String result = httpService.okSignedPost(HttpConstant.OK_ORDERS_INFO, params);
        return null;
    }

    @Override
    public Object getOkOrdersHistory() {
        return null;
    }

    @Override
    public Object placeOkOrder() {
        return null;
    }

    @Override
    public Object placeOkOrders() {
        return null;
    }

    @Override
    public Object cancelOkOrder() {
        return null;
    }

    @Override
    public Object cancelOkOrders() {
        return null;
    }
}
