package com.huobi.contract.index.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContractPriceIndexCalcRecord implements Serializable {
    private static final long serialVersionUID = 5731243044602920242L;
    private Long id;

    private Long exchangeId;

    private String targetSymbol;

    private BigDecimal targetPrice;

    private BigDecimal weight;

    private BigDecimal originalWeight;

    private Date grabTime;

    private Date inputTime;

    private Long priceIndexId;

    private String exchangeFullName;

    private String exchangeShortName;

    private Long contractPriceIndexHisId;

    public Long getContractPriceIndexHisId() {
        return contractPriceIndexHisId;
    }

    public void setContractPriceIndexHisId(Long contractPriceIndexHisId) {
        this.contractPriceIndexHisId = contractPriceIndexHisId;
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

    public String getTargetSymbol() {
        return targetSymbol;
    }

    public void setTargetSymbol(String targetSymbol) {
        this.targetSymbol = targetSymbol == null ? null : targetSymbol.trim();
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getOriginalWeight() {
        return originalWeight;
    }

    public void setOriginalWeight(BigDecimal originalWeight) {
        this.originalWeight = originalWeight;
    }

    public Date getGrabTime() {
        return grabTime;
    }

    public void setGrabTime(Date grabTime) {
        this.grabTime = grabTime;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Long getPriceIndexId() {
        return priceIndexId;
    }

    public void setPriceIndexId(Long priceIndexId) {
        this.priceIndexId = priceIndexId;
    }

    public String getExchangeFullName() {
        return exchangeFullName;
    }

    public void setExchangeFullName(String exchangeFullName) {
        this.exchangeFullName = exchangeFullName;
    }

    public String getExchangeShortName() {
        return exchangeShortName;
    }

    public void setExchangeShortName(String exchangeShortName) {
        this.exchangeShortName = exchangeShortName;
    }
}