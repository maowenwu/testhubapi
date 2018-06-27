package com.huobi.quantification;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 
 */
public class TOrderSequence implements Serializable {
    /**
     * 主键id
     */
    private Long fId;

    /**
     * 乐观锁数据版本
     */
    private Long fVersion;

    /**
     * 记录业务时间的毫秒数
     */
    private Long fCreatedAt;

    /**
     * 更新时间
     */
    private Long fUpdatedAt;

    /**
     * 订单类型：1:市价买入、2:市价卖出、3:限价买入、4:限价卖出、5:撤单申请
     */
    private Byte fType;

    /**
     * 订单id
     */
    private Long fOrderId;

    /**
     * 交易对
     */
    private String fSymbol;

    /**
     * 限价订单表示订单数量、市价卖出表示订单数量、市价买入表示订单金额
     */
    private BigDecimal fAmount;

    /**
     * 限价订单订单价格
     */
    private BigDecimal fPrice;

    /**
     * 系统当前熔断率
     */
    private Float fCircuitRate;

    /**
     * 撮合相关订单数
     */
    private Integer fOrderCount;

    /**
     * 撮合消息重试次数
     */
    private Integer fRetry;

    private static final long serialVersionUID = 1L;

    public Long getfId() {
        return fId;
    }

    public void setfId(Long fId) {
        this.fId = fId;
    }

    public Long getfVersion() {
        return fVersion;
    }

    public void setfVersion(Long fVersion) {
        this.fVersion = fVersion;
    }

    public Long getfCreatedAt() {
        return fCreatedAt;
    }

    public void setfCreatedAt(Long fCreatedAt) {
        this.fCreatedAt = fCreatedAt;
    }

    public Long getfUpdatedAt() {
        return fUpdatedAt;
    }

    public void setfUpdatedAt(Long fUpdatedAt) {
        this.fUpdatedAt = fUpdatedAt;
    }

    public Byte getfType() {
        return fType;
    }

    public void setfType(Byte fType) {
        this.fType = fType;
    }

    public Long getfOrderId() {
        return fOrderId;
    }

    public void setfOrderId(Long fOrderId) {
        this.fOrderId = fOrderId;
    }

    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }

    public BigDecimal getfAmount() {
        return fAmount;
    }

    public void setfAmount(BigDecimal fAmount) {
        this.fAmount = fAmount;
    }

    public BigDecimal getfPrice() {
        return fPrice;
    }

    public void setfPrice(BigDecimal fPrice) {
        this.fPrice = fPrice;
    }

    public Float getfCircuitRate() {
        return fCircuitRate;
    }

    public void setfCircuitRate(Float fCircuitRate) {
        this.fCircuitRate = fCircuitRate;
    }

    public Integer getfOrderCount() {
        return fOrderCount;
    }

    public void setfOrderCount(Integer fOrderCount) {
        this.fOrderCount = fOrderCount;
    }

    public Integer getfRetry() {
        return fRetry;
    }

    public void setfRetry(Integer fRetry) {
        this.fRetry = fRetry;
    }
}