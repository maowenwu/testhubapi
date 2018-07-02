package com.huobi.quantification.enums;

public enum OkContractType {

    THIS_WEEK("this_week"), NEXT_WEEK("next_week"), QUARTER("quarter");
    private String type;

    OkContractType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
