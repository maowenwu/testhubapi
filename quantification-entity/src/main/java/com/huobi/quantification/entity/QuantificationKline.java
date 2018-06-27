package com.huobi.quantification.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class QuantificationKline implements Serializable {
    private Long id;

    private Long klineExchangeId;

    private String klineSymbol;

    private String klinePeriod;

    private Long klineSize;

    private Long klineTickId;

    private Date klineTs;

    private Double klineAmount;

    private Double klineCount;

    private BigDecimal klineOpen;

    private BigDecimal klineClose;

    private BigDecimal klineLow;

    private BigDecimal klineHigh;

    private BigDecimal klineVol;

    private String klineType;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKlineExchangeId() {
        return klineExchangeId;
    }

    public void setKlineExchangeId(Long klineExchangeId) {
        this.klineExchangeId = klineExchangeId;
    }

    public String getKlineSymbol() {
        return klineSymbol;
    }

    public void setKlineSymbol(String klineSymbol) {
        this.klineSymbol = klineSymbol == null ? null : klineSymbol.trim();
    }

    public String getKlinePeriod() {
        return klinePeriod;
    }

    public void setKlinePeriod(String klinePeriod) {
        this.klinePeriod = klinePeriod == null ? null : klinePeriod.trim();
    }

    public Long getKlineSize() {
        return klineSize;
    }

    public void setKlineSize(Long klineSize) {
        this.klineSize = klineSize;
    }

    public Long getKlineTickId() {
        return klineTickId;
    }

    public void setKlineTickId(Long klineTickId) {
        this.klineTickId = klineTickId;
    }

    public Date getKlineTs() {
        return klineTs;
    }

    public void setKlineTs(Date klineTs) {
        this.klineTs = klineTs;
    }

    public Double getKlineAmount() {
        return klineAmount;
    }

    public void setKlineAmount(Double klineAmount) {
        this.klineAmount = klineAmount;
    }

    public Double getKlineCount() {
        return klineCount;
    }

    public void setKlineCount(Double klineCount) {
        this.klineCount = klineCount;
    }

    public BigDecimal getKlineOpen() {
        return klineOpen;
    }

    public void setKlineOpen(BigDecimal klineOpen) {
        this.klineOpen = klineOpen;
    }

    public BigDecimal getKlineClose() {
        return klineClose;
    }

    public void setKlineClose(BigDecimal klineClose) {
        this.klineClose = klineClose;
    }

    public BigDecimal getKlineLow() {
        return klineLow;
    }

    public void setKlineLow(BigDecimal klineLow) {
        this.klineLow = klineLow;
    }

    public BigDecimal getKlineHigh() {
        return klineHigh;
    }

    public void setKlineHigh(BigDecimal klineHigh) {
        this.klineHigh = klineHigh;
    }

    public BigDecimal getKlineVol() {
        return klineVol;
    }

    public void setKlineVol(BigDecimal klineVol) {
        this.klineVol = klineVol;
    }

    public String getKlineType() {
        return klineType;
    }

    public void setKlineType(String klineType) {
        this.klineType = klineType == null ? null : klineType.trim();
    }
}