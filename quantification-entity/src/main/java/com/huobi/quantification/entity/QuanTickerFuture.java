package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanTickerFuture {
    /**
     * @mbg.generated 2018-06-28 14:54:23
     */
    private Long id;

    /**
     * 交易所ID
     * @mbg.generated 2018-06-28 14:54:23
     */
    private Integer exchangeId;

    /**
     * 合约代码
     * @mbg.generated 2018-06-28 14:54:23
     */
    private String contractCode;

    /**
     * 合约名称
     * @mbg.generated 2018-06-28 14:54:23
     */
    private String contractName;

    /**
     * 最高价
     * @mbg.generated 2018-06-28 14:54:23
     */
    private BigDecimal highPrice;

    /**
     * 最低价
     * @mbg.generated 2018-06-28 14:54:23
     */
    private BigDecimal lowPrice;

    /**
     * 最新成交价
     * @mbg.generated 2018-06-28 14:54:23
     */
    private BigDecimal lastPrice;

    /**
     * 卖1价
     * @mbg.generated 2018-06-28 14:54:23
     */
    private BigDecimal askPrice;

    /**
     * 买1价
     * @mbg.generated 2018-06-28 14:54:23
     */
    private BigDecimal bidPrice;

    /**
     * 基础币种
     * @mbg.generated 2018-06-28 14:54:23
     */
    private String baseCoin;

    /**
     * 定价币种
     * @mbg.generated 2018-06-28 14:54:23
     */
    private String quoteCoin;

    /**
     * api请求时间
     * @mbg.generated 2018-06-28 14:54:23
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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
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

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
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