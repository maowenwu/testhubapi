package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanAccountAsset {
    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long id;

    /**
     * 账号ID
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long accountId;

    /**
     * 币种
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal coin;

    /**
     * 总量
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal total;

    /**
     * 可用
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal available;

    /**
     * 冻结
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal frozen;

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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getCoin() {
        return coin;
    }

    public void setCoin(BigDecimal coin) {
        this.coin = coin;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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