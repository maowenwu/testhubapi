package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class StrategyRiskHistory {
    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private Integer id;

    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private String strategyName;

    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private Integer instanceConfigId;

    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private Long instanceId;

    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private String baseCoin;

    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private BigDecimal riskRate;

    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private BigDecimal netPosition;

    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private BigDecimal currProfit;

    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private BigDecimal totalProfit;

    /**
     * @mbg.generated 2018-08-18 11:38:00
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-08-18 11:38:00
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

    public Integer getInstanceConfigId() {
        return instanceConfigId;
    }

    public void setInstanceConfigId(Integer instanceConfigId) {
        this.instanceConfigId = instanceConfigId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
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