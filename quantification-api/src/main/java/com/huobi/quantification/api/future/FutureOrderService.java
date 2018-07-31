package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;

import java.util.List;

public interface FutureOrderService {

    /**
     * 下单
     *
     * @param futurePlaceOrderReqDto
     * @return
     */
    ServiceResult<FuturePlaceOrderRespDto> placeOrder(FuturePlaceOrderReqDto futurePlaceOrderReqDto);

    /**
     * 批量下单
     *
     * @param batchOrderReqDto
     * @return
     */
    ServiceResult<List<FutureBatchOrderRespDto>> placeBatchOrders(FutureBatchOrderReqDto batchOrderReqDto);

    /**
     * 查询订单-根据内部orderid
     *
     * @return
     */
    ServiceResult<FutureQueryOrderRespDto> getOrderByInnerOrderId(FutureQueryOrderInnerReqDto orderInnerReqDto);

    /**
     * 查询订单-根据交易所orderid
     *
     * @return
     */
    ServiceResult<FutureQueryOrderRespDto> getOrderByExOrderId(FutureQueryOrderExOrderIdReqDto orderExOrderIdReqDto);

    /**
     * 查询订单-根据关联orderid
     *
     * @return
     */
    ServiceResult<FutureQueryOrderRespDto> getOrderByLinkOrderId(FutureQueryOrderLinkReqDto reqDto);

    /**
     * 查询订单-根据订单状态
     *
     * @return
     */
    ServiceResult<FutureQueryOrderRespDto> getOrderByStatus(FutureQueryOrderStatusReqDto reqDto);

    /**
     * 撤销订单-根据内部orderID
     *
     * @return
     */
    ServiceResult cancelOrder(FutureCancelOrderReqDto cancelOrderReqDto);

    ServiceResult<Long> cancelSingleOrder(FutureCancelSingleOrderReqDto reqDto);


    /**
     * 撤销活跃订单
     *
     * @param orderReqDto
     * @return
     */
    ServiceResult cancelActiveOrder(FutureActiveOrderReqDto orderReqDto);


    ServiceResult<FuturePriceOrderRespDto> getActiveOrderMap(FuturePriceOrderReqDto reqDto);

    ServiceResult cancelAllOrder(String symbol);

    ServiceResult updateOrderInfo(Integer exchangeId, Long accountId);
}
