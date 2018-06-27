package com.huobi.contract.index.facade.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *指数价格结果对象
 * @see #exchangeName
 * @see #exchangeId
 */
public class ContractIndexInfo implements Serializable {


    private static final long serialVersionUID = -7839998823684342432L;
    /**
     * 交易所ID
     */
    private Long exchangeId;
    /**
     * 交易所全称
     */
    private String exchangeName;
    /**
     * 交易所简称
     */
    private String exchangeShortName;
    /**
     * 权重(值为占比,eg:25,标识权重为25%)
     */
    private BigDecimal weight;
    /**
     * 原始权重
     */
    private BigDecimal originalWeight;
    /**
     *最近100次成功记录数
     */
    private Integer succCount;
    /**
     * 最新价格
     */
    private BigDecimal lastPrice;
    /**
     * 偏移中位数比例(值为保留4位有效数字的小数,需转换为对应的百分比)
     */
    private BigDecimal offsetCentorRate;
    /**
     * 偏移指数比例
     */
    private BigDecimal offsetIndexRate;
    /**
     *更新时间
     */
    private String updateTime;

    public ContractIndexInfo() {
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeShortName() {
        return exchangeShortName;
    }

    public void setExchangeShortName(String exchangeShortName) {
        this.exchangeShortName = exchangeShortName;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getOriginalWeight() {
        return originalWeight;
    }

    public void setOriginalWeight(BigDecimal originalWeight) {
        this.originalWeight = originalWeight;
    }

    public Integer getSuccCount() {
        return succCount;
    }

    public void setSuccCount(Integer succCount) {
        this.succCount = succCount;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getOffsetCentorRate() {
        return offsetCentorRate;
    }

    public void setOffsetCentorRate(BigDecimal offsetCentorRate) {
        this.offsetCentorRate = offsetCentorRate;
    }

    public BigDecimal getOffsetIndexRate() {
        return offsetIndexRate;
    }

    public void setOffsetIndexRate(BigDecimal offsetIndexRate) {
        this.offsetIndexRate = offsetIndexRate;
    }

    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
