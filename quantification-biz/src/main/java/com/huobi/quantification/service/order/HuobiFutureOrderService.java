package com.huobi.quantification.service.order;

import com.huobi.quantification.bo.HuobiFutureOrderBO;

import java.util.List;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface HuobiFutureOrderService {

    List<Long> updateHuobiOrderInfo(Long accountId, String contractCode);

    Long placeOrder(HuobiFutureOrderBO order);

    Long cancelOrder(Long accountId, Long orderId, Long clientOrderId);

    void cancelAllOrder(Long accountId, String symbol);

    void replenishOrder(Long accountId, String symbol);
}
