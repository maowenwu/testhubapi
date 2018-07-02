package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class QuanAccountFutureAsset {
    /**
     * @mbg.generated 2018-07-02 14:36:55
     */
    private Long id;

    /**
     * 账号ID
     * @mbg.generated 2018-07-02 14:36:55
     */
    private Long accountSourceId;

    /**
     * @mbg.generated 2018-07-02 14:36:55
     */
    private Long queryId;

    /**
     * 币种
     * @mbg.generated 2018-07-02 14:36:55
     */
    private String symbol;

    /**
     * 账户权益
     * @mbg.generated 2018-07-02 14:36:55
     */
    private BigDecimal riskRate;

    /**
     * 交易所服务器时间
     * @mbg.generated 2018-07-02 14:36:55
     */
    private BigDecimal accountRights;

    /**
     * api请求时间
     * @mbg.generated 2018-07-02 14:36:55
     */
    private BigDecimal profitUnreal;

    /**
     * @mbg.generated 2018-07-02 14:36:55
     */
    private BigDecimal profitReal;

    /**
     * @mbg.generated 2018-07-02 14:36:55
     */
    private BigDecimal keepDeposit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountSourceId() {
        return accountSourceId;
    }

    public void setAccountSourceId(Long accountSourceId) {
        this.accountSourceId = accountSourceId;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(BigDecimal riskRate) {
        this.riskRate = riskRate;
    }

    public BigDecimal getAccountRights() {
        return accountRights;
    }

    public void setAccountRights(BigDecimal accountRights) {
        this.accountRights = accountRights;
    }

    public BigDecimal getProfitUnreal() {
        return profitUnreal;
    }

    public void setProfitUnreal(BigDecimal profitUnreal) {
        this.profitUnreal = profitUnreal;
    }

    public BigDecimal getProfitReal() {
        return profitReal;
    }

    public void setProfitReal(BigDecimal profitReal) {
        this.profitReal = profitReal;
    }

    public BigDecimal getKeepDeposit() {
        return keepDeposit;
    }

    public void setKeepDeposit(BigDecimal keepDeposit) {
        this.keepDeposit = keepDeposit;
    }
}