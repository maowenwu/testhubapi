package com.huobi.quantification.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.constant.OkRestErrorCode;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.constant.OrderStatusTable;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.request.future.FutureOkCancelOrderRequest;
import com.huobi.quantification.request.future.FutureOkOrderRequest;
import com.huobi.quantification.response.future.OKFutureCancelOrderResponse;
import com.huobi.quantification.response.future.OKFuturePlaceOrderResponse;
import com.huobi.quantification.response.future.OKFutureQueryOrderResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.OkOrderService;
import com.huobi.quantification.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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


    public void updateOkOrderInfo(Long accountId, String symbol, String contractType) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[OkOrder][symbol={},contractType={}]任务开始", symbol, contractType);
        // 查找出订单状态不为已完成、撤单的订单
        List<Integer> status = new ArrayList<>();
        status.add(0);//0等待成交
        status.add(1);//1部分成交
        status.add(4);//4撤单处理中
        // 扫描未完结的订单
        List<Long> orderIds = quanOrderFutureMapper.selectOrderIdBySourceStatus(ExchangeEnum.OKEX.getExId(), accountId, status);
        if (CollectionUtils.isNotEmpty(orderIds)) {
            List<QuanOrderFuture> orderFutures = queryOkOrdersInfoByAPI(accountId, symbol, contractType, orderIds);
            for (QuanOrderFuture orderFuture : orderFutures) {
                quanOrderFutureMapper.updateByExIdAccountIdExOrderId(orderFuture);
            }
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


    private List<QuanOrderFuture> parseAndSaveOrderInfo(Long accountId, String body) {
        OKFutureQueryOrderResponse response = JSON.parseObject(body, OKFutureQueryOrderResponse.class);
        List<QuanOrderFuture> list = new ArrayList<>();
        if (response.isResult()) {
            for (OKFutureQueryOrderResponse.OrdersBean order : response.getOrders()) {
                QuanOrderFuture orderFuture = parseOkFutureOrder(order);
                orderFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
                orderFuture.setAccountId(accountId);
                list.add(orderFuture);
            }
        }
        return list;
    }

    private QuanOrderFuture parseOkFutureOrder(OKFutureQueryOrderResponse.OrdersBean order) {
        QuanOrderFuture orderFuture = new QuanOrderFuture();
        // 交易所订单id
        orderFuture.setExOrderId(order.getOrderId());
        // 下单前已填充
        //orderFuture.setLinkOrderId();
        // 下单前已填充
        //orderFuture.setCreateDate();
        orderFuture.setUpdateDate(new Date());
        int status = order.getStatus();
        // 使用程序判断是否是部分成交已撤单状态
        if (status == -1 && !BigDecimalUtils.equals(order.getDealAmount(), BigDecimal.ZERO)) {
            // 如果状态为已撤单并且成交量不等于0，那么可以判断为部分成交已撤单
            status = 5;
        }
        orderFuture.setStatus(OrderStatusTable.OkOrderStatus.getOrderStatus(status).getOrderStatus());
        String symbol = order.getSymbol();
        String[] split = symbol.split("_");
        orderFuture.setBaseCoin(split[0]);
        orderFuture.setQuoteCoin(split[1]);
        // 下单前已填充
        //orderFuture.setContractType(null);
        // 下单前已填充
        //orderFuture.setContractCode();
        orderFuture.setSide(getOkSide(order.getType()).getSideType());
        orderFuture.setOffset(getOkOffset(order.getType()).getOffset());
        // 杠杆率
        orderFuture.setLever(order.getLeverRate());
        orderFuture.setOrderType("limit");
        // 订单价格
        orderFuture.setOrderPrice(order.getPrice());
        // 成交均价
        orderFuture.setDealPrice(order.getPriceAvg());
        // 委托量
        orderFuture.setOrderQty(order.getAmount());
        // 成交量
        orderFuture.setDealQty(order.getDealAmount());
        // 未成交量
        orderFuture.setRemainingQty(order.getAmount().subtract(order.getDealAmount()));
        // 冻结保证金
        // todo 还未确认
        //orderFuture.setMarginFrozen();

        orderFuture.setContractCode(order.getContractName());
        orderFuture.setFees(order.getFee());
        orderFuture.setSourceStatus(order.getStatus());
        return orderFuture;
    }

    private SideEnum getOkSide(int type) {
        if (type == 1 || type == 3) {
            return SideEnum.BUY;
        } else {
            return SideEnum.SELL;
        }
    }


    private OffsetEnum getOkOffset(int type) {
        if (type == 1 || type == 2) {
            return OffsetEnum.LONG;
        } else {
            return OffsetEnum.SHORT;
        }
    }

    @Override
    public Long placeOkOrder(FutureOkOrderRequest order) {
        OKFuturePlaceOrderResponse response = placeOkOrderByAPI(order);
        if (response.isResult()) {
            return response.getOrderId();
        } else {
            throw new RuntimeException(OkRestErrorCode.findErrorMessageByCode(response.getErrorCode()));
        }
    }

    private OKFuturePlaceOrderResponse placeOkOrderByAPI(FutureOkOrderRequest order) {
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
        return JSON.parseObject(body, OKFuturePlaceOrderResponse.class);
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


}
