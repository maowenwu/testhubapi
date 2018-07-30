package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanAccountHistory {
    /**
     * @mbg.generated 2018-07-30 15:18:20
     */
    private Long id;

    /**
     * 账号ID
     * @mbg.generated 2018-07-30 15:18:20
     */
    private Long accountId;

    /**
     * 币种
     * @mbg.generated 2018-07-30 15:18:20
     */
    private String coin;

    /**
     * 当前金额
     * @mbg.generated 2018-07-30 15:18:20
     */
    private BigDecimal currentAmount;

    /**
     * 上次金额
     * @mbg.generated 2018-07-30 15:18:20
     */
    private BigDecimal lastAmount;

    /**
     * 充值提现金额
     * @mbg.generated 2018-07-30 15:18:20
     */
    private BigDecimal rechargeAmount;

    /**
     * 1.充值  -1. 提现
     * @mbg.generated 2018-07-30 15:18:20
     */
    private String rechargeType;

    /**
     * 创建时间
     * @mbg.generated 2018-07-30 15:18:20
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BigDecimal getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(BigDecimal lastAmount) {
        this.lastAmount = lastAmount;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}