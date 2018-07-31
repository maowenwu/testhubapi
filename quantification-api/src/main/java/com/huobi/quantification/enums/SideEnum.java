package com.huobi.quantification.enums;

public enum SideEnum {

    BUY(1), SELL(2);

    private int sideType;

    SideEnum(int sideType) {
        this.sideType = sideType;
    }

    public int getSideType() {
        return sideType;
    }
}
