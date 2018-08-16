package com.huobi.quantification.service.order;

import com.huobi.quantification.request.future.FutureHuobiOrderRequest;

import java.util.List;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface HuobiFutureOrderService {


    /**
     * 合约下单
     *
     * @return
     */
    Long placeOrder(FutureHuobiOrderRequest order);


    Long cancelOrder(Long accountId,Long orderId, Long clientOrderId);


    List<Long> updateHuobiOrderInfo(Long accountId, String contractCode);

    void cancelAllOrder(Long accountId,String symbol);

    void replenishOrder(Long accountId, String symbol);
}
