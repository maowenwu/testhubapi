package com.huobi.quantification.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.bo.HuobiSpotCancelAllOrderBO;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dto.HuobiTradeOrderDto;
import com.huobi.quantification.execeptions.APIException;
import com.huobi.quantification.huobi.request.CreateOrderRequest;
import com.huobi.quantification.response.spot.HuobiBatchCancelOpenOrdersResponse;
import com.huobi.quantification.response.spot.HuobiSpotOrderResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.HuobiSpotOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
@Service
@Transactional
public class HuobiSpotOrderServiceImpl implements HuobiSpotOrderService {

    @Autowired
    private HttpService httpService;


    /**
     * 下单
     *
     * @return
     */
    public Long placeHuobiOrder(HuobiTradeOrderDto orderDto) {
        // todo
        return System.currentTimeMillis();
        /*HuobiSpotOrderResponse response = placeOkOrderByAPI(orderDto);
        return response.getOrderId();*/
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
        HuobiSpotOrderResponse response = JSON.parseObject(body, HuobiSpotOrderResponse.class);
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            return response;
        }
        throw new APIException(body);
    }

    @Override
    public void cancelAllOrder(HuobiSpotCancelAllOrderBO cancelAllOrderBO) {
        Map<String, Object> param = new HashMap<>();
        param.put("account-id", cancelAllOrderBO.getAccountId());
        param.put("symbol", cancelAllOrderBO.getBaseCoin() + cancelAllOrderBO.getQuoteCoin());
        String body = httpService.doHuobiSpotPost(cancelAllOrderBO.getAccountId(), HttpConstant.HUOBI_BATCHCANCELOPENORDERS, param);
        HuobiBatchCancelOpenOrdersResponse response = JSON.parseObject(body, HuobiBatchCancelOpenOrdersResponse.class);
        String status = response.getStatus();
        if ("ok".equalsIgnoreCase(status)) {
            int failedCount = response.getData().getFailedCount();
            if (failedCount == 0) {
                return;
            }
        }
        throw new APIException(body);
    }


}
