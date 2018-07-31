package com.huobi.quantification.enums;

public enum HuobiJobTypeEnum {

    Depth(1), Kline(2), Account(3), Order(4), CurrentPrice(5), Position(6), Index(7);


    private int jobType;

    HuobiJobTypeEnum(int jobType) {
        this.jobType = jobType;
    }

    public int getJobType() {
        return jobType;
    }
}
