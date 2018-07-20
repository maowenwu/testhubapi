package com.huobi.quantification.dto;

import java.io.Serializable;

public class FutureJobReqDto implements Serializable {

    private int exchangeId;
    private int jobType;
    private JobParamDto jobParamDto;
    private String jobDesc;
    private String cron;
    private int state;


    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public JobParamDto getJobParamDto() {
        return jobParamDto;
    }

    public void setJobParamDto(JobParamDto jobParamDto) {
        this.jobParamDto = jobParamDto;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
