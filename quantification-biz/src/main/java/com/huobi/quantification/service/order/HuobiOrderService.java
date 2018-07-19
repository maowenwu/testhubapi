package com.huobi.quantification.service.order;

import com.huobi.quantification.dto.HuobiTradeOrderDto;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface HuobiOrderService {
	/**
	 * 定时更新订单详情
	 * 
	 * @param accountId
	 * @param orderId
	 */
	void updateHuobiOrder(Long accountId, String symbol);
	  /**
     * 获取订单信息
     *
     * @return
     */
    Object getHuobiOrderInfo(Long accountId);

    /**
     * 批量获取订单信息
     *
     * @return
     */
    Object getHuobiOrdersInfo(Long accountId);

    /**
     * 查询成交（时间顺序）
     *
     * @return
     */
    Object getHuobiOrdersHistory(Long accountId, Long orderId);
    
    /**
     * 获取所有当前帐号下未成交订单
     * 
     * @return
     */
    Object getHuobiOpenOrders(Long accountId);
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
    Object placeHuobikOrders(Long accountId);

    /**
     * 撤单
     *
     * @return
     */
    Object cancelHuobiOrder(Long accountId, Long orderId);

    /**
     * 批量撤单
     *
     * @return
     */
    Object cancelHuobiOrders(Long accountId);
}
