package com.huobi.quantification.facade;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.OkTradeOrderDto;

public interface OkOrderServiceFacade {


    /**
     * 获取合约订单信息
     *
     * @return
     */
    ServiceResult getOkOrderInfo();


    /**
     * 批量获取合约订单信息
     *
     * @return
     */
    ServiceResult getOkOrdersInfo();


    /**
     * 合约下单
     *
     * @return
     */
    ServiceResult placeOkOrder(OkTradeOrderDto order);

    /**
     * 批量下单
     *
     * @return
     */
    ServiceResult placeOkOrders();

    /**
     * 合约撤单
     *
     * @return
     */
    ServiceResult cancelOkOrder();

    /**
     * 合约批量撤单
     *
     * @return
     */
    ServiceResult cancelOkOrders();
}
