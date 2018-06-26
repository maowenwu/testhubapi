package com.huobi.contract.index.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Ticker {


    private BigDecimal price;

    private Date tradeTime;

    public Ticker() {
    }

    public Ticker(BigDecimal price, Date tradeTime) {
        this.price = price;
        this.tradeTime = tradeTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "price=" + price +
                ", tradeTime=" + tradeTime +
                '}';
    }
}
