package com.huobi.quantification.entity;

import java.util.Date;

public class QuanDepthFuture {
    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long id;

    /**
     * 交易所ID
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long exchangeId;

    /**
     * 基础币种
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String baseCoin;

    /**
     * 定价币种
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String quoteCoin;

    /**
     * 合约代码
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String contractCode;

    /**
     * 合约名称
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String contractName;

    /**
     * 响应生成时间点
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Date depthTs;

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

    public Date getDepthTs() {
        return depthTs;
    }

    public void setDepthTs(Date depthTs) {
        this.depthTs = depthTs;
    }
}