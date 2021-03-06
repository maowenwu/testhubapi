package com.huobi.quantification.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class QuanDepth implements Serializable {
    private Long id;

    /**
     * 交易所ID
     */
    private Long exchangeId;

    /**
     * 响应生成时间点
     */
    private Date depthTs;

    /**
     * 基础币种
     */
    private String baseCoin;

    /**
     * 定价币种
     */
    private String quoteCoin;

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

    public Date getDepthTs() {
        return depthTs;
    }

    public void setDepthTs(Date depthTs) {
        this.depthTs = depthTs;
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
}