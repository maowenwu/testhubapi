package com.huobi.quantification.service.order;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface OrderService {



    void updateOkOrderInfo(Long accountId, String symbol, String contractType);

    /**
     * 查询成交（时间顺序）
     *
     * @return
     */
    Object storeOkOrdersHistory();


}
