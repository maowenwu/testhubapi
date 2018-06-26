package com.huobi.contract.index.facade.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 指数变更记录返回结果对象
 */
public class SymbolChangeRecord implements Serializable {

    private static final long serialVersionUID = -282498903884553426L;
    /**
     * 品种
     */
    private String symbol;
    /**
     * 更改时间
     */
    private String updateTime;
    /**
     *生效时间
     */
    private String effectiveTime;
    /**
     *交易所权重记录
     */
    private List<ExchangeWeight> exchangeWeights;
    /**
     *操作人
     */
    private String operateUser;
    /**
     * 原因
     */
    private String reason;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }


    public List<ExchangeWeight> getExchangeWeights() {
        return exchangeWeights;
    }

    public void setExchangeWeights(List<ExchangeWeight> exchangeWeights) {
        this.exchangeWeights = exchangeWeights;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
