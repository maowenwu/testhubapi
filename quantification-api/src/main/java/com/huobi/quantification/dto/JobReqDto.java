package com.huobi.quantification.dto;

import java.io.Serializable;

public class JobReqDto implements Serializable {

    private int exchangeId;
    private int jobType;
    private JobParamDto jobParamDto;
    private  String cron;

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
}

