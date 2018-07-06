package com.huobi.quantification.enums;

public enum ExchangeEnum {

    HUOBI(1, "houbi"), OKEX(2, "okex");

    private int exId;
    private String exName;

    ExchangeEnum(int exId, String exName) {
        this.exId = exId;
        this.exName = exName;
    }

    public int getExId() {
        return exId;
    }

    public String getExName() {
        return exName;
    }
}
