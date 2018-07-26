package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class StrategyOrderConfig {
    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private Integer id;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private String symbol;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private String contractType;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private BigDecimal contractFee;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private BigDecimal spotFee;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private BigDecimal deliveryFee;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private BigDecimal expectYields;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private BigDecimal priceStep;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private Integer asksMaxAmount;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private Integer bidsMaxAmount;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private BigDecimal asksBasisPrice;

    /**
     * @mbg.generated 2018-07-26 13:12:25
     */
    private BigDecimal bidsBasisPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getContractFee() {
        return contractFee;
    }

    public void setContractFee(BigDecimal contractFee) {
        this.contractFee = contractFee;
    }

    public BigDecimal getSpotFee() {
        return spotFee;
    }

    public void setSpotFee(BigDecimal spotFee) {
        this.spotFee = spotFee;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getExpectYields() {
        return expectYields;
    }

    public void setExpectYields(BigDecimal expectYields) {
        this.expectYields = expectYields;
    }

    public BigDecimal getPriceStep() {
        return priceStep;
    }

    public void setPriceStep(BigDecimal priceStep) {
        this.priceStep = priceStep;
    }

    public Integer getAsksMaxAmount() {
        return asksMaxAmount;
    }

    public void setAsksMaxAmount(Integer asksMaxAmount) {
        this.asksMaxAmount = asksMaxAmount;
    }

    public Integer getBidsMaxAmount() {
        return bidsMaxAmount;
    }

    public void setBidsMaxAmount(Integer bidsMaxAmount) {
        this.bidsMaxAmount = bidsMaxAmount;
    }

    public BigDecimal getAsksBasisPrice() {
        return asksBasisPrice;
    }

    public void setAsksBasisPrice(BigDecimal asksBasisPrice) {
        this.asksBasisPrice = asksBasisPrice;
    }

    public BigDecimal getBidsBasisPrice() {
        return bidsBasisPrice;
    }

    public void setBidsBasisPrice(BigDecimal bidsBasisPrice) {
        this.bidsBasisPrice = bidsBasisPrice;
    }
}