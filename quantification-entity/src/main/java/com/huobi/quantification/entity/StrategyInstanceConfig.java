package com.huobi.quantification.entity;

import java.util.Date;

public class StrategyInstanceConfig {
    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Integer id;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private String strategyName;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Long instanceId;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Integer instanceEnable;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Integer orderThreadEnable;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Integer hedgeThreadEnable;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Integer riskThreadEnable;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private String instanceGroup;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Integer futureExchangeId;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Long futureAccountId;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private String futureBaseCoin;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private String futureQuotCoin;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private String futureContractCode;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Integer futureLever;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Integer spotExchangeId;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Long spotAccountId;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private String spotBaseCoin;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private String spotQuotCoin;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private String spotDepthType;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private String futureDepthType;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Date instanceHeartbeat;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Date orderThreadHeartbeat;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Date hedgeThreadHeartbeat;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Date riskThreadHeartbeat;

    /**
     * @mbg.generated 2018-08-21 09:30:22
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-08-21 09:30:22
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

    public Integer getInstanceEnable() {
        return instanceEnable;
    }

    public void setInstanceEnable(Integer instanceEnable) {
        this.instanceEnable = instanceEnable;
    }

    public Integer getOrderThreadEnable() {
        return orderThreadEnable;
    }

    public void setOrderThreadEnable(Integer orderThreadEnable) {
        this.orderThreadEnable = orderThreadEnable;
    }

    public Integer getHedgeThreadEnable() {
        return hedgeThreadEnable;
    }

    public void setHedgeThreadEnable(Integer hedgeThreadEnable) {
        this.hedgeThreadEnable = hedgeThreadEnable;
    }

    public Integer getRiskThreadEnable() {
        return riskThreadEnable;
    }

    public void setRiskThreadEnable(Integer riskThreadEnable) {
        this.riskThreadEnable = riskThreadEnable;
    }

    public String getInstanceGroup() {
        return instanceGroup;
    }

    public void setInstanceGroup(String instanceGroup) {
        this.instanceGroup = instanceGroup;
    }

    public Integer getFutureExchangeId() {
        return futureExchangeId;
    }

    public void setFutureExchangeId(Integer futureExchangeId) {
        this.futureExchangeId = futureExchangeId;
    }

    public Long getFutureAccountId() {
        return futureAccountId;
    }

    public void setFutureAccountId(Long futureAccountId) {
        this.futureAccountId = futureAccountId;
    }

    public String getFutureBaseCoin() {
        return futureBaseCoin;
    }

    public void setFutureBaseCoin(String futureBaseCoin) {
        this.futureBaseCoin = futureBaseCoin;
    }

    public String getFutureQuotCoin() {
        return futureQuotCoin;
    }

    public void setFutureQuotCoin(String futureQuotCoin) {
        this.futureQuotCoin = futureQuotCoin;
    }

    public String getFutureContractCode() {
        return futureContractCode;
    }

    public void setFutureContractCode(String futureContractCode) {
        this.futureContractCode = futureContractCode;
    }

    public Integer getFutureLever() {
        return futureLever;
    }

    public void setFutureLever(Integer futureLever) {
        this.futureLever = futureLever;
    }

    public Integer getSpotExchangeId() {
        return spotExchangeId;
    }

    public void setSpotExchangeId(Integer spotExchangeId) {
        this.spotExchangeId = spotExchangeId;
    }

    public Long getSpotAccountId() {
        return spotAccountId;
    }

    public void setSpotAccountId(Long spotAccountId) {
        this.spotAccountId = spotAccountId;
    }

    public String getSpotBaseCoin() {
        return spotBaseCoin;
    }

    public void setSpotBaseCoin(String spotBaseCoin) {
        this.spotBaseCoin = spotBaseCoin;
    }

    public String getSpotQuotCoin() {
        return spotQuotCoin;
    }

    public void setSpotQuotCoin(String spotQuotCoin) {
        this.spotQuotCoin = spotQuotCoin;
    }

    public String getSpotDepthType() {
        return spotDepthType;
    }

    public void setSpotDepthType(String spotDepthType) {
        this.spotDepthType = spotDepthType;
    }

    public String getFutureDepthType() {
        return futureDepthType;
    }

    public void setFutureDepthType(String futureDepthType) {
        this.futureDepthType = futureDepthType;
    }

    public Date getInstanceHeartbeat() {
        return instanceHeartbeat;
    }

    public void setInstanceHeartbeat(Date instanceHeartbeat) {
        this.instanceHeartbeat = instanceHeartbeat;
    }

    public Date getOrderThreadHeartbeat() {
        return orderThreadHeartbeat;
    }

    public void setOrderThreadHeartbeat(Date orderThreadHeartbeat) {
        this.orderThreadHeartbeat = orderThreadHeartbeat;
    }

    public Date getHedgeThreadHeartbeat() {
        return hedgeThreadHeartbeat;
    }

    public void setHedgeThreadHeartbeat(Date hedgeThreadHeartbeat) {
        this.hedgeThreadHeartbeat = hedgeThreadHeartbeat;
    }

    public Date getRiskThreadHeartbeat() {
        return riskThreadHeartbeat;
    }

    public void setRiskThreadHeartbeat(Date riskThreadHeartbeat) {
        this.riskThreadHeartbeat = riskThreadHeartbeat;
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