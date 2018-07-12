package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureOrderRequestDto;
import com.huobi.quantification.dto.FutureOrderResponseDto;

public interface FutureOrderService {


    ServiceResult<FutureOrderResponseDto> placeOrder(FutureOrderRequestDto futureOrderRequestDto);

    ServiceResult placeBatchOrders(int exchangeId, long accountID, Object orders, boolean parallel, int timeInterval, boolean sync);

    /**
     * 查询订单-根据内部orderid
     *
     * @return
     */
    ServiceResult getOrderByInnerOrderID();

    /**
     * 查询订单-根据交易所orderid
     *
     * @return
     */
    ServiceResult getOrderByExOrderID();

    /**
     * 查询订单-根据关联orderid
     *
     * @return
     */
    ServiceResult getOrderByLinkOrderID();

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
    ServiceResult cancelOrder();


    /**
     * 撤销活跃订单
     *
     * @param id
     * @return
     */
    ServiceResult cancelOrder(Object id);


}
