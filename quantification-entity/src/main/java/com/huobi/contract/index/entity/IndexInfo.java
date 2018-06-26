package com.huobi.contract.index.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class IndexInfo implements Serializable {
    private static final long serialVersionUID = -822260835503872737L;
    private Long id;

    private String indexName;

    private String indexSymbol;

    private Short isValid;

    private String remark;

    private String inputBy;

    private Date inputTime;

    private String updator;

    private Date updateTime;

    private BigDecimal alarmThreshold;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName == null ? null : indexName.trim();
    }

    public String getIndexSymbol() {
        return indexSymbol;
    }

    public void setIndexSymbol(String indexSymbol) {
        this.indexSymbol = indexSymbol == null ? null : indexSymbol.trim();
    }

    public Short getIsValid() {
        return isValid;
    }

    public void setIsValid(Short isValid) {
        this.isValid = isValid;
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

    public BigDecimal getAlarmThreshold() {
        return alarmThreshold;
    }

    public void setAlarmThreshold(BigDecimal alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
    }
}