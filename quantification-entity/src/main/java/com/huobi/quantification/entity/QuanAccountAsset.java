package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanAccountAsset {
    /**
     * @mbg.generated 2018-08-03 16:47:13
     */
    private Long id;

    /**
     * 账号ID
     * @mbg.generated 2018-08-03 16:47:13
     */
    private Long accountId;

    /**
     * 币种
     * @mbg.generated 2018-08-03 16:47:13
     */
    private String coinType;

    /**
     * 总量
     * @mbg.generated 2018-08-03 16:47:13
     */
    private BigDecimal total;

    /**
     * 可用
     * @mbg.generated 2018-08-03 16:47:13
     */
    private BigDecimal available;

    /**
     * 冻结
     * @mbg.generated 2018-08-03 16:47:13
     */
    private BigDecimal frozen;

    /**
     * @mbg.generated 2018-08-03 16:47:13
     */
    private Integer init;

    /**
     * 交易所服务器时间
     * @mbg.generated 2018-08-03 16:47:13
     */
    private Date createTime;

    /**
     * api请求时间
     * @mbg.generated 2018-08-03 16:47:13
     */
    private Date updateTime;

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

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
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