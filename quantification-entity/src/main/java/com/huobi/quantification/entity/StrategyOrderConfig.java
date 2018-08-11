package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class StrategyOrderConfig {
    /**
     * @mbg.generated 2018-08-11 14:08:36
     */
    private Integer id;

    /**
     * @mbg.generated 2018-08-11 14:08:36
     */
    private String symbol;

    /**
     * @mbg.generated 2018-08-11 14:08:36
     */
    private String contractType;

    /**
     * 摆单间隔时间(每次摆单后，sleep多少秒进入下一轮)
     * @mbg.generated 2018-08-11 14:08:36
     */
    private Integer placeOrderInterval;

    /**
     * 期望收益率
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal expectYields;

    /**
     * 合并深度价格step
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal priceStep;

    /**
     * 拷贝系数-下限(用于设置depthbook数量)
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal minCopyFactor;

    /**
     * 拷贝系数-上限(用于设置depthbook数量)
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal maxCopyFactor;

    /**
     * 单价格最大委托数量(张)
     * @mbg.generated 2018-08-11 14:08:36
     */
    private Integer maxAmountPerPrice;

    /**
     * 买单价格向下调整数值
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal bidsBasisPrice;

    /**
     * 卖单价格向上调整数值
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal asksBasisPrice;

    /**
     * 买单挂单数量上限(个)
     * @mbg.generated 2018-08-11 14:08:36
     */
    private Integer bidsMaxAmount;

    /**
     * 卖单挂单数量上限(个)
     * @mbg.generated 2018-08-11 14:08:36
     */
    private Integer asksMaxAmount;

    /**
     * 多仓数量上限(张)，包括持仓和未成交的开仓单
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal longMaxAmount;

    /**
     * 空仓数量上限(张)，包括持仓和未成交的开仓单
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal shortMaxAmount;

    /**
     * 多仓持仓总量上限(张)，用于开平控制
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal maxLongPosition;

    /**
     * 空仓持仓总量上限(张)，用于开平控制
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal maxShortPosition;

    /**
     * 合约账户保留保证金
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal contractMarginReserve;

    /**
     * 币币账户保留币量
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal spotCoinReserve;

    /**
     * 币币账户保留资金
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal spotBalanceReserve;

    /**
     * 临近交割停止下开仓单时间
     * @mbg.generated 2018-08-11 14:08:36
     */
    private String stopTime1;

    /**
     * 停止摆盘开始时间
     * @mbg.generated 2018-08-11 14:08:36
     */
    private String stopTime2;

    /**
     * 强平买单滑点
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal buyCloseSlippage;

    /**
     * 强平卖单滑点
     * @mbg.generated 2018-08-11 14:08:36
     */
    private BigDecimal sellCloseSlippage;

    /**
     * 强平单单笔最大数量（张）
     * @mbg.generated 2018-08-11 14:08:36
     */
    private Integer maxCloseAmount;

    /**
     * 强平下单间隔
     * @mbg.generated 2018-08-11 14:08:36
     */
    private Integer closeOrderInterval;

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

    public Integer getPlaceOrderInterval() {
        return placeOrderInterval;
    }

    public void setPlaceOrderInterval(Integer placeOrderInterval) {
        this.placeOrderInterval = placeOrderInterval;
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

    public BigDecimal getMinCopyFactor() {
        return minCopyFactor;
    }

    public void setMinCopyFactor(BigDecimal minCopyFactor) {
        this.minCopyFactor = minCopyFactor;
    }

    public BigDecimal getMaxCopyFactor() {
        return maxCopyFactor;
    }

    public void setMaxCopyFactor(BigDecimal maxCopyFactor) {
        this.maxCopyFactor = maxCopyFactor;
    }

    public Integer getMaxAmountPerPrice() {
        return maxAmountPerPrice;
    }

    public void setMaxAmountPerPrice(Integer maxAmountPerPrice) {
        this.maxAmountPerPrice = maxAmountPerPrice;
    }

    public BigDecimal getBidsBasisPrice() {
        return bidsBasisPrice;
    }

    public void setBidsBasisPrice(BigDecimal bidsBasisPrice) {
        this.bidsBasisPrice = bidsBasisPrice;
    }

    public BigDecimal getAsksBasisPrice() {
        return asksBasisPrice;
    }

    public void setAsksBasisPrice(BigDecimal asksBasisPrice) {
        this.asksBasisPrice = asksBasisPrice;
    }

    public Integer getBidsMaxAmount() {
        return bidsMaxAmount;
    }

    public void setBidsMaxAmount(Integer bidsMaxAmount) {
        this.bidsMaxAmount = bidsMaxAmount;
    }

    public Integer getAsksMaxAmount() {
        return asksMaxAmount;
    }

    public void setAsksMaxAmount(Integer asksMaxAmount) {
        this.asksMaxAmount = asksMaxAmount;
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

    public String getStopTime1() {
        return stopTime1;
    }

    public void setStopTime1(String stopTime1) {
        this.stopTime1 = stopTime1;
    }

    public String getStopTime2() {
        return stopTime2;
    }

    public void setStopTime2(String stopTime2) {
        this.stopTime2 = stopTime2;
    }

    public BigDecimal getBuyCloseSlippage() {
        return buyCloseSlippage;
    }

    public void setBuyCloseSlippage(BigDecimal buyCloseSlippage) {
        this.buyCloseSlippage = buyCloseSlippage;
    }

    public BigDecimal getSellCloseSlippage() {
        return sellCloseSlippage;
    }

    public void setSellCloseSlippage(BigDecimal sellCloseSlippage) {
        this.sellCloseSlippage = sellCloseSlippage;
    }

    public Integer getMaxCloseAmount() {
        return maxCloseAmount;
    }

    public void setMaxCloseAmount(Integer maxCloseAmount) {
        this.maxCloseAmount = maxCloseAmount;
    }

    public Integer getCloseOrderInterval() {
        return closeOrderInterval;
    }

    public void setCloseOrderInterval(Integer closeOrderInterval) {
        this.closeOrderInterval = closeOrderInterval;
    }
}