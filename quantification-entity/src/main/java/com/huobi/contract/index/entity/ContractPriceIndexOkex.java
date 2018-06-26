package com.huobi.contract.index.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ContractPriceIndexOkex {
    private Long id;

    private String indexSymbol;

    private BigDecimal indexPrice;

    private Date inputTime;

    private String remark;
    public ContractPriceIndexOkex(){}
    public ContractPriceIndexOkex(String indexSymbol, BigDecimal indexPrice, Date inputTime, String remark) {
        this.indexSymbol = indexSymbol;
        this.indexPrice = indexPrice;
        this.inputTime = inputTime;
        this.remark = remark;
    }

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
        this.indexSymbol = indexSymbol;
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
        this.remark = remark;
    }
}