package com.huobi.contract.index.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContractPriceIndexHis implements Serializable {
    private static final long serialVersionUID = -1763416867391713461L;
    private Long id;

    private Long exchangeId;

    private String targetSymbol;

    private String sourceSymbol;

    private BigDecimal targetPrice;

    private BigDecimal sourcePrice;

    private BigDecimal rate;

    private Integer status;

    private Date tradeTime;

    private Date inputTime;

    private int origin;

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

    public String getTargetSymbol() {
        return targetSymbol;
    }

    public void setTargetSymbol(String targetSymbol) {
        this.targetSymbol = targetSymbol == null ? null : targetSymbol.trim();
    }

    public String getSourceSymbol() {
        return sourceSymbol;
    }

    public void setSourceSymbol(String sourceSymbol) {
        this.sourceSymbol = sourceSymbol == null ? null : sourceSymbol.trim();
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }

    public BigDecimal getSourcePrice() {
        return sourcePrice;
    }

    public void setSourcePrice(BigDecimal sourcePrice) {
        this.sourcePrice = sourcePrice;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "ContractPriceIndexHis{" +
                "id=" + id +
                ", exchangeId=" + exchangeId +
                ", targetSymbol='" + targetSymbol + '\'' +
                ", sourceSymbol='" + sourceSymbol + '\'' +
                ", targetPrice=" + targetPrice +
                ", sourcePrice=" + sourcePrice +
                ", rate=" + rate +
                ", status=" + status +
                ", tradeTime=" + tradeTime +
                ", inputTime=" + inputTime +
                ", origin=" + origin +
                '}';
    }
}