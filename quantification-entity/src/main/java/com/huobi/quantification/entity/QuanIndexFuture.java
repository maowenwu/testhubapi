package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanIndexFuture {
    /**
     * @mbg.generated 2018-07-12 17:16:43
     */
    private Long id;

    /**
     * @mbg.generated 2018-07-12 17:16:43
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-07-12 17:16:43
     */
    private String symbol;

    /**
     * @mbg.generated 2018-07-12 17:16:43
     */
    private BigDecimal futureIndex;

    /**
     * @mbg.generated 2018-07-12 17:16:43
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-07-12 17:16:43
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

    public BigDecimal getFutureIndex() {
        return futureIndex;
    }

    public void setFutureIndex(BigDecimal futureIndex) {
        this.futureIndex = futureIndex;
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