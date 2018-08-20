package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.QuanContractCode;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OrderStatusEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.request.future.FutureHuobiOrderRequest;
import com.huobi.quantification.request.future.FutureOkCancelOrderRequest;
import com.huobi.quantification.request.future.FutureOkOrderRequest;
import com.huobi.quantification.service.contract.ContractService;
import com.huobi.quantification.service.order.HuobiFutureOrderService;
import com.huobi.quantification.service.order.OkOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FutureOrderServiceImpl implements FutureOrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OkOrderService okOrderService;

    @Autowired
    private HuobiFutureOrderService huobiFutureOrderService;

    @Autowired
    private QuanOrderFutureMapper quanOrderFutureMapper;

    @Autowired
    private ContractService contractService;

    @Override
    public ServiceResult<FuturePlaceOrderRespDto> placeOrder(FuturePlaceOrderReqDto reqDto) {
        if (reqDto.getExchangeId() == ExchangeEnum.OKEX.getExId()) {
            return placeOkOrder(reqDto);
        } else if (reqDto.getExchangeId() == ExchangeEnum.HUOBI_FUTURE.getExId()) {
            return placeHuobiFutureOrder(reqDto);
        }
        return null;
    }

    private ServiceResult<FuturePlaceOrderRespDto> placeHuobiFutureOrder(FuturePlaceOrderReqDto reqDto) {
        try {
            // 插入order表生成内部订单id
            QuanOrderFuture orderFuture = new QuanOrderFuture();
            orderFuture.setStrategyName(reqDto.getStrategyName());
            orderFuture.setInstanceId(reqDto.getInstanceId());
            orderFuture.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
            orderFuture.setAccountId(reqDto.getAccountId());
            orderFuture.setLinkOrderId(reqDto.getLinkOrderId());
            if (StringUtils.isEmpty(reqDto.getContractCode())) {
                // 通过symbol+ContractType找ContractCode
                QuanContractCode quanContractCode = contractService.getContractCode(reqDto.getExchangeId(), reqDto.getBaseCoin(), reqDto.getContractType());
                orderFuture.setBaseCoin(reqDto.getBaseCoin());
                orderFuture.setQuoteCoin(reqDto.getQuoteCoin());
                orderFuture.setContractType(reqDto.getContractType());
                orderFuture.setContractCode(quanContractCode.getContractCode());
            } else {
                // 通过ContractCode找symbol+ContractType
                QuanContractCode quanContractCode = contractService.getContractCode(reqDto.getExchangeId(), reqDto.getContractCode());
                orderFuture.setBaseCoin(quanContractCode.getSymbol());
                // 火币期货下单默认QuoteCoin为usd所以就写死了
                orderFuture.setQuoteCoin("usd");
                orderFuture.setContractType(quanContractCode.getContractType());
                orderFuture.setContractCode(reqDto.getContractCode());
            }
            orderFuture.setStatus(OrderStatusEnum.PRE_SUBMITTED.getOrderStatus());
            orderFuture.setCreateDate(new Date());
            orderFuture.setUpdateDate(new Date());
            quanOrderFutureMapper.insert(orderFuture);

            FuturePlaceOrderRespDto respDto = new FuturePlaceOrderRespDto();
            respDto.setInnerOrderId(orderFuture.getInnerOrderId());
            respDto.setLinkOrderId(reqDto.getLinkOrderId());
            reqDto.setClientOrderId(orderFuture.getInnerOrderId());
            Long exOrderId = doPlaceHuobiOrder(reqDto);
            // 下单完成后更新exOrderId到order表
            if (exOrderId != null) {
                orderFuture.setExOrderId(exOrderId);
                orderFuture.setStatus(OrderStatusEnum.SUBMITTED.getOrderStatus());
                quanOrderFutureMapper.updateByPrimaryKeySelective(orderFuture);
            }
            respDto.setExOrderId(exOrderId);
            return ServiceResult.buildSuccessResult(respDto);
        } catch (Exception e) {
            logger.error("下单异常，订单：" + reqDto);
            return ServiceResult.buildAPIErrorResult(e.getMessage());
        }
    }

    private Long doPlaceHuobiOrder(FuturePlaceOrderReqDto reqDto) {
        FutureHuobiOrderRequest request = new FutureHuobiOrderRequest();
        if (StringUtils.isEmpty(reqDto.getContractCode())) {
            request.setSymbol(reqDto.getBaseCoin().toUpperCase());
            request.setContractType(reqDto.getContractType());
        } else {
            request.setContractCode(reqDto.getContractCode());
        }
        request.setPrice(reqDto.getPrice().toString());
        request.setVolume(reqDto.getQuantity().toString());
        if (reqDto.getSide() == 1) {
            request.setDirection("buy");
        } else {
            request.setDirection("sell");
        }
        if (reqDto.getOffset() == 1) {
            request.setOffset("open");
        } else {
            request.setOffset("close");
        }
        request.setLeverRate(reqDto.getLever() + "");
        request.setOrderPriceType("limit");
        request.setClientOrderId(reqDto.getClientOrderId() + "");
        Long exOrderId = huobiFutureOrderService.placeOrder(request);
        return exOrderId;
    }

    public ServiceResult<FuturePlaceOrderRespDto> placeOkOrder(FuturePlaceOrderReqDto reqDto) {
        // 插入order表生成内部订单id
        QuanOrderFuture orderFuture = new QuanOrderFuture();
        orderFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
        orderFuture.setAccountId(reqDto.getAccountId());
        orderFuture.setLinkOrderId(reqDto.getLinkOrderId());
        orderFuture.setStatus(OrderStatusEnum.PRE_SUBMITTED.getOrderStatus());
        orderFuture.setCreateDate(new Date());
        orderFuture.setUpdateDate(new Date());
        quanOrderFutureMapper.insert(orderFuture);

        FuturePlaceOrderRespDto respDto = new FuturePlaceOrderRespDto();
        respDto.setInnerOrderId(orderFuture.getInnerOrderId());
        respDto.setLinkOrderId(reqDto.getLinkOrderId());
        if (reqDto.isSync()) {
            Long okOrderId = doPlaceOkOrder(reqDto, orderFuture);
            respDto.setExOrderId(okOrderId);
        } else {
            // 异步执行
            AsyncUtils.runAsyncNoException(() -> {
                doPlaceOkOrder(reqDto, orderFuture);
            });
        }
        return ServiceResult.buildSuccessResult(respDto);
    }

    private Long doPlaceOkOrder(FuturePlaceOrderReqDto reqDto, QuanOrderFuture orderFuture) {
        FutureOkOrderRequest orderRequest = new FutureOkOrderRequest();
        orderRequest.setAccountId(reqDto.getAccountId());
        String symbol = null;
        String contractType = null;
        String contractCode = null;
        if (StringUtils.isNotEmpty(reqDto.getContractCode())) {
            QuanContractCode quanContractCode = contractService.getContractCode(reqDto.getExchangeId(), reqDto.getContractCode());
            symbol = quanContractCode.getSymbol();
            contractType = quanContractCode.getContractType();
            contractCode = quanContractCode.getContractCode();
        } else {
            symbol = concatSymbol(reqDto.getBaseCoin(), reqDto.getQuoteCoin());
            contractType = reqDto.getContractType();
        }
        orderRequest.setSymbol(symbol);
        orderRequest.setContractType(contractType);
        orderRequest.setPrice(reqDto.getPrice().toString());
        // todo 下单数量，单位
        orderRequest.setAmount(reqDto.getQuantity().toString());
        int type = getOkOrderType(reqDto.getSide(), reqDto.getOffset());
        orderRequest.setType(type);
        // todo 订单类型默认给市价单
        orderRequest.setMatchPrice(0);
        orderRequest.setLeverRate(reqDto.getLever() + "");
        Long orderId = okOrderService.placeOkOrder(orderRequest);
        // 下单完成后更新交易所id到order表
        orderFuture.setExOrderId(orderId);
        orderFuture.setContractType(contractType);
        orderFuture.setContractCode(contractCode);
        orderFuture.setSourceStatus(0);
        quanOrderFutureMapper.updateByPrimaryKeySelective(orderFuture);
        return orderId;
    }

    private int getOkOrderType(int side, int offset) {
        if (side == 1) {
            // 买入开仓
            if (offset == 1) {
                return 1;
            } else {
                // 买入平仓
                return 2;
            }
        } else {
            // 卖出开仓
            if (offset == 1) {
                return 3;
            } else {
                // 卖出平仓
                return 4;
            }
        }
    }

    private String concatSymbol(String baseCoin, String quoteCoin) {
        return baseCoin.toLowerCase() + "_" + quoteCoin.toLowerCase();
    }

    @Override
    public ServiceResult<FuturePriceOrderRespDto> getActiveOrderMap(FuturePriceOrderReqDto reqDto) {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.SUBMITTED.getOrderStatus());
        statusList.add(OrderStatusEnum.PARTIAL_FILLED.getOrderStatus());
        // 活跃订单中不应该包含撤单中的订单
        //statusList.add(OrderStatusEnum.CANCELING.getOrderStatus());
        try {
            List<QuanOrderFuture> list = quanOrderFutureMapper.selectOrderByStatus(reqDto.getExchangeId(), reqDto.getAccountId(), reqDto.getContractCode(), statusList);
            Map<BigDecimal, List<QuanOrderFuture>> result = list.stream().collect(Collectors.groupingBy(e -> e.getOrderPrice()));
            FuturePriceOrderRespDto respDto = new FuturePriceOrderRespDto();
            Map<BigDecimal, List<FuturePriceOrderRespDto.DataBean>> priceOrderMap = new HashMap<>();
            result.forEach((k, v) -> {
                List<FuturePriceOrderRespDto.DataBean> beanList = new ArrayList<>();
                v.stream().forEach(e -> {
                    FuturePriceOrderRespDto.DataBean dataBean = new FuturePriceOrderRespDto.DataBean();
                    BeanUtils.copyProperties(e, dataBean);
                    beanList.add(dataBean);
                });
                priceOrderMap.put(k, beanList);
            });
            respDto.setPriceOrderMap(priceOrderMap);
            return ServiceResult.buildSuccessResult(respDto);
        } catch (Exception e) {
            logger.error("获取活跃订单价格分组Map失败，exchangeId={}，accountId={}", reqDto.getExchangeId(), reqDto.getAccountId());
            return ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        }
    }

    @Override
    public ServiceResult<Long> cancelSingleOrder(FutureCancelSingleOrderReqDto reqDto) {
        if (reqDto.getExchangeId() == ExchangeEnum.HUOBI_FUTURE.getExId()) {
            Long exOrderId = huobiFutureOrderService.cancelOrder(reqDto.getAccountId(), reqDto.getExOrderId(), null);
            if (exOrderId != null) {
                return ServiceResult.buildSuccessResult(exOrderId);
            } else {
                return ServiceResult.buildErrorResult(ServiceErrorEnum.CANCEL_ORDER_ERROR);
            }
        }
        return null;
    }

    @Override
    public ServiceResult cancelAllOrder(FutureCancelAllOrderReqDto reqDto) {
        try {
            if (ExchangeEnum.HUOBI_FUTURE.getExId() == reqDto.getExchangeId()) {
                huobiFutureOrderService.cancelAllOrder(reqDto.getAccountId(), reqDto.getSymbol());
                return ServiceResult.buildSuccessResult(null);
            }
            return null;
        } catch (Throwable e) {
            logger.error("取消火币期货所有订单失败", e);
            return ServiceResult.buildAPIErrorResult(e.getMessage());
        }
    }

    @Override
    public ServiceResult updateOrderInfo(FutureUpdateOrderReqDto reqDto) {
        try {
            if (ExchangeEnum.HUOBI_FUTURE.getExId() == reqDto.getExchangeId()) {
                List<Long> failedOrderIds = huobiFutureOrderService.updateHuobiOrderInfo(reqDto.getAccountId(), reqDto.getContractCode());
                if (CollectionUtils.isEmpty(failedOrderIds)) {
                    return ServiceResult.buildSuccessResult(null);
                } else {
                    return ServiceResult.buildAPIErrorResult("更新失败的订单：" + failedOrderIds);
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("updateOrderInfo失败", e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        }
    }

}
