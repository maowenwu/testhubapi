package com.huobi.quantification.entity;

import java.util.Date;

public class QuanDepth {
    /**
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long id;

    /**
     * 交易所ID
     * @mbg.generated 2018-07-27 14:41:01
     */
    private int exchangeId;

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
     * 响应生成时间点
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Date depthTs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
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

    public Date getDepthTs() {
        return depthTs;
    }

    public void setDepthTs(Date depthTs) {
        this.depthTs = depthTs;
    }
}