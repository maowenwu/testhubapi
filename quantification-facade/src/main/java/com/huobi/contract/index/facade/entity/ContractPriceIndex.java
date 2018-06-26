package com.huobi.contract.index.facade.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContractPriceIndex implements Serializable {

    private static final long serialVersionUID = 1153664300990176695L;
    /**
     * ID
     */
    private Long id;
    /**
     * 品种（eg:BTC-USD）
     */
    private String indexSymbol;
    /**
     * 价格
     */
    private BigDecimal indexPrice;
    /**
     * 录入时间
     */
    private Date inputTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 权重是否变更（0：否，1：是）
     */
    private Byte isWeightChange;//0 否，1 是
    /**
     * 最近一次变更时间
     */
    private Date lastTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndexSymbol() {
        return indexSymbol;
    }

    public void setIndexSymbol(String indexSymbol) {
        this.indexSymbol = indexSymbol == null ? null : indexSymbol.trim();
    }

    public BigDecimal getIndexPrice() {
        return indexPrice;
    }

    public void setIndexPrice(BigDecimal indexPrice) {
        this.indexPrice = indexPrice;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getIsWeightChange() {
        return isWeightChange;
    }

    public void setIsWeightChange(Byte isWeightChange) {
        this.isWeightChange = isWeightChange;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}