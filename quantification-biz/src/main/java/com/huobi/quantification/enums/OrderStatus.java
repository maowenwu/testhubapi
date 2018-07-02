package com.huobi.quantification.enums;

public enum OrderStatus {

    FINISH(1), UNFINISH(0);

    private int intStatus;

    OrderStatus(int intStatus) {
        this.intStatus = intStatus;
    }

    public int getIntStatus() {
        return intStatus;
    }
}
