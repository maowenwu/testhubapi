package com.huobi.quantification.entity;

import java.util.Date;

public class QuanJobFuture {
    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private Integer id;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private Integer jobType;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private String jobName;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private Long accountId;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private String symbol;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private String contractType;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private String ipId;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private String cron;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private Integer state;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private Date updateDate;

    /**
     * @mbg.generated 2018-07-04 17:21:27
     */
    private Date createDate;

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

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public String getIpId() {
        return ipId;
    }

    public void setIpId(String ipId) {
        this.ipId = ipId;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}