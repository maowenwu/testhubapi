package com.huobi.quantification.enums;

public enum OkJobTypeEnum {

    Depth(1), Kline(2), Ticker(3), UserInfo(4), Position(5), Order(6), CurrentPrice(7),Index(8);


    private int jobType;

    OkJobTypeEnum(int jobType) {
        this.jobType = jobType;
    }

    public int getJobType() {
        return jobType;
    }
}
