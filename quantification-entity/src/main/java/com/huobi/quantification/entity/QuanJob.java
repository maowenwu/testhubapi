package com.huobi.quantification.entity;

import java.util.Date;

public class QuanJob {
    /**
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Integer id;

    /**
     * 交易所id
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Integer exchangeId;

    /**
     * 任务类型
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Integer jobType;

    /**
     * 任务名
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String jobName;

    /**
     * 任务所需参数
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String jobParam;

    /**
     * 任务描述
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String jobDesc;

    /**
     * cron表达式
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String cron;

    /**
     * 状态
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Integer state;

    /**
     * 更新时间
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Date updateDate;

    /**
     * 创建时间
     * @mbg.generated 2018-07-27 14:41:01
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