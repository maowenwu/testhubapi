package com.huobi.contract.index.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExchangeRateHis implements Serializable {
    private static final long serialVersionUID = -2927099020215323582L;
    private Long id;

    private Long exchangeId;

    private String exchangeSymbol;

    private BigDecimal exchangeRate;

    private Date exchangeTime;

    private Integer status;

    private Date inputTime;
    public ExchangeRateHis(){}
    public ExchangeRateHis(Long exchangeId, String exchangeSymbol, BigDecimal exchangeRate, Date exchangeTime, Date inputTime, Integer status) {
        this.exchangeId = exchangeId;
        this.exchangeSymbol = exchangeSymbol;
        this.exchangeRate = exchangeRate;
        this.exchangeTime = exchangeTime;
        this.inputTime = inputTime;
        this.status = status;
    }

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

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}