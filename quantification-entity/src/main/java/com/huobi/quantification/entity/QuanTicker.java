package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanTicker {
    /**
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long id;

    /**
     * 交易所ID
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Integer exchangeId;

    /**
     * 最高价
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal highPrice;

    /**
     * 最低价
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal lowPrice;

    /**
     * 最新成交价
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal lastPrice;

    /**
     * 买1价
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal bidPrice;

    /**
     * 卖1价
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal askPrice;

    /**
     * 基础币种
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String baseCoin;

    /**
     * 定价币种
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String quoteCoin;

    /**
     * api请求时间
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Date ts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public String getQuoteCoin() {
        return quoteCoin;
    }

    public void setQuoteCoin(String quoteCoin) {
        this.quoteCoin = quoteCoin;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}