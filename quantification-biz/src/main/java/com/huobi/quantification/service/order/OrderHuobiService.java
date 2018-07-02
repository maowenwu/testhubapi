package com.huobi.quantification.service.order;
/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface OrderHuobiService {
	  /**
     * 获取订单信息
     *
     * @return
     */
    Object getHuobiOrderInfo();

    /**
     * 批量获取订单信息
     *
     * @return
     */
    Object getHuobiOrdersInfo();

    /**
     * 查询成交（时间顺序）
     *
     * @return
     */
    Object getHuobiOrdersHistory();

    /**
     * 下单
     *
     * @return
     */
    Object placeHuobiOrder();

    /**
     * 批量下单
     *
     * @return
     */
    Object placeHuobikOrders();

    /**
     * 撤单
     *
     * @return
     */
    Object cancelHuobiOrder();

    /**
     * 批量撤单
     *
     * @return
     */
    Object cancelHuobiOrders();
}
