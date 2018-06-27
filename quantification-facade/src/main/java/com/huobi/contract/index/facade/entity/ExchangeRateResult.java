package com.huobi.contract.index.facade.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>汇率结果对象</p>
 */
public class ExchangeRateResult implements Serializable {

    private static final long serialVersionUID = 2715556966193899875L;

    /**
     * 币对（eg：BTC-USD,CNY-USD）
     */
    private String exchangeSymbol;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;
    /**
     * 汇率获取时间
     */
    private Date inputTime;
    /**
     * 备注
     */
    private String remark;


    public String getExchangeSymbol() {
        return exchangeSymbol;
    }

    public void setExchangeSymbol(String exchangeSymbol) {
        this.exchangeSymbol = exchangeSymbol == null ? null : exchangeSymbol.trim();
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
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
}