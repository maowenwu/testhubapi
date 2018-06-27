package com.huobi.quantification.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class QuanTicker implements Serializable {
    private Long id;

    private Long exchangeId;

    private String status;

    private Long ts;

    private String ch;

    private Long tickId;

    private Double amount;

    private Double count;

    private Long open;

    private Long close;

    private Long low;

    private Long high;

    private Double vol;

    private Long bidPrice;

    private Double bidAmount;

    private Long askPrice;

    private Double askAmount;

    private Date dateUpdate;

    private String tickerType;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public Long getTickId() {
        return tickId;
    }

    public void setTickId(Long tickId) {
        this.tickId = tickId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Long getOpen() {
        return open;
    }

    public void setOpen(Long open) {
        this.open = open;
    }

    public Long getClose() {
        return close;
    }

    public void setClose(Long close) {
        this.close = close;
    }

    public Long getLow() {
        return low;
    }

    public void setLow(Long low) {
        this.low = low;
    }

    public Long getHigh() {
        return high;
    }

    public void setHigh(Long high) {
        this.high = high;
    }

    public Double getVol() {
        return vol;
    }

    public void setVol(Double vol) {
        this.vol = vol;
    }

    public Long getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Long bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Long getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Long askPrice) {
        this.askPrice = askPrice;
    }

    public Double getAskAmount() {
        return askAmount;
    }

    public void setAskAmount(Double askAmount) {
        this.askAmount = askAmount;
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
        this.tickerType = tickerType;
    }
}