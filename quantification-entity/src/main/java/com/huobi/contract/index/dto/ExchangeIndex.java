package com.huobi.contract.index.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExchangeIndex implements Serializable {

    private static final long serialVersionUID = 8549039325269272675L;

    private Long exchangeId;
    private String fullName;
    private String shortName;
    private Short isValid;
    private int httpQualified;
    private int wsQualified;
    private String sourceSymbol;
    private String exchangeSymbol;
    private BigDecimal originalWeight;
    private BigDecimal weight;
    private String indexSymbol;
    private Short indexIsValid;

    public ExchangeIndex() {

    }

    public ExchangeIndex(Long exchangeId, String fullName, String shortName, Short isValid, int httpQualified, int wsQualified, String sourceSymbol, String exchangeSymbol, BigDecimal originalWeight, BigDecimal weight, String indexSymbol, Short indexIsValid) {
        this.exchangeId = exchangeId;
        this.fullName = fullName;
        this.shortName = shortName;
        this.isValid = isValid;
        this.httpQualified = httpQualified;
        this.wsQualified = wsQualified;
        this.sourceSymbol = sourceSymbol;
        this.exchangeSymbol = exchangeSymbol;
        this.originalWeight = originalWeight;
        this.weight = weight;
        this.indexSymbol = indexSymbol;
        this.indexIsValid = indexIsValid;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Short getIsValid() {
        return isValid;
    }

    public void setIsValid(Short isValid) {
        this.isValid = isValid;
    }

    public int getHttpQualified() {
        return httpQualified;
    }

    public void setHttpQualified(int httpQualified) {
        this.httpQualified = httpQualified;
    }

    public int getWsQualified() {
        return wsQualified;
    }

    public void setWsQualified(int wsQualified) {
        this.wsQualified = wsQualified;
    }

    public String getSourceSymbol() {
        return sourceSymbol;
    }

    public void setSourceSymbol(String sourceSymbol) {
        this.sourceSymbol = sourceSymbol;
    }

    public String getExchangeSymbol() {
        return exchangeSymbol;
    }

    public void setExchangeSymbol(String exchangeSymbol) {
        this.exchangeSymbol = exchangeSymbol;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getIndexSymbol() {
        return indexSymbol;
    }

    public void setIndexSymbol(String indexSymbol) {
        this.indexSymbol = indexSymbol;
    }

    public Short getIndexIsValid() {
        return indexIsValid;
    }

    public void setIndexIsValid(Short indexIsValid) {
        this.indexIsValid = indexIsValid;
    }

    public BigDecimal getOriginalWeight() {
        return originalWeight;
    }

    public void setOriginalWeight(BigDecimal originalWeight) {
        this.originalWeight = originalWeight;
    }

    @Override
    public String toString() {
        return "ExchangeIndex{" +
                "exchangeId=" + exchangeId +
                ", fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", isValid=" + isValid +
                ", httpQualified=" + httpQualified +
                ", wsQualified=" + wsQualified +
                ", sourceSymbol='" + sourceSymbol + '\'' +
                ", exchangeSymbol='" + exchangeSymbol + '\'' +
                ", originalWeight=" + originalWeight +
                ", weight=" + weight +
                ", indexSymbol='" + indexSymbol + '\'' +
                ", indexIsValid=" + indexIsValid +
                '}';
    }
}
