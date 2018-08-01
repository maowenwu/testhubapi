package com.huobi.quantification.enums;

public enum JobTypeEnum {

    Depth(1), Kline(2), Ticker(3), Account(4), Position(5), Order(6), CurrentPrice(7),Index(8);


    private int jobType;

    JobTypeEnum(int jobType) {
        this.jobType = jobType;
    }

    public int getJobType() {
        return jobType;
    }
}
