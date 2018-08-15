package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class StrategyRiskHistory {
    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private Integer id;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private String strategyName;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private Long strategyVersion;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private Long accountId;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private String baseCoin;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private BigDecimal riskRate;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private BigDecimal netPosition;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private BigDecimal currProfit;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private BigDecimal totalProfit;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-08-15 17:33:47
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Long getStrategyVersion() {
        return strategyVersion;
    }

    public void setStrategyVersion(Long strategyVersion) {
        this.strategyVersion = strategyVersion;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public BigDecimal getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(BigDecimal riskRate) {
        this.riskRate = riskRate;
    }

    public BigDecimal getNetPosition() {
        return netPosition;
    }

    public void setNetPosition(BigDecimal netPosition) {
        this.netPosition = netPosition;
    }

    public BigDecimal getCurrProfit() {
        return currProfit;
    }

    public void setCurrProfit(BigDecimal currProfit) {
        this.currProfit = currProfit;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
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