package com.huobi.quantification.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class QuanTickerFutures implements Serializable {
    private Long id;

    /**
     * 交易所ID
     */
    private Long exchangeId;

    /**
     * api请求时间
     */
    private Date ts;

    /**
     * 最新成交价
     */
    private Long lastPrice;

    /**
     * 买1价
     */
    private Long bidPrice;

    /**
     * 卖1价
     */
    private Long askPrice;

    /**
     * 基础币种
     */
    private String baseCoin;

    /**
     * 定价币种
     */
    private String quoteCoin;

    /**
     * 最高价
     */
    private Long highPrice;

    /**
     * 最低价
     */
    private Long lowPrice;

    /**
     * 合约代码
     */
    private String contractCode;

    /**
     * 合约名称
     */
    private String contractName;

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

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Long getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Long lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Long getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Long bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Long getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Long askPrice) {
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

    public Long getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Long highPrice) {
        this.highPrice = highPrice;
    }

    public Long getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Long lowPrice) {
        this.lowPrice = lowPrice;
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
}