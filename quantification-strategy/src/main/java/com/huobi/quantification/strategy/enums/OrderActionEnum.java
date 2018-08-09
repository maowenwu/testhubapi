package com.huobi.quantification.strategy.enums;

public enum OrderActionEnum {

    NORMAL(0), CLOSE_ORDER_ONLY(1), STOP_ORDER(2);

    private int action;

    OrderActionEnum(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public static OrderActionEnum valueOf(int action) {
        for (OrderActionEnum actionEnum : values()) {
            if (actionEnum.getAction() == action) {
                return actionEnum;
            }
        }
        throw new IllegalArgumentException("输入OrderActionEnum异常，OrderAction=" + action);
    }
}
