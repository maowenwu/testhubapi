package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanKlineFuture {
    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private Long id;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private String type;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private String symbol;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private String contractType;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private BigDecimal high;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private BigDecimal open;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private BigDecimal low;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private BigDecimal close;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private BigDecimal amount;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private Date ts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}