package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanAccountFutureAsset {
    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long id;

    /**
     * 账号ID
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long accountFutureId;

    /**
     * 币种
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal coin;

    /**
     * 账户权益
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal right;

    /**
     * 交易所服务器时间
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Date ts;

    /**
     * api请求时间
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Date dataUpdate;

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

    public BigDecimal getCoin() {
        return coin;
    }

    public void setCoin(BigDecimal coin) {
        this.coin = coin;
    }

    public BigDecimal getRight() {
        return right;
    }

    public void setRight(BigDecimal right) {
        this.right = right;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Date getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(Date dataUpdate) {
        this.dataUpdate = dataUpdate;
    }
}