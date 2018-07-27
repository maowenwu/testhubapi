package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanKline {
    /**
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long id;

    /**
     * 交易所id
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Integer exchangeId;

    /**
     * 交易对
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String symbol;

    /**
     * k线类型
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String period;

    /**
     * 数量
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long size;

    /**
     * 最高价
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal high;

    /**
     * 开盘价
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal open;

    /**
     * 最低价
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal low;

    /**
     * 收盘价,当K线为最晚的一根时，是最新成交价
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal close;

    /**
     * 成交笔数
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal count;

    /**
     * 成交量
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal amount;

    /**
     * 成交额
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal vol;

    /**
     * 响应时间
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}