package com.huobi.quantification.service.order;

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
     * 合约下单
     *
     * @return
     */
    Long placeOkOrder(FutureOkOrderRequest order);



    /**
     * 合约撤单
     *
     * @return
     */
    OKFutureCancelOrderResponse cancelOkOrder(FutureOkCancelOrderRequest cancelOrderRequest);



}
