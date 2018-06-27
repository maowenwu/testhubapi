package com.huobi.contract.index.facade.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 指数列表结果对象
 */
public class ContractIndexListResult implements Serializable {


    private static final long serialVersionUID = -4069329559194193491L;
    /**
     * 当前价格
     */
    private BigDecimal indexPrice;
    /**
     * 中位数
     */
    private BigDecimal midNumber;
    /**
     * 品种对应的列表信息
     */
    private List<ContractIndexInfo> list;

    public ContractIndexListResult() {
    }

    public BigDecimal getIndexPrice() {
        return indexPrice;
    }

    public void setIndexPrice(BigDecimal indexPrice) {
        this.indexPrice = indexPrice;
    }

    public BigDecimal getMidNumber() {
        return midNumber;
    }

    public void setMidNumber(BigDecimal midNumber) {
        this.midNumber = midNumber;
    }

    public List<ContractIndexInfo> getList() {
        return list;
    }

    public void setList(List<ContractIndexInfo> list) {
        this.list = list;
    }
}
