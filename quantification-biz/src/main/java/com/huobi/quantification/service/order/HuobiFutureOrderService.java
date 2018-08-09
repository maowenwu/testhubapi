package com.huobi.quantification.service.order;

import com.huobi.quantification.request.future.FutureHuobiOrderRequest;

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


    Long cancelOrder(Long orderId, Long clientOrderId);


    boolean updateHuobiOrderInfo(Long accountId);

    boolean cancelAllOrder(String symbol);

    void replenishOrder(Long accountId);
}
