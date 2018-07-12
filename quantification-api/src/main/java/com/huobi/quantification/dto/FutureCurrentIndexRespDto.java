package com.huobi.quantification.dto;

import java.math.BigDecimal;

public class FutureCurrentIndexRespDto {


    private long ts;
    private BigDecimal currentIndexPrice;

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public BigDecimal getCurrentIndexPrice() {
        return currentIndexPrice;
    }

    public void setCurrentIndexPrice(BigDecimal currentIndexPrice) {
        this.currentIndexPrice = currentIndexPrice;
    }
}
