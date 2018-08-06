package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class StrategyOrderConfig {
    /**
     * @mbg.generated 2018-08-06 19:22:26
     */
    private Integer id;

    /**
     * @mbg.generated 2018-08-06 19:22:26
     */
    private String symbol;

    /**
     * @mbg.generated 2018-08-06 19:22:26
     */
    private String contractType;

    /**
     * 合约手续费
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal contractFee;

    /**
     * 现货手续费
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal spotFee;

    /**
     * 交割手续费
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal deliveryFee;

    /**
     * 期望收益率
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal expectYields;

    /**
     * 合并价格
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal priceStep;

    /**
     * 最大卖单量
     * @mbg.generated 2018-08-06 19:22:26
     */
    private Integer asksMaxAmount;

    /**
     * 最大买单量
     * @mbg.generated 2018-08-06 19:22:26
     */
    private Integer bidsMaxAmount;

    /**
     * 卖单基准价格
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal asksBasisPrice;

    /**
     * 买单基准价格
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal bidsBasisPrice;

    /**
     * 多仓数量最大限制（包括持仓和未成交的开仓单）
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal longMaxAmount;

    /**
     * 空仓数量最大限制（包括持仓和未成交的开仓单）
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal shortMaxAmount;

    /**
     * 最大多仓持仓
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal maxLongPosition;

    /**
     * 最大空仓持仓
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal maxShortPosition;

    /**
     * 合约账户保留保证金
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal contractMarginReserve;

    /**
     * 币币账户保留币量
     * @mbg.generated 2018-08-06 19:22:26
     */
    private BigDecimal spotCoinReserve;

    /**
     * 币币账户保留资金
     * @mbg.generated 2018-08-06 19:22:26
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

    public BigDecimal getMaxLongPosition() {
        return maxLongPosition;
    }

    public void setMaxLongPosition(BigDecimal maxLongPosition) {
        this.maxLongPosition = maxLongPosition;
    }

    public BigDecimal getMaxShortPosition() {
        return maxShortPosition;
    }

    public void setMaxShortPosition(BigDecimal maxShortPosition) {
        this.maxShortPosition = maxShortPosition;
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