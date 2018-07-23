package com.huobi.quantification.entity;

import java.util.Date;

public class QuanContractCode {
    /**
     * @mbg.generated 2018-07-23 16:44:07
     */
    private Integer id;

    /**
     * @mbg.generated 2018-07-23 16:44:07
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-07-23 16:44:07
     */
    private String symbol;

    /**
     * @mbg.generated 2018-07-23 16:44:07
     */
    private String contractType;

    /**
     * @mbg.generated 2018-07-23 16:44:07
     */
    private String contractCode;

    /**
     * @mbg.generated 2018-07-23 16:44:07
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-07-23 16:44:07
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}