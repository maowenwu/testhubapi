package com.huobi.quantification.dto;

import java.math.BigDecimal;

public class FutureCurrentPriceRespDto {

    private long ts;
    private BigDecimal currentPrice;

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
}
