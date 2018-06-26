package com.huobi.contract.index.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExchangeRate implements Serializable {

    private static final long serialVersionUID = 2715556966193899875L;

    private Long id;

    private String exchangeSymbol;

    private BigDecimal exchangeRate;

    private Date inputTime;

    private String remark;

    public ExchangeRate() {
    }

    public ExchangeRate(String exchangeSymbol, BigDecimal exchangeRate, Date inputTime, String remark) {
        this.exchangeSymbol = exchangeSymbol;
        this.exchangeRate = exchangeRate;
        this.inputTime = inputTime;
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExchangeSymbol() {
        return exchangeSymbol;
    }

    public void setExchangeSymbol(String exchangeSymbol) {
        this.exchangeSymbol = exchangeSymbol == null ? null : exchangeSymbol.trim();
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}