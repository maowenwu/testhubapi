package com.huobi.quantification.service.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.huobi.quantification.execeptions.APIException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.exception.HttpRequestException;
import com.huobi.quantification.constant.OrderStatusTable;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.enums.OrderStatusEnum;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.bo.HuobiFutureOrderBO;
import com.huobi.quantification.response.future.FutureHuobiOrderCancelResponse;
import com.huobi.quantification.response.future.FutureHuobiOrderInfoResponse;
import com.huobi.quantification.response.future.FutureHuobiOrderPageInfoResponse;
import com.huobi.quantification.response.future.FutureHuobiOrderResponse;
import com.huobi.quantification.response.future.HuobiFutureOrderCancelAllResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.HuobiFutureOrderService;

@Service
@Transactional
public class HuobiFutureOrderServiceImpl implements HuobiFutureOrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpService httpService;

    @Autowired
    private QuanOrderFutureMapper quanOrderFutureMapper;

    @Override
    public Long placeOrder(HuobiFutureOrderBO order) {
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
        String body = httpService.doHuobiFuturePostJson(order.getAccountId(), HttpConstant.HUOBI_FUTURE_ORDER, params);
        FutureHuobiOrderResponse response = JSON.parseObject(body, FutureHuobiOrderResponse.class);
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            return response.getData().getOrderId();
        } else {
            throw new APIException(body);
        }
    }

    @Override
    public Long cancelOrder(Long accountId, Long orderId, Long clientOrderId) {
        Map<String, String> params = new HashMap<>();
        if (orderId != null) {
            params.put("order_id", orderId.toString());
        }
        if (clientOrderId != null) {
            params.put("client_order_id", clientOrderId.toString());
        }
        String body = httpService.doHuobiFuturePostJson(accountId, HttpConstant.HUOBI_FUTURE_ORDER_CANCEL, params);
        FutureHuobiOrderCancelResponse response = JSON.parseObject(body, FutureHuobiOrderCancelResponse.class);
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            return Long.valueOf(response.getData().getSuccesses());
        }
        throw new APIException(body);
    }

    @Override
    public List<Long> updateHuobiOrderInfo(Long accountId, String contractCode) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiOrder][symbol={},contractType={}]任务开始");
        // 查找出订单状态不为已完成、撤单的订单
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.SUBMITTED.getOrderStatus());
        statusList.add(OrderStatusEnum.PARTIAL_FILLED.getOrderStatus());
        statusList.add(OrderStatusEnum.CANCELING.getOrderStatus());
        List<Long> orderIds = quanOrderFutureMapper.selectExOrderIdByStatus(ExchangeEnum.HUOBI_FUTURE.getExId(), accountId, contractCode, statusList);

        List<Long> dealOrder = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderIds)) {
            List<List<Long>> lists = Lists.partition(orderIds, 20);
            lists.forEach(e -> {
                List<QuanOrderFuture> orderFutures = queryHuobiOrderInfoByAPI(accountId, e);
                for (QuanOrderFuture orderFuture : orderFutures) {
                    quanOrderFutureMapper.updateByExIdAccountIdExOrderId(orderFuture);
                }
                dealOrder.addAll(orderFutures.stream().map(o -> o.getExOrderId()).collect(Collectors.toList()));
            });
        }
        orderIds.removeAll(dealOrder);
        logger.info("[HuobiOrder][symbol={},contractType={}]任务结束，耗时：" + started);
        return orderIds;
    }

    private List<QuanOrderFuture> queryHuobiOrderInfoByAPI(Long accountId, List<Long> orderIds) {
        Map<String, String> params = new HashMap<>();
        String orderId = String.join(",", orderIds.stream().map(e -> e.toString()).collect(Collectors.toList()));
        params.put("order_id", orderId);
        String body = null;
        try {
            body = httpService.doHuobiFuturePostJson(accountId, HttpConstant.HUOBI_FUTURE_ORDER_INFO, params);
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
                orderFuture.setUpdateDate(new Date());
                orderFuture.setStatus(
                        OrderStatusTable.HuobiFutureOrderStatus.getOrderStatus(e.getStatus()).getOrderStatus());
                orderFuture.setBaseCoin(e.getSymbol().toLowerCase());
                orderFuture.setQuoteCoin("usd");
                if ("buy".equalsIgnoreCase(e.getDirection())) {
                    orderFuture.setSide(SideEnum.BUY.getSideType());
                } else {
                    orderFuture.setSide(SideEnum.SELL.getSideType());
                }
                if ("open".equalsIgnoreCase(e.getOffset())) {
                    orderFuture.setOffset(OffsetEnum.OPEN.getOffset());
                } else {
                    orderFuture.setOffset(OffsetEnum.CLOSE.getOffset());
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
    public void cancelAllOrder(Long accountId, String symbol) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        String body = httpService.doHuobiFuturePostJson(accountId, HttpConstant.HUOBI_FUTURE_ORDER_CANCEL_ALL, params);
        HuobiFutureOrderCancelAllResponse response = JSON.parseObject(body, HuobiFutureOrderCancelAllResponse.class);
        // 1051：没有可撤订单可以算成功的
        if ("ok".equalsIgnoreCase(response.getStatus()) || Integer.valueOf(1051).equals(response.getErrCode())) {
            return;
        }
        throw new APIException(body);
    }

    @Override
    public void replenishOrder(Long accountId, String symbol) {
        Integer pageSize = 50;
        Integer pageIndex = 1;
        List<QuanOrderFuture> orderList = new ArrayList<>();
        while (true) {
            Map<String, String> params = new HashMap<>();
            params.put("symbol", symbol.toUpperCase());
            params.put("page_size", String.valueOf(pageSize));
            params.put("page_index", String.valueOf(pageIndex));
            String body = httpService.doHuobiFuturePostJson(accountId, HttpConstant.HUOBI_CONTRACE_OPENORDERS, params);
            FutureHuobiOrderPageInfoResponse response = JSON.parseObject(body, FutureHuobiOrderPageInfoResponse.class);
            if ("ok".equalsIgnoreCase(response.getStatus())) {
                List<QuanOrderFuture> list = parseRespToList(response);
                orderList.addAll(list);
                pageIndex++;
                if (response.getData().getOrders().isEmpty() || response.getData().getOrders().size() < pageSize) {
                    break;
                }
            } else {
                throw new APIException(body);
            }

        }
        orderList.stream().forEach(e -> {
            e.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
            e.setAccountId(accountId);
        });
        if (CollectionUtils.isNotEmpty(orderList)) {
            int count = quanOrderFutureMapper.insertBatch(orderList);
            logger.info("本次补单量：{}", count);
        }
    }

    /**
     * 将分页查询的返回值解析为对应的实体
     *
     * @param resp
     * @return
     */
    private List<QuanOrderFuture> parseRespToList(FutureHuobiOrderPageInfoResponse resp) {
        List<QuanOrderFuture> list = new ArrayList<>();
        resp.getData().getOrders().forEach(e -> {
            QuanOrderFuture orderFuture = new QuanOrderFuture();
            // 交易所订单id
            orderFuture.setExOrderId(e.getOrderId());
            orderFuture.setBaseCoin(e.getSymbol().toLowerCase());
            orderFuture.setQuoteCoin("usd");
            orderFuture.setContractType(e.getContractType());
            orderFuture.setContractCode(e.getContractCode());
            orderFuture.setStatus(OrderStatusTable.HuobiFutureOrderStatus.getOrderStatus(e.getStatus()).getOrderStatus());
            orderFuture.setSourceStatus(e.getStatus());
            if ("buy".equalsIgnoreCase(e.getDirection())) {
                orderFuture.setSide(SideEnum.BUY.getSideType());
            } else {
                orderFuture.setSide(SideEnum.SELL.getSideType());
            }
            if ("open".equalsIgnoreCase(e.getOffset())) {
                orderFuture.setOffset(OffsetEnum.OPEN.getOffset());
            } else {
                orderFuture.setOffset(OffsetEnum.CLOSE.getOffset());
            }
            // 杠杆率
            orderFuture.setLever(e.getLeverRate());
            orderFuture.setOrderType(e.getOrderPriceType());
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
            orderFuture.setOrderSource("replenish");
            orderFuture.setCreateDate(new Date());
            orderFuture.setUpdateDate(new Date());
            list.add(orderFuture);
        });
        return list;
    }
}
