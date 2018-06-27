package com.huobi.quantification.entity;

import java.io.Serializable;
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
    private Long coin;

    /**
     * 账户权益
     */
    private Long rights;

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

    public Long getCoin() {
        return coin;
    }

    public void setCoin(Long coin) {
        this.coin = coin;
    }

    public Long getRights() {
        return rights;
    }

    public void setRights(Long rights) {
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