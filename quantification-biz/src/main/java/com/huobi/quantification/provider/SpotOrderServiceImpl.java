package com.huobi.quantification.provider;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.exception.HttpRequestException;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.dto.SpotOrderCancelReqDto.Orders;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OrderStatusEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.response.spot.HuobiBatchCancelOpenOrdersResponse;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.HuobiOrderService;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private HuobiOrderService huobiOrderService;

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
            Long orderId = huobiOrderService.placeHuobiOrder(orderDto);
            quanOrder.setOrderSourceId(orderId);
            respDto.setExOrderId(orderId);
            respDto.setLinkOrderId(reqDto.getLinkOrderId());
            respDto.setInnerOrderId(quanOrder.getOrderInnerId());
            serviceResult.setData(respDto);
            logger.info("同步下单成功，订单号:{}", orderId);
        } else {
            Future<Long> orderIdFuture = AsyncUtils.submit(() -> huobiOrderService.placeHuobiOrder(orderDto));
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
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("account-id", reqDto.getAccountId());
            param.put("symbol", reqDto.getBaseCoin() + reqDto.getQuoteCoin());
            String body = httpService.doHuobiSpotPost(reqDto.getAccountId(), HttpConstant.HUOBI_BATCHCANCELOPENORDERS, param);
            HuobiBatchCancelOpenOrdersResponse response = JSON.parseObject(body, HuobiBatchCancelOpenOrdersResponse.class);
            String status = response.getStatus();
            if ("ok".equalsIgnoreCase(status)) {
                int failedCount = response.getData().getFailedCount();
                if (failedCount == 0) {
                    return ServiceResult.buildSuccessResult(null);
                }
            }
            return ServiceResult.buildErrorResult(ServiceErrorEnum.HTTP_REQUEST_ERROR);
        } catch (HttpRequestException e) {
            return ServiceResult.buildErrorResult(ServiceErrorEnum.HTTP_REQUEST_ERROR);
        }
    }


}
