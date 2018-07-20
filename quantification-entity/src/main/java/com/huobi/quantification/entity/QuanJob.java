package com.huobi.quantification.entity;

import java.util.Date;

public class QuanJob {
	 /**
     * @mbg.generated 2018-07-20 14:45:57
     */
    private Integer id;

    /**
     * @mbg.generated 2018-07-20 14:45:57
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-07-20 14:45:57
     */
    private Integer jobType;

    /**
     * @mbg.generated 2018-07-20 14:45:57
     */
    private String jobName;

    /**
     * @mbg.generated 2018-07-20 14:45:57
     */
    private String jobParam;

    /**
     * @mbg.generated 2018-07-20 14:45:57
     */
    private String jobDesc;

    /**
     * @mbg.generated 2018-07-20 14:45:57
     */
    private String cron;

    /**
     * @mbg.generated 2018-07-20 14:45:57
     */
    private Integer state;

    /**
     * @mbg.generated 2018-07-20 14:45:57
     */
    private Date updateDate;

    /**
     * @mbg.generated 2018-07-20 14:45:57
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

    public String getJobParam() {
        return jobParam;
    }

    public void setJobParam(String jobParam) {
        this.jobParam = jobParam;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
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