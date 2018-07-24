package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;

import java.util.List;

public interface FutureOrderService {

    /**
     * 下单
     *
     * @param futureOrderReqDto
     * @return
     */
    ServiceResult<FutureOrderRespDto> placeOrder(FutureOrderReqDto futureOrderReqDto);

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
    ServiceResult getOrderByInnerOrderId();

    /**
     * 查询订单-根据交易所orderid
     *
     * @return
     */
    ServiceResult getOrderByExOrderId();

    /**
     * 查询订单-根据关联orderid
     *
     * @return
     */
    ServiceResult getOrderByLinkOrderId();

    /**
     * 查询订单-根据订单状态
     *
     * @return
     */
    ServiceResult getOrderByStatus();

    /**
     * 撤销订单-根据内部orderID
     *
     * @return
     */
    ServiceResult cancelOrder(FutureCancelOrderReqDto cancelOrderReqDto);


    /**
     * 撤销活跃订单
     *
     * @param orderReqDto
     * @return
     */
    ServiceResult cancelActiveOrder(FutureActiveOrderReqDto orderReqDto);


}
