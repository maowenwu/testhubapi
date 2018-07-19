package com.huobi.quantification.service.order;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.request.future.FutureOkBatchOrderRequest;
import com.huobi.quantification.request.future.FutureOkCancelOrderRequest;
import com.huobi.quantification.request.future.FutureOkOrderRequest;
import com.huobi.quantification.response.future.OKFutureCancelOrderResponse;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface OkOrderService {



    void updateOkOrderInfo(Long accountId, String symbol, String contractType);

    /**
     * 查询成交（时间顺序）
     *
     * @return
     */
    Object storeOkOrdersHistory();


    /**
     * 获取合约订单信息
     *
     * @return
     */
    ServiceResult getOkOrderInfo();


    /**
     * 批量获取合约订单信息
     *
     * @return
     */
    ServiceResult getOkOrdersInfo();


    /**
     * 合约下单
     *
     * @return
     */
    Long placeOkOrder(FutureOkOrderRequest order);

    /**
     * 批量下单
     *
     * @return
     */
    ServiceResult placeOkOrders(FutureOkBatchOrderRequest batchOrderRequest);

    /**
     * 合约撤单
     *
     * @return
     */
    OKFutureCancelOrderResponse cancelOkOrder(FutureOkCancelOrderRequest cancelOrderRequest);

    /**
     * 合约批量撤单
     *
     * @return
     */
    ServiceResult cancelOkOrders();

}
