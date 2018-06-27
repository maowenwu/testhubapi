package com.huobi.quantification.service.order;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface OrderService {

    /**
     * 获取合约订单信息
     *
     * @return
     */
    Object getOkOrderInfo();

    /**
     * 批量获取合约订单信息
     *
     * @return
     */
    Object getOkOrdersInfo();

    /**
     * 查询成交（时间顺序）
     *
     * @return
     */
    Object getOkOrdersHistory();

    /**
     * 合约下单
     *
     * @return
     */
    Object placeOkOrder();

    /**
     * 批量下单
     *
     * @return
     */
    Object placeOkOrders();

    /**
     * 合约撤单
     *
     * @return
     */
    Object cancelOkOrder();

    /**
     * 合约批量撤单
     *
     * @return
     */
    Object cancelOkOrders();


}
