package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanAccountFuturePosition {
    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private Long id;

    /**
     * 账号ID
     * @mbg.generated 2018-07-02 14:46:55
     */
    private Long accountSourceId;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private Long queryId;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private String contractCode;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private String contractName;

    /**
     * 币种
     * @mbg.generated 2018-07-02 14:46:55
     */
    private String symbol;

    /**
     * 账户权益
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal forceLiquPrice;

    /**
     * 交易所服务器时间
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal buyAmount;

    /**
     * api请求时间
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal buyAvailable;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal buyPriceAvg;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal buyPriceCost;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal buyProfitReal;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal leverRate;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal sellAmount;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal sellAvailable;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal sellPriceAvg;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal sellPriceCost;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private BigDecimal sellProfitReal;

    /**
     * @mbg.generated 2018-07-02 14:46:55
     */
    private Date dateCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountSourceId() {
        return accountSourceId;
    }

    public void setAccountSourceId(Long accountSourceId) {
        this.accountSourceId = accountSourceId;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getForceLiquPrice() {
        return forceLiquPrice;
    }

    public void setForceLiquPrice(BigDecimal forceLiquPrice) {
        this.forceLiquPrice = forceLiquPrice;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public BigDecimal getBuyAvailable() {
        return buyAvailable;
    }

    public void setBuyAvailable(BigDecimal buyAvailable) {
        this.buyAvailable = buyAvailable;
    }

    public BigDecimal getBuyPriceAvg() {
        return buyPriceAvg;
    }

    public void setBuyPriceAvg(BigDecimal buyPriceAvg) {
        this.buyPriceAvg = buyPriceAvg;
    }

    public BigDecimal getBuyPriceCost() {
        return buyPriceCost;
    }

    public void setBuyPriceCost(BigDecimal buyPriceCost) {
        this.buyPriceCost = buyPriceCost;
    }

    public BigDecimal getBuyProfitReal() {
        return buyProfitReal;
    }

    public void setBuyProfitReal(BigDecimal buyProfitReal) {
        this.buyProfitReal = buyProfitReal;
    }

    public BigDecimal getLeverRate() {
        return leverRate;
    }

    public void setLeverRate(BigDecimal leverRate) {
        this.leverRate = leverRate;
    }

    public BigDecimal getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(BigDecimal sellAmount) {
        this.sellAmount = sellAmount;
    }

    public BigDecimal getSellAvailable() {
        return sellAvailable;
    }

    public void setSellAvailable(BigDecimal sellAvailable) {
        this.sellAvailable = sellAvailable;
    }

    public BigDecimal getSellPriceAvg() {
        return sellPriceAvg;
    }

    public void setSellPriceAvg(BigDecimal sellPriceAvg) {
        this.sellPriceAvg = sellPriceAvg;
    }

    public BigDecimal getSellPriceCost() {
        return sellPriceCost;
    }

    public void setSellPriceCost(BigDecimal sellPriceCost) {
        this.sellPriceCost = sellPriceCost;
    }

    public BigDecimal getSellProfitReal() {
        return sellProfitReal;
    }

    public void setSellProfitReal(BigDecimal sellProfitReal) {
        this.sellProfitReal = sellProfitReal;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
}