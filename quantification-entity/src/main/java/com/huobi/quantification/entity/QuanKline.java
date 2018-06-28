package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanKline {
    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long id;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long exchangeId;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long tickId;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String klineType;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String symbol;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String period;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long size;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal high;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal open;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal low;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal close;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Double count;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Double amount;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal vol;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Date ts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getTickId() {
        return tickId;
    }

    public void setTickId(Long tickId) {
        this.tickId = tickId;
    }

    public String getKlineType() {
        return klineType;
    }

    public void setKlineType(String klineType) {
        this.klineType = klineType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}