package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class StrategyOrderConfig {
    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private Integer id;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private String symbol;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private String contractType;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal contractFee;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal spotFee;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal deliveryFee;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal expectYields;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal priceStep;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private Integer asksMaxAmount;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private Integer bidsMaxAmount;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal asksBasisPrice;

    /**
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal bidsBasisPrice;

    /**
     * 多仓数量最大限制（包括持仓和未成交的开仓单）
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal longMaxAmount;

    /**
     * 空仓数量最大限制（包括持仓和未成交的开仓单）
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal shortMaxAmount;

    /**
     * 最大持仓量（用于控制开平方向）
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal maxPositionAmount;

    /**
     * 最小持仓量（用于控制开平方向）
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal minPositionAmount;

    /**
     * 合约账户保留保证金
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal contractMarginReserve;

    /**
     * 币币账户保留币量
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal spotCoinReserve;

    /**
     * 币币账户保留资金
     * @mbg.generated 2018-07-30 19:46:41
     */
    private BigDecimal spotBalanceReserve;

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

    public BigDecimal getLongMaxAmount() {
        return longMaxAmount;
    }

    public void setLongMaxAmount(BigDecimal longMaxAmount) {
        this.longMaxAmount = longMaxAmount;
    }

    public BigDecimal getShortMaxAmount() {
        return shortMaxAmount;
    }

    public void setShortMaxAmount(BigDecimal shortMaxAmount) {
        this.shortMaxAmount = shortMaxAmount;
    }

    public BigDecimal getMaxPositionAmount() {
        return maxPositionAmount;
    }

    public void setMaxPositionAmount(BigDecimal maxPositionAmount) {
        this.maxPositionAmount = maxPositionAmount;
    }

    public BigDecimal getMinPositionAmount() {
        return minPositionAmount;
    }

    public void setMinPositionAmount(BigDecimal minPositionAmount) {
        this.minPositionAmount = minPositionAmount;
    }

    public BigDecimal getContractMarginReserve() {
        return contractMarginReserve;
    }

    public void setContractMarginReserve(BigDecimal contractMarginReserve) {
        this.contractMarginReserve = contractMarginReserve;
    }

    public BigDecimal getSpotCoinReserve() {
        return spotCoinReserve;
    }

    public void setSpotCoinReserve(BigDecimal spotCoinReserve) {
        this.spotCoinReserve = spotCoinReserve;
    }

    public BigDecimal getSpotBalanceReserve() {
        return spotBalanceReserve;
    }

    public void setSpotBalanceReserve(BigDecimal spotBalanceReserve) {
        this.spotBalanceReserve = spotBalanceReserve;
    }
}