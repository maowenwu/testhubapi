package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanExchangeConfig{
    /**
     * @mbg.generated 2018-08-03 10:44:12
     */
    private Integer id;

    /**
     * @mbg.generated 2018-08-03 10:44:12
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-08-03 10:44:12
     */
    private String baseCoin;

    /**
     * @mbg.generated 2018-08-03 10:44:12
     */
    private String quoteCoin;

    /**
     * @mbg.generated 2018-08-03 10:44:12
     */
    private Integer pricePrecision;

    /**
     * @mbg.generated 2018-08-03 10:44:12
     */
    private Integer amountPrecision;

    /**
     * @mbg.generated 2018-08-03 10:44:12
     */
    private BigDecimal faceValue;

    /**
     * @mbg.generated 2018-08-03 10:44:12
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-08-03 10:44:12
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

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public String getQuoteCoin() {
        return quoteCoin;
    }

    public void setQuoteCoin(String quoteCoin) {
        this.quoteCoin = quoteCoin;
    }

    public Integer getPricePrecision() {
        return pricePrecision;
    }

    public void setPricePrecision(Integer pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    public Integer getAmountPrecision() {
        return amountPrecision;
    }

    public void setAmountPrecision(Integer amountPrecision) {
        this.amountPrecision = amountPrecision;
    }

    public BigDecimal getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
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