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

    public static SideEnum valueOf(int side) {
        for (SideEnum sideEnum : values()) {
            if (sideEnum.getSideType() == side) {
                return sideEnum;
            }
        }
        throw new IllegalArgumentException("输入SideEnum异常，side=" + side);
    }
}
