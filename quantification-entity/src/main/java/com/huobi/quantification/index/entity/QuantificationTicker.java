package com.huobi.quantification.index.entity;

import java.io.Serializable;
import java.util.Date;

public class QuantificationTicker implements Serializable {
    private Long id;

    private Long tickerExchangeId;

    private String tickerStatus;

    private Long tickerTs;

    private String tickerCh;

    private Long tickId;

    private Double tickerAmount;

    private Double tickerCount;

    private Long tickerOpen;

    private Long tickerClose;

    private Long tickerLow;

    private Long tickerHigh;

    private Double tickerVol;

    private Long tickerBidPrice;

    private Double tickerBidAmount;

    private Long tickerAskPrice;

    private Double tickerAskAmount;

    private Date dateUpdate;

    private String tickerType;

    private String tickerSymbol;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTickerExchangeId() {
        return tickerExchangeId;
    }

    public void setTickerExchangeId(Long tickerExchangeId) {
        this.tickerExchangeId = tickerExchangeId;
    }

    public String getTickerStatus() {
        return tickerStatus;
    }

    public void setTickerStatus(String tickerStatus) {
        this.tickerStatus = tickerStatus == null ? null : tickerStatus.trim();
    }

    public Long getTickerTs() {
        return tickerTs;
    }

    public void setTickerTs(Long tickerTs) {
        this.tickerTs = tickerTs;
    }

    public String getTickerCh() {
        return tickerCh;
    }

    public void setTickerCh(String tickerCh) {
        this.tickerCh = tickerCh == null ? null : tickerCh.trim();
    }

    public Long getTickId() {
        return tickId;
    }

    public void setTickId(Long tickId) {
        this.tickId = tickId;
    }

    public Double getTickerAmount() {
        return tickerAmount;
    }

    public void setTickerAmount(Double tickerAmount) {
        this.tickerAmount = tickerAmount;
    }

    public Double getTickerCount() {
        return tickerCount;
    }

    public void setTickerCount(Double tickerCount) {
        this.tickerCount = tickerCount;
    }

    public Long getTickerOpen() {
        return tickerOpen;
    }

    public void setTickerOpen(Long tickerOpen) {
        this.tickerOpen = tickerOpen;
    }

    public Long getTickerClose() {
        return tickerClose;
    }

    public void setTickerClose(Long tickerClose) {
        this.tickerClose = tickerClose;
    }

    public Long getTickerLow() {
        return tickerLow;
    }

    public void setTickerLow(Long tickerLow) {
        this.tickerLow = tickerLow;
    }

    public Long getTickerHigh() {
        return tickerHigh;
    }

    public void setTickerHigh(Long tickerHigh) {
        this.tickerHigh = tickerHigh;
    }

    public Double getTickerVol() {
        return tickerVol;
    }

    public void setTickerVol(Double tickerVol) {
        this.tickerVol = tickerVol;
    }

    public Long getTickerBidPrice() {
        return tickerBidPrice;
    }

    public void setTickerBidPrice(Long tickerBidPrice) {
        this.tickerBidPrice = tickerBidPrice;
    }

    public Double getTickerBidAmount() {
        return tickerBidAmount;
    }

    public void setTickerBidAmount(Double tickerBidAmount) {
        this.tickerBidAmount = tickerBidAmount;
    }

    public Long getTickerAskPrice() {
        return tickerAskPrice;
    }

    public void setTickerAskPrice(Long tickerAskPrice) {
        this.tickerAskPrice = tickerAskPrice;
    }

    public Double getTickerAskAmount() {
        return tickerAskAmount;
    }

    public void setTickerAskAmount(Double tickerAskAmount) {
        this.tickerAskAmount = tickerAskAmount;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getTickerType() {
        return tickerType;
    }

    public void setTickerType(String tickerType) {
        this.tickerType = tickerType == null ? null : tickerType.trim();
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol == null ? null : tickerSymbol.trim();
    }
}