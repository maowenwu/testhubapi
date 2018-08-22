package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class StrategyFinanceHistory {
    /**
     * @mbg.generated 2018-08-22 15:09:17
     */
    private Long id;

    /**
     * 交易所id
     * @mbg.generated 2018-08-22 15:09:17
     */
    private Integer exchangeId;

    /**
     * 账号ID
     * @mbg.generated 2018-08-22 15:09:17
     */
    private Long accountSourceId;

    /**
     * 币种
     * @mbg.generated 2018-08-22 15:09:17
     */
    private String coinType;

    /**
     * 充值提现金额
     * @mbg.generated 2018-08-22 15:09:17
     */
    private BigDecimal transferAmount;

    /**
     * 1.充值  2. 提现
     * @mbg.generated 2018-08-22 15:09:17
     */
    private Integer moneyType;

    /**
     * @mbg.generated 2018-08-22 15:09:17
     */
    private Integer init;

    /**
     * 创建时间
     * @mbg.generated 2018-08-22 15:09:17
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-08-22 15:09:17
     */
    private Date updateTime;

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

    public Long getAccountSourceId() {
        return accountSourceId;
    }

    public void setAccountSourceId(Long accountSourceId) {
        this.accountSourceId = accountSourceId;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Integer getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
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