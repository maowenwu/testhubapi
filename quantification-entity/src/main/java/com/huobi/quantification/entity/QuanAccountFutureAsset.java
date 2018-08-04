package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanAccountFutureAsset {
    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private Long id;

    /**
     * 账号ID
     * @mbg.generated 2018-08-04 10:24:20
     */
    private Long accountFutureId;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private Long queryId;

    /**
     * 币种
     * @mbg.generated 2018-08-04 10:24:20
     */
    private String coinType;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private BigDecimal marginBalance;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private BigDecimal marginPosition;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private BigDecimal marginFrozen;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private BigDecimal marginAvailable;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private BigDecimal profitReal;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private BigDecimal profitUnreal;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private BigDecimal riskRate;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private Integer init;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountFutureId() {
        return accountFutureId;
    }

    public void setAccountFutureId(Long accountFutureId) {
        this.accountFutureId = accountFutureId;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public BigDecimal getMarginBalance() {
        return marginBalance;
    }

    public void setMarginBalance(BigDecimal marginBalance) {
        this.marginBalance = marginBalance;
    }

    public BigDecimal getMarginPosition() {
        return marginPosition;
    }

    public void setMarginPosition(BigDecimal marginPosition) {
        this.marginPosition = marginPosition;
    }

    public BigDecimal getMarginFrozen() {
        return marginFrozen;
    }

    public void setMarginFrozen(BigDecimal marginFrozen) {
        this.marginFrozen = marginFrozen;
    }

    public BigDecimal getMarginAvailable() {
        return marginAvailable;
    }

    public void setMarginAvailable(BigDecimal marginAvailable) {
        this.marginAvailable = marginAvailable;
    }

    public BigDecimal getProfitReal() {
        return profitReal;
    }

    public void setProfitReal(BigDecimal profitReal) {
        this.profitReal = profitReal;
    }

    public BigDecimal getProfitUnreal() {
        return profitUnreal;
    }

    public void setProfitUnreal(BigDecimal profitUnreal) {
        this.profitUnreal = profitUnreal;
    }

    public BigDecimal getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(BigDecimal riskRate) {
        this.riskRate = riskRate;
    }

    public Integer getInit() {
        return init;
    }

    public void setInit(Integer init) {
        this.init = init;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}