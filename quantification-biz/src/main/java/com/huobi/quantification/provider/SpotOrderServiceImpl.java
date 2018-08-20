package com.huobi.quantification.provider;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.bo.HuobiSpotCancelAllOrderBO;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.exception.ApiException;
import com.huobi.quantification.common.exception.HttpRequestException;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OrderStatusEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.response.spot.HuobiBatchCancelOpenOrdersResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.HuobiSpotOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class SpotOrderServiceImpl implements SpotOrderService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private QuanOrderMapper quanOrderMapper;

    @Autowired
    private HuobiSpotOrderService huobiSpotOrderService;

    @Autowired
    private HttpService httpService;


    @Override
    public ServiceResult<SpotPlaceOrderRespDto> placeOrder(SpotPlaceOrderReqDto reqDto) {
        HuobiTradeOrderDto orderDto = new HuobiTradeOrderDto();
        QuanOrder quanOrder = new QuanOrder();
        orderDto.setAccountId(reqDto.getAccountId());
        //验证参数,orderDto属性赋值
        if ("buy".equals(reqDto.getSide()) || "sell".equals(reqDto.getSide())) {
            if (reqDto.getOrderType().contains("market")) {
                orderDto.setAmount(reqDto.getCashAmount());
            } else {
                orderDto.setPrice(reqDto.getPrice());
                orderDto.setAmount(reqDto.getQuantity());
            }
        } else {
            throw new RuntimeException(ServiceErrorEnum.PARAM_ERROR.getMessage());
        }
        orderDto.setSource("api");
        String symbol = getSymbol(reqDto.getExchangeId(), reqDto.getBaseCoin(), reqDto.getQuoteCoin());
        orderDto.setSymbol(symbol);
        orderDto.setType(reqDto.getSide() + "-" + reqDto.getOrderType());
        //quanOrder属性赋值，并持久化保存
        quanOrder.setExchangeId(reqDto.getExchangeId());
        quanOrder.setOrderAccountId(reqDto.getAccountId());
        quanOrder.setOrderAmount(orderDto.getAmount());
        quanOrder.setOrderPrice(orderDto.getPrice());
        quanOrder.setOrderCreatedAt(new Date());
        quanOrder.setOrderSource(orderDto.getSource());
        quanOrder.setOrderState(OrderStatusEnum.PRE_SUBMITTED.getOrderStatus());
        quanOrder.setOrderSymbol(symbol);
        quanOrder.setOrderType(orderDto.getType());
        quanOrderMapper.insertAndGetId(quanOrder);
        //下单，并更新数据库
        ServiceResult<SpotPlaceOrderRespDto> serviceResult = new ServiceResult<>();
        serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
        serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
        SpotPlaceOrderRespDto respDto = new SpotPlaceOrderRespDto();
        if (reqDto.isSync()) {
            Long orderId = huobiSpotOrderService.placeHuobiOrder(orderDto);
            quanOrder.setOrderSourceId(orderId);
            respDto.setExOrderId(orderId);
            respDto.setLinkOrderId(reqDto.getLinkOrderId());
            respDto.setInnerOrderId(quanOrder.getOrderInnerId());
            serviceResult.setData(respDto);
            logger.info("同步下单成功，订单号:{}", orderId);
        } else {
            Future<Long> orderIdFuture = AsyncUtils.submit(() -> huobiSpotOrderService.placeHuobiOrder(orderDto));
            try {
                quanOrder.setOrderSourceId(orderIdFuture.get());
                logger.info("异步下单成功，订单号:{}", orderIdFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
                serviceResult.setMessage(ServiceErrorEnum.EXECUTION_ERROR.getMessage());
                serviceResult.setCode(ServiceErrorEnum.EXECUTION_ERROR.getCode());
            }
        }
        quanOrder.setOrderState(OrderStatusEnum.SUBMITTED.getOrderStatus());
        quanOrderMapper.updateByPrimaryKey(quanOrder);
        return serviceResult;
    }

    private String getSymbol(int exchangeId, String baseCoin, String quoteCoin) {
        if (exchangeId == ExchangeEnum.HUOBI.getExId()) {
            return baseCoin.toLowerCase() + quoteCoin.toLowerCase();
        } else {
            throw new UnsupportedOperationException("交易所" + exchangeId + ",还不支持");
        }
    }

    @Override
    public ServiceResult cancelAllOrder(SpotCancleAllOrderReqDto reqDto) {
        HuobiSpotCancelAllOrderBO cancelAllOrderBO = new HuobiSpotCancelAllOrderBO();
        BeanUtils.copyProperties(reqDto, cancelAllOrderBO);
        try {
            huobiSpotOrderService.cancelAllOrder(cancelAllOrderBO);
            return ServiceResult.buildSuccessResult(null);
        } catch (Throwable e) {
            if (e instanceof ApiException) {
                return ServiceResult.buildAPIErrorResult(e.getMessage());
            } else {
                return ServiceResult.buildSystemErrorResult(Throwables.getStackTraceAsString(e));
            }
        }
    }


}
