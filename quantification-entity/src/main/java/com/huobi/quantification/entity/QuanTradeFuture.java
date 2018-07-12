package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanTradeFuture {
    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private Long id;

    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private String symbol;

    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private String contractType;

    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private Long queryId;

    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private String type;

    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private BigDecimal price;

    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private BigDecimal amount;

    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private Date createDate;

    /**
     * @mbg.generated 2018-07-12 21:24:14
     */
    private Date updateTime;

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

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}