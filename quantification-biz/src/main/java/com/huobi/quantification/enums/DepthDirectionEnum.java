package com.huobi.quantification.enums;

public enum  DepthDirectionEnum {

    ASKS(0),BIDS(1);

    private int intType;

    DepthDirectionEnum(int intType) {
        this.intType = intType;
    }

    public int getIntType() {
        return intType;
    }
}
