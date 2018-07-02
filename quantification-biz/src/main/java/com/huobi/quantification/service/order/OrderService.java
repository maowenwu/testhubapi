package com.huobi.quantification.service.order;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface OrderService {

    /**
     * 定时拉去ok所有订单信息，包括已完成、未完成订单
     */
    void storeOkFutureOrder();

    /**
     * 查询成交（时间顺序）
     *
     * @return
     */
    Object storeOkOrdersHistory();


}
