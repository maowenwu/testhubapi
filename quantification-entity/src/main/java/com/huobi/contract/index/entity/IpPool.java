package com.huobi.contract.index.entity;

import java.io.Serializable;
import java.util.Date;

public class IpPool implements Serializable {

    private static final long serialVersionUID = 1028073172997015707L;

    private Long seqId;

    private String poolName;

    private Byte proxyType;

    private String ip;

    private Integer port;

    private Byte isValid;

    private String userName;

    private String passwd;

    private String remark;

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

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName == null ? null : poolName.trim();
    }

    public Byte getProxyType() {
        return proxyType;
    }

    public void setProxyType(Byte proxyType) {
        this.proxyType = proxyType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    @Override
    public String toString() {
        return "IpPool{" +
                "seqId=" + seqId +
                ", poolName='" + poolName + '\'' +
                ", proxyType=" + proxyType +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", isValid=" + isValid +
                ", userName='" + userName + '\'' +
                ", passwd='" + passwd + '\'' +
                ", remark='" + remark + '\'' +
                ", inputBy='" + inputBy + '\'' +
                ", inputTime=" + inputTime +
                ", updator='" + updator + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}