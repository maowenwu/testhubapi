package com.huobi.quantification.service.order;

import com.huobi.quantification.dto.HuobiTradeOrderDto;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface HuobiOrderService {
	
	void updateHuobiOrder(Long orderId);
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
    Object getHuobiOrdersHistory(Long orderId);
    
    /**
     * 获取所有当前帐号下未成交订单
     * 
     * @return
     */
    Object getHuobiOpenOrders();
    /**
     * 下单
     *
     * @return
     */
    Long placeHuobiOrder(HuobiTradeOrderDto orderDto);

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
    Object cancelHuobiOrder(Long orderId);

    /**
     * 批量撤单
     *
     * @return
     */
    Object cancelHuobiOrders();
}
