package com.huobi.contract.index.entity;

import java.util.Date;

public class IpUsageRecord {
    private Long seqId;

    private Long exchangeId;

    private String ipAddr;

    private String reqUrl;

    private int status;

    private int isIpUnfreeze;

    private Integer statusCode;

    private String errorMsg;

    private String remark;

    private Date inputTime;
    public IpUsageRecord(){}
    public IpUsageRecord(Long exchangeId, String ipAddr, String reqUrl, int status, int isIpUnfreeze, Integer statusCode, String errorMsg, Date inputTime) {
        this.exchangeId = exchangeId;
        this.ipAddr = ipAddr;
        this.reqUrl = reqUrl;
        this.status = status;
        this.isIpUnfreeze = isIpUnfreeze;
        this.statusCode = statusCode;
        this.errorMsg = errorMsg;
        this.inputTime = inputTime;
    }

    public IpUsageRecord(Long exchangeId, String ipAddr, String reqUrl, int status, int isIpUnfreeze, Integer statusCode, String errorMsg, String remark, Date inputTime) {
        this.exchangeId = exchangeId;
        this.ipAddr = ipAddr;
        this.reqUrl = reqUrl;
        this.status = status;
        this.isIpUnfreeze = isIpUnfreeze;
        this.statusCode = statusCode;
        this.errorMsg = errorMsg;
        this.remark = remark;
        this.inputTime = inputTime;
    }

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl == null ? null : reqUrl.trim();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsIpUnfreeze() {
        return isIpUnfreeze;
    }

    public void setIsIpUnfreeze(int isIpUnfreeze) {
        this.isIpUnfreeze = isIpUnfreeze;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }
}