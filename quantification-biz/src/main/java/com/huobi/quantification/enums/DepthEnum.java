package com.huobi.quantification.enums;

public enum DepthEnum {

    ASKS(0),BIDS(1);

    private int intType;

    DepthEnum(int intType) {
        this.intType = intType;
    }

    public int getIntType() {
        return intType;
    }
}
