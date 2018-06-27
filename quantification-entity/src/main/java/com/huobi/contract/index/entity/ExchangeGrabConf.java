package com.huobi.contract.index.entity;

import java.util.Date;

public class ExchangeGrabConf {
    private Long seqId;

    private Byte proxyType;

    private Long exchangeId;

    private Long grabIntervalTime;

    private Long ipUnfreezeTime;

    private Byte isValid;

    private String inputBy;

    private Date inputTime;

    private String updator;

    private Date updateTime;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public Byte getProxyType() {
        return proxyType;
    }

    public void setProxyType(Byte proxyType) {
        this.proxyType = proxyType;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getGrabIntervalTime() {
        return grabIntervalTime;
    }

    public void setGrabIntervalTime(Long grabIntervalTime) {
        this.grabIntervalTime = grabIntervalTime;
    }

    public Long getIpUnfreezeTime() {
        return ipUnfreezeTime;
    }

    public void setIpUnfreezeTime(Long ipUnfreezeTime) {
        this.ipUnfreezeTime = ipUnfreezeTime;
    }

    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    public String getInputBy() {
        return inputBy;
    }

    public void setInputBy(String inputBy) {
        this.inputBy = inputBy == null ? null : inputBy.trim();
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}