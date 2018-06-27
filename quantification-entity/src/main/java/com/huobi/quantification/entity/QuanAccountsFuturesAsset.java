package com.huobi.quantification.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
public class QuanAccountsFuturesAsset implements Serializable {
    private Long id;

    /**
     * 账号ID
     */
    private Long accountsId;

    /**
     * 币种
     */
    private BigDecimal coin;

    /**
     * 账户权益
     */
    private BigDecimal rights;

    /**
     * api请求时间
     */
    private Date dataUpdate;

    /**
     * 交易所服务器时间
     */
    private Date ts;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountsId() {
        return accountsId;
    }

    public void setAccountsId(Long accountsId) {
        this.accountsId = accountsId;
    }

    public BigDecimal getCoin() {
        return coin;
    }

    public void setCoin(BigDecimal coin) {
        this.coin = coin;
    }

    public BigDecimal getRights() {
        return rights;
    }

    public void setRights(BigDecimal rights) {
        this.rights = rights;
    }

    public Date getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(Date dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}