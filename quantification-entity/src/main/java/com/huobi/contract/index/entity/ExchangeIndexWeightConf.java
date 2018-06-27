package com.huobi.contract.index.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExchangeIndexWeightConf implements Serializable {
    private static final long serialVersionUID = 4898661252862958848L;
    private Long weightId;

    private Long exchangeId;

    private String indexSymbol;

    private String sourceSymbol;

    private String exchangeSymbol;

    private BigDecimal weight;

    private Integer httpQualified;

    private Integer wsQualified;

    private String inputBy;

    private Date inputTime;

    private String updator;

    private Date updateTime;

    public Long getWeightId() {
        return weightId;
    }

    public void setWeightId(Long weightId) {
        this.weightId = weightId;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getIndexSymbol() {
        return indexSymbol;
    }

    public void setIndexSymbol(String indexSymbol) {
        this.indexSymbol = indexSymbol == null ? null : indexSymbol.trim();
    }

    public String getSourceSymbol() {
        return sourceSymbol;
    }

    public void setSourceSymbol(String sourceSymbol) {
        this.sourceSymbol = sourceSymbol == null ? null : sourceSymbol.trim();
    }

    public String getExchangeSymbol() {
        return exchangeSymbol;
    }

    public void setExchangeSymbol(String exchangeSymbol) {
        this.exchangeSymbol = exchangeSymbol == null ? null : exchangeSymbol.trim();
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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

    public Integer getHttpQualified() {
        return httpQualified;
    }

    public void setHttpQualified(Integer httpQualified) {
        this.httpQualified = httpQualified;
    }

    public Integer getWsQualified() {
        return wsQualified;
    }

    public void setWsQualified(Integer wsQualified) {
        this.wsQualified = wsQualified;
    }

    @Override
    public String toString() {
        return "ExchangeIndexWeightConf{" +
                "weightId=" + weightId +
                ", exchangeId=" + exchangeId +
                ", indexSymbol='" + indexSymbol + '\'' +
                ", sourceSymbol='" + sourceSymbol + '\'' +
                ", exchangeSymbol='" + exchangeSymbol + '\'' +
                ", weight=" + weight +
                ", httpQualified=" + httpQualified +
                ", wsQualified=" + wsQualified +
                ", inputBy='" + inputBy + '\'' +
                ", inputTime=" + inputTime +
                ", updator='" + updator + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}