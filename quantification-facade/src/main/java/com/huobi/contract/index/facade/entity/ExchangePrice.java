package com.huobi.contract.index.facade.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 价格记录对象
 * @see #timePoint
 * @see #value
 */
public class ExchangePrice implements Serializable {

    private static final long serialVersionUID = 3461521527477324632L;
    /**
     * 时间点
     */
    private String timePoint;
    /**
     * 对应值
     */
    private BigDecimal value;

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
