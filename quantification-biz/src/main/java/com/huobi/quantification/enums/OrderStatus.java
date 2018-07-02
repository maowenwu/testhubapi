package com.huobi.quantification.enums;

public enum OrderStatus {

    FINISH(2), UNFINISH(1);

    private int intStatus;

    OrderStatus(int intStatus) {
        this.intStatus = intStatus;
    }

    public int getIntStatus() {
        return intStatus;
    }
}
