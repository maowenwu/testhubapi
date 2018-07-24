package com.huobi.quantification.enums;

public enum OrderStatusEnum {

    /**
     * 准备提交
     */
    PRE_SUBMITTED(0),
    /**
     * 提交
     */
    SUBMITTED(1),
    /**
     * 部分成交
     */
    PARTIAL_FILLED(2),
    /**
     * 部分成交已撤单
     */
    PARTIAL_CANCELED(3),
    /**
     * 已成交
     */
    FILLED(4),
    /**
     * 已撤单
     */
    CANCELED(5),
    /**
     * 撤单中
     */
    CANCELING(6);

    private int orderStatus;

    OrderStatusEnum(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderStatus() {
        return orderStatus;
    }
}
