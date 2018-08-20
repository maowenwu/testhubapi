package com.huobi.quantification.service.order;

import com.huobi.quantification.bo.HuobiSpotCancelAllOrderBO;
import com.huobi.quantification.dto.HuobiTradeOrderDto;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface HuobiSpotOrderService {


    /**
     * 下单
     *
     * @return
     */
    Long placeHuobiOrder(HuobiTradeOrderDto orderDto);


    void cancelAllOrder(HuobiSpotCancelAllOrderBO cancelAllOrderBO);

}
