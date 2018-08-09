package com.huobi.quantification.strategy.enums;

public enum HedgerActionEnum {

    NORMAL(0), STOP_HEDGER(2);

    private int action;

    HedgerActionEnum(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public static HedgerActionEnum valueOf(int action) {
        for (HedgerActionEnum actionEnum : values()) {
            if (actionEnum.getAction() == action) {
                return actionEnum;
            }
        }
        throw new IllegalArgumentException("输入HedgerActionEnum异常，action=" + action);
    }
}
