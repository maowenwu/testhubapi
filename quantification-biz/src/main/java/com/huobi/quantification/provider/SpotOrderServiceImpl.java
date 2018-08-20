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


    /**
     * 将数据库查到的实体转换为对应的响应输出结果
     *
     * @param quanOrder quanOrder
     * @return SpotOrderRespDto
     */
    public SpotOrderRespDto copySpotOrderToSpotOrderRespDto(QuanOrder quanOrder) {
        SpotOrderRespDto result = new SpotOrderRespDto();
        result.setExOrderID(quanOrder.getExchangeId());
        // result.setLinkOrderID();
        result.setCreateTime(quanOrder.getOrderCreatedAt());
        // result.setUpdateTime();
        result.setStatus(quanOrder.getOrderState());
        // result.setBaseCoin();
        // result.setQuoteCoin();
        // result.setSide();
        result.setOrderType(quanOrder.getOrderType());
        result.setOrderPrice(quanOrder.getOrderPrice());
        result.setDealPrice(quanOrder.getOrderFieldCashAmount());
        // result.setOrderQty();
        // result.setDealQty();
        result.setOrderCashAmount(quanOrder.getOrderAmount());
        result.setDealCashAmount(quanOrder.getOrderFieldCashAmount());
        // result.setRemainingQty();
        result.setFees(quanOrder.getOrderFieldFees());
        return result;
    }

    /**
     * 将数据库查到的实体(list)转换为对应的响应输出结果List<SpotOrderRespDto>
     *
     * @param list list
     * @return List<SpotOrderRespDto>
     */
    public List<SpotOrderRespDto> copySpotOrderListToSpotOrderRespLIstDto(List<QuanOrder> list) {
        List<SpotOrderRespDto> result = new ArrayList<>();
        for (QuanOrder temp : list) {
            result.add(copySpotOrderToSpotOrderRespDto(temp));
        }
        return result;
    }

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


    /**
     * 一个一个取消订单
     */
    private Map<String, Object> cancelOrderOneByOne(List<Long> orderIds, Long accountID, Long timeInterval) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        Boolean status = true;
        List<Long> successList = new ArrayList<>();
        List<Map<String, Object>> failList = new ArrayList<>();
        String errorCode = "";
        com.alibaba.fastjson.JSONObject parseObject = null;
        for (Long tempOrderId : orderIds) {
            String body = "";
            try {
                body = httpService.doHuobiSpotPost(accountID, HttpConstant.HUOBI_SUBMITCANCEL
                        .replaceAll("\\{order-id\\}", String.valueOf(tempOrderId)), null);
                Thread.sleep(timeInterval == null ? 100L : timeInterval);
                logger.info("订单号:{}取消订单返回的结果为:{}", String.valueOf(tempOrderId), body);
                parseObject = JSON.parseObject(body);
                errorCode = parseObject.getString("err-code");
            } catch (Exception e) {
                logger.error("订单号:{}取消订单发生了异常:{}", String.valueOf(tempOrderId), e);
                errorCode = String.valueOf(ServiceErrorEnum.TIMEOUT_ERROR.getCode());
            }
            if (null != parseObject && "ok".equalsIgnoreCase(parseObject.getString("status"))) {
                successList.add(tempOrderId);
            } else {
                Map<String, Object> failMap = new HashMap<>();
                failMap.put("innerOrderID", String.valueOf(tempOrderId));
                failMap.put("error_code", errorCode);
                status = false;
                failList.add(failMap);
            }
        }
        map.put("success", successList);
        map.put("fail", failList);
        resultMap.put("result", map);
        resultMap.put("status", status);
        return resultMap;

    }


    /**
     * 批量取消订单
     *
     * @param orderIds
     * @param accountID
     * @param size
     * @return
     */
    private Map<String, Object> cancelOrderBatch(List<Long> orderIds, Long accountID, int size) {
        Boolean status = true;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        // 调用批量撤销接口单次不超过50个订单id
        Map<String, Object> paramMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (Long temp : orderIds) {
            list.add(temp.toString());
        }
        for (int i = 0; 50 * i <= list.size(); i++) {
            paramMap.put("order-ids", list.subList(50 * i, 50));
            logger.info("批量取消订单的请求orderIds为:{}", list.subList(50 * i, 50));
            try {
                String body = httpService.doHuobiSpotPost(accountID, HttpConstant.HUOBI_BATCHCANCEL,
                        paramMap);
                logger.info("批量取消订单返回的结果为:{}", body);
                map = parseData(body);
            } catch (Exception e) {
                logger.error("批量取消订单发生了异常:{}", e);
                status = false;
            }
        }
        resultMap.put("result", map);
        resultMap.put("status", status);
        return resultMap;
    }

    /**
     * 解析批量撤单返回的数据
     *
     * @param input input
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, Object> parseData(String input) {
        Map<String, Object> resultMap = new HashMap<>();
        Map maps = (Map) JSON.parse(input);
        Object object = maps.get("data");
        Map<String, Object> objectMap = (Map<String, Object>) JSON.parse(object.toString());
        List successMap = (List) JSON.parse(objectMap.get("success").toString());
        List<Map> failMap = (List<Map>) JSON.parse(objectMap.get("failed").toString());
        for (Map tempMap : failMap) {
            tempMap.put("innerOrderID", tempMap.get("order-id"));
            tempMap.put("error_code", tempMap.get("err-code"));
            tempMap.remove("order-id");
            tempMap.remove("err-code");
            tempMap.remove("err-msg");
        }
        resultMap.put("success", successMap);
        resultMap.put("fail", failMap);
        return resultMap;
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
