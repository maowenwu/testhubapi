package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanAccountFuturePosition {
    /**
     * @mbg.generated 2018-08-21 16:38:31
     */
    private Long id;

    /**
     * 账号ID
     * @mbg.generated 2018-08-21 16:38:31
     */
    private Long accountFutureId;

    /**
     * @mbg.generated 2018-08-21 16:38:31
     */
    private Long queryId;

    /**
     * @mbg.generated 2018-08-21 16:38:31
     */
    private String baseCoin;

    /**
     * @mbg.generated 2018-08-21 16:38:31
     */
    private String quoteCoin;

    /**
     * @mbg.generated 2018-08-21 16:38:31
     */
    private String contractType;

    /**
     * @mbg.generated 2018-08-21 16:38:31
     */
    private String contractCode;

    /**
     * 1 多仓，2 空仓
     * @mbg.generated 2018-08-21 16:38:31
     */
    private Integer side;

    /**
     * 张数
     * @mbg.generated 2018-08-21 16:38:31
     */
    private BigDecimal amount;

    /**
     * 可平张数
     * @mbg.generated 2018-08-21 16:38:31
     */
    private BigDecimal available;

    /**
     * 冻结张数
     * @mbg.generated 2018-08-21 16:38:31
     */
    private BigDecimal frozen;

    /**
     * 开仓均价
     * @mbg.generated 2018-08-21 16:38:31
     */
    private BigDecimal costOpen;

    /**
     * 持仓均价
     * @mbg.generated 2018-08-21 16:38:31
     */
    private BigDecimal costHold;

    /**
     * 未实现盈亏
     * @mbg.generated 2018-08-21 16:38:31
     */
    private BigDecimal profitUnreal;

    /**
     * 收益率
     * @mbg.generated 2018-08-21 16:38:31
     */
    private BigDecimal profitRate;

    /**
     * 杠杆倍数
     * @mbg.generated 2018-08-21 16:38:31
     */
    private BigDecimal leverRate;

    /**
     * @mbg.generated 2018-08-21 16:38:31
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-08-21 16:38:31
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

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getSide() {
        return side;
    }

    public void setSide(Integer side) {
        this.side = side;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen;
    }

    public BigDecimal getCostOpen() {
        return costOpen;
    }

    public void setCostOpen(BigDecimal costOpen) {
        this.costOpen = costOpen;
    }

    public BigDecimal getCostHold() {
        return costHold;
    }

    public void setCostHold(BigDecimal costHold) {
        this.costHold = costHold;
    }

    public BigDecimal getProfitUnreal() {
        return profitUnreal;
    }

    public void setProfitUnreal(BigDecimal profitUnreal) {
        this.profitUnreal = profitUnreal;
    }

    public BigDecimal getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }

    public BigDecimal getLeverRate() {
        return leverRate;
    }

    public void setLeverRate(BigDecimal leverRate) {
        this.leverRate = leverRate;
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