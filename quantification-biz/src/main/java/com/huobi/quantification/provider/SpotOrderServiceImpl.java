package com.huobi.quantification.provider;

import com.google.common.base.Throwables;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.bo.HuobiSpotCancelAllOrderBO;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.exception.ApiException;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.dto.HuobiTradeOrderDto;
import com.huobi.quantification.dto.SpotCancleAllOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OrderStatusEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.HuobiSpotOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SpotOrderServiceImpl implements SpotOrderService {

    @Autowired
    private QuanOrderMapper quanOrderMapper;

    @Autowired
    private HuobiSpotOrderService huobiSpotOrderService;

    @Override
    public ServiceResult<SpotPlaceOrderRespDto> placeOrder(SpotPlaceOrderReqDto reqDto) {
        try {
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

            Long orderId = huobiSpotOrderService.placeHuobiOrder(orderDto);
            quanOrder.setOrderSourceId(orderId);
            quanOrder.setOrderState(OrderStatusEnum.SUBMITTED.getOrderStatus());
            quanOrderMapper.updateByPrimaryKey(quanOrder);

            SpotPlaceOrderRespDto respDto = new SpotPlaceOrderRespDto();
            respDto.setExOrderId(orderId);
            respDto.setLinkOrderId(reqDto.getLinkOrderId());
            respDto.setInnerOrderId(quanOrder.getOrderInnerId());
            return ServiceResult.buildSuccessResult(respDto);
        } catch (RuntimeException e) {
            if (e instanceof ApiException) {
                return ServiceResult.buildAPIErrorResult(e.getMessage());
            } else {
                return ServiceResult.buildSystemErrorResult(Throwables.getStackTraceAsString(e));
            }
        }
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
