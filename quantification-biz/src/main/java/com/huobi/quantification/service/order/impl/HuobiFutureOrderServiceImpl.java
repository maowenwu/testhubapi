package com.huobi.quantification.service.order.impl;


import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.exception.HttpRequestException;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.constant.OrderStatusTable;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.enums.OrderStatusEnum;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.request.future.FutureHuobiOrderRequest;
import com.huobi.quantification.response.future.FutureHuobiOrderCancelResponse;
import com.huobi.quantification.response.future.FutureHuobiOrderInfoResponse;
import com.huobi.quantification.response.future.FutureHuobiOrderResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.HuobiFutureOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class HuobiFutureOrderServiceImpl implements HuobiFutureOrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpService httpService;

    @Autowired
    private QuanOrderFutureMapper quanOrderFutureMapper;

    @Override
    public Long placeOrder(FutureHuobiOrderRequest order) {
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotEmpty(order.getSymbol())) {
            params.put("symbol", order.getSymbol());
        }
        if (StringUtils.isNotEmpty(order.getContractType())) {
            params.put("contract_type", order.getContractType());
        }
        if (StringUtils.isNotEmpty(order.getContractCode())) {
            params.put("contract_code", order.getContractCode());
        }
        params.put("price", order.getPrice());
        params.put("volume", order.getVolume());
        params.put("direction", order.getDirection());
        params.put("offset", order.getOffset());
        params.put("lever_rate", order.getLeverRate());
        params.put("order_price_type", order.getOrderPriceType());
        if (StringUtils.isNotEmpty(order.getClientOrderId())) {
            params.put("client_order_id", order.getClientOrderId());
        }
        params.put("userId", "156138");
        String body = httpService.doPostJson(HttpConstant.HUOBI_FUTURE_ORDER, params);
        FutureHuobiOrderResponse response = JSON.parseObject(body, FutureHuobiOrderResponse.class);
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            return response.getData().getOrderId();
        } else {
            logger.error("火币期货下单失败，order：{}，返回结果：{}", JSON.toJSONString(order), body);
            return null;
        }
    }

    @Override
    public Long cancelOrder(Long orderId, Long clientOrderId) {
        Map<String, String> params = new HashMap<>();
        if (orderId != null) {
            params.put("order_id", orderId.toString());
        }
        if (clientOrderId != null) {
            params.put("client_order_id", clientOrderId.toString());
        }
        params.put("userId", "156138");
        String body = null;
        try {
            body = httpService.doPostJson(HttpConstant.HUOBI_FUTURE_ORDER_CANCEL, params);
        } catch (HttpRequestException e) {
            logger.error("取消订单http执行异常", e);
            return null;
        }
        FutureHuobiOrderCancelResponse response = JSON.parseObject(body, FutureHuobiOrderCancelResponse.class);
        if (response.isSuccess() && StringUtils.isNotEmpty(response.getData().getSuccesses())) {
            Long cancelOrderId = Long.valueOf(response.getData().getSuccesses());
            return cancelOrderId;
        }
        return null;
    }

    @Override
    public boolean updateHuobiOrderInfo(Long accountId) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiOrder][symbol={},contractType={}]任务开始");
        // 查找出订单状态不为已完成、撤单的订单
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.SUBMITTED.getOrderStatus());
        statusList.add(OrderStatusEnum.PARTIAL_FILLED.getOrderStatus());
        statusList.add(OrderStatusEnum.CANCELING.getOrderStatus());
        List<Long> orderIds = quanOrderFutureMapper.selectExOrderIdByStatus(ExchangeEnum.HUOBI_FUTURE.getExId(), accountId, statusList);
        // 用于标记是否更新完成，当待更新订单量与实际更新订单量相同时才算更新完成
        boolean updateSuccess;
        List<Long> dealOrder = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderIds)) {
            List<List<Long>> lists = Lists.partition(orderIds, 20);
            lists.forEach(e -> {
                List<QuanOrderFuture> orderFutures = queryHuobiOrderInfoByAPI(accountId, e);
                dealOrder.addAll(orderFutures.stream().map(o -> o.getExOrderId()).collect(Collectors.toList()));
                for (QuanOrderFuture orderFuture : orderFutures) {
                    quanOrderFutureMapper.updateByExIdAccountIdExOrderId(orderFuture);
                }
            });
        }
        logger.info("[HuobiOrder][symbol={},contractType={}]任务结束，耗时：" + started);
        updateSuccess = orderIds.size() == dealOrder.size();
        if (!updateSuccess) {
            int n = orderIds.size();
            orderIds.removeAll(dealOrder);
            logger.error("订单信息更新失败，待更新订单量：{}，实际更新订单量：{}，更新有问题的订单号：{}", n, dealOrder.size(), orderIds);
        }
        return updateSuccess;
    }

    private List<QuanOrderFuture> queryHuobiOrderInfoByAPI(Long accountId, List<Long> orderIds) {
        Map<String, String> params = new HashMap<>();
        String orderId = String.join(",", orderIds.stream().map(e -> e.toString()).collect(Collectors.toList()));
        params.put("order_id", orderId);
        params.put("userId", "156138");
        String body = null;
        try {
            body = httpService.doPostJson(HttpConstant.HUOBI_FUTURE_ORDER_INFO, params);
        } catch (HttpRequestException e) {
            logger.error("批量查询订单信息失败，订单id：{}", orderId, e);
            throw new RuntimeException("批量查询订单信息失败");
        }
        FutureHuobiOrderInfoResponse response = JSON.parseObject(body, FutureHuobiOrderInfoResponse.class);
        if (response.getStatus().equalsIgnoreCase("ok")) {
            List<QuanOrderFuture> list = new ArrayList<>();
            response.getData().forEach(e -> {
                QuanOrderFuture orderFuture = new QuanOrderFuture();
                orderFuture.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
                orderFuture.setAccountId(accountId);
                // 交易所订单id
                orderFuture.setExOrderId(e.getOrderId());
                // 下单前已填充
                //orderFuture.setLinkOrderId();
                // 下单前已填充
                //orderFuture.setCreateDate();
                orderFuture.setUpdateDate(new Date());
                orderFuture.setStatus(OrderStatusTable.HuobiFutureOrderStatus.getOrderStatus(e.getStatus()).getOrderStatus());
                orderFuture.setBaseCoin(e.getSymbol().toLowerCase());
                orderFuture.setQuoteCoin("usd");
                // 下单前已填充
                //orderFuture.setContractType(null);
                // 下单前已填充
                //orderFuture.setContractCode();
                if ("buy".equalsIgnoreCase(e.getDirection())) {
                    orderFuture.setSide(SideEnum.BUY.getSideType());
                } else {
                    orderFuture.setSide(SideEnum.SELL.getSideType());
                }
                if ("open".equalsIgnoreCase(e.getOffset())) {
                    orderFuture.setOffset(OffsetEnum.LONG.getOffset());
                } else {
                    orderFuture.setOffset(OffsetEnum.SHORT.getOffset());
                }
                // 杠杆率
                orderFuture.setLever(e.getLeverRate());
                orderFuture.setOrderType("limit");
                // 订单价格
                orderFuture.setOrderPrice(e.getPrice());
                // 成交均价
                orderFuture.setDealPrice(e.getTradeAvgPrice());
                // 委托量
                orderFuture.setOrderQty(e.getVolume());
                // 成交量
                orderFuture.setDealQty(e.getTradeVolume());
                // 未成交量
                orderFuture.setRemainingQty(e.getVolume().subtract(e.getTradeVolume()));
                // 冻结保证金
                orderFuture.setMarginFrozen(e.getMarginFrozen());
                orderFuture.setContractCode(e.getContractCode());
                orderFuture.setFees(e.getFee());
                orderFuture.setSourceStatus(e.getStatus());
                list.add(orderFuture);
            });
            return list;
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    public void cancelAllOrder(String symbol) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("userId", "156138");
        String body = httpService.doPostJson(HttpConstant.HUOBI_FUTURE_ORDER_CANCEL_ALL, params);
        System.out.println(body);
    }
}