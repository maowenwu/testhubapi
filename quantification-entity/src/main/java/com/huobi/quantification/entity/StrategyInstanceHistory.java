package com.huobi.quantification.entity;

import java.util.Date;

public class StrategyInstanceHistory {
    /**
     * @mbg.generated 2018-08-17 14:20:16
     */
    private Integer id;

    /**
     * @mbg.generated 2018-08-17 14:20:16
     */
    private String strategyName;

    /**
     * @mbg.generated 2018-08-17 14:20:16
     */
    private Long instanceId;

    /**
     * @mbg.generated 2018-08-17 14:20:16
     */
    private String baseCoin;

    /**
     * @mbg.generated 2018-08-17 14:20:16
     */
    private String contractCode;

    /**
     * @mbg.generated 2018-08-17 14:20:16
     */
    private Date instanceStartupTime;

    /**
     * @mbg.generated 2018-08-17 14:20:16
     */
    private Date instanceStopTime;

    /**
     * @mbg.generated 2018-08-17 14:20:16
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-08-17 14:20:16
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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getInstanceStartupTime() {
        return instanceStartupTime;
    }

    public void setInstanceStartupTime(Date instanceStartupTime) {
        this.instanceStartupTime = instanceStartupTime;
    }

    public Date getInstanceStopTime() {
        return instanceStopTime;
    }

    public void setInstanceStopTime(Date instanceStopTime) {
        this.instanceStopTime = instanceStopTime;
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