package com.huobi.quantification.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.constant.OkRestErrorCode;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.dto.OkCancelOrderDto;
import com.huobi.quantification.dto.OkTradeOrderDto;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.*;
import com.huobi.quantification.facade.OkOrderServiceFacade;
import com.huobi.quantification.service.account.AccountService;
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
public class OrderServiceImpl implements OrderService, OkOrderServiceFacade {

    @Autowired
    private HttpService httpService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private QuanOrderFutureMapper quanOrderFutureMapper;

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

    @Override
    public void storeOkFutureOrder() {
        List<Long> accountList = accountService.findAccountFutureByExchangeId(ExchangeEnum.OKEX.getExId());
        for (Long account : accountList) {
            updateAllOkOrderInfo(account, OkSymbolEnum.BTC_USD.getSymbol(), OkContractType.THIS_WEEK);
            /*updateAllOkOrderInfo(account, OkSymbolEnum.BTC_USD.getSymbol(), OkContractType.NEXT_WEEK);
            updateAllOkOrderInfo(account, OkSymbolEnum.BTC_USD.getSymbol(), OkContractType.QUARTER);


            updateAllOkOrderInfo(account, OkSymbolEnum.LTC_USD.getSymbol(), OkContractType.THIS_WEEK);
            updateAllOkOrderInfo(account, OkSymbolEnum.LTC_USD.getSymbol(), OkContractType.NEXT_WEEK);
            updateAllOkOrderInfo(account, OkSymbolEnum.LTC_USD.getSymbol(), OkContractType.QUARTER);

            updateAllOkOrderInfo(account, OkSymbolEnum.ETH_USD.getSymbol(), OkContractType.THIS_WEEK);
            updateAllOkOrderInfo(account, OkSymbolEnum.ETH_USD.getSymbol(), OkContractType.NEXT_WEEK);
            updateAllOkOrderInfo(account, OkSymbolEnum.ETH_USD.getSymbol(), OkContractType.QUARTER);

            updateAllOkOrderInfo(account, OkSymbolEnum.ETC_USD.getSymbol(), OkContractType.THIS_WEEK);
            updateAllOkOrderInfo(account, OkSymbolEnum.ETC_USD.getSymbol(), OkContractType.NEXT_WEEK);
            updateAllOkOrderInfo(account, OkSymbolEnum.ETC_USD.getSymbol(), OkContractType.QUARTER);

            updateAllOkOrderInfo(account, OkSymbolEnum.BCH_USD.getSymbol(), OkContractType.THIS_WEEK);
            updateAllOkOrderInfo(account, OkSymbolEnum.BCH_USD.getSymbol(), OkContractType.NEXT_WEEK);
            updateAllOkOrderInfo(account, OkSymbolEnum.BCH_USD.getSymbol(), OkContractType.QUARTER);*/
        }
    }

    private void updateAllOkOrderInfo(Long accountId, String symbol, OkContractType contractType) {
        List<QuanOrderFuture> orderFutures = queryAllOkOrderInfo(accountId, symbol, contractType);
        for (QuanOrderFuture orderFuture : orderFutures) {
            quanOrderFutureMapper.insertOrUpdate(orderFuture);
        }

    }

    private List<QuanOrderFuture> queryAllOkOrderInfo(Long accountId, String symbol, OkContractType contractType) {
        List<QuanOrderFuture> list = new ArrayList<>();
        List<QuanOrderFuture> finishOrder = queryAllOkOrderInfoByStatus(accountId, symbol, contractType, OrderStatus.FINISH);
        List<QuanOrderFuture> unfinishOrder = queryAllOkOrderInfoByStatus(accountId, symbol, contractType, OrderStatus.UNFINISH);
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
    private List<QuanOrderFuture> queryAllOkOrderInfoByStatus(Long accountId, String symbol, OkContractType contractType, OrderStatus status) {
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


    private List<QuanOrderFuture> queryOkOrderInfoByAPI(Long accountId, String symbol, OkContractType contractType, OrderStatus status, String orderId, int currentPage, int pageLength) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType.getType());
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
                orderFuture.setOrderAccountId(accountId);
                orderFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
                list.add(orderFuture);
            }
        }
        return list;
    }

    private QuanOrderFuture parseOkFutureOrder(JSONObject order) {
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
        orderFuture.setUpdateDate(new Date());
        return orderFuture;
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
    public ServiceResult placeOkOrder(OkTradeOrderDto order) {
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
        JSONObject jsonObject = JSON.parseObject(body);
        ServiceResult<Long> result = new ServiceResult<>();
        if (jsonObject.getBoolean("result")) {
            Long orderId = jsonObject.getLong("order_id");
            QuanOrderFuture orderFuture = new QuanOrderFuture();
            orderFuture.setStrategyName(order.getStrategyName());
            orderFuture.setStrategyVersion(order.getStrategyVersion());
            orderFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
            orderFuture.setOrderAccountId(order.getAccountId());
            orderFuture.setOrderSourceId(orderId);
            orderFuture.setUpdateDate(new Date());
            quanOrderFutureMapper.insert(orderFuture);

            result.setCode(ServiceResultEnum.SUCCESS.getCode());
            result.setMessage(ServiceResultEnum.SUCCESS.getMessage());
            result.setData(orderId);
        } else {
            Integer errorCode = jsonObject.getInteger("error_code");
            result.setCode(errorCode);
            result.setMessage(OkRestErrorCode.findErrorMessageByCode(errorCode));
        }
        return result;
    }

    @Override
    public ServiceResult placeOkOrders() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        params.put("orders_data", "[{price:6050,amount:0.0001,type:1,match_price:0}]");
        params.put("lever_rate", "10");
        //String result = httpService.doOkSignedPost(HttpConstant.OK_BATCH_TRADE, params);
        return null;
    }

    @Override
    public ServiceResult<String> cancelOkOrder(OkCancelOrderDto cancelOrderDto) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", cancelOrderDto.getSymbol());
        params.put("contract_type", cancelOrderDto.getContractType());
        params.put("order_id", cancelOrderDto.getOrderId());
        String body = httpService.doOkSignedPost(cancelOrderDto.getAccountId(), HttpConstant.OK_CANCEL, params);
        JSONObject jsonObject = JSON.parseObject(body);
        ServiceResult<String> result = new ServiceResult<>();
        if (jsonObject.getBoolean("result")) {
            result.setCode(ServiceResultEnum.SUCCESS.getCode());
            result.setMessage(ServiceResultEnum.SUCCESS.getMessage());
            result.setData(jsonObject.getString("order_id"));
        } else {
            Integer errorCode = jsonObject.getInteger("error_code");
            result.setCode(errorCode);
            result.setMessage(OkRestErrorCode.findErrorMessageByCode(errorCode));
        }
        return result;
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
