package com.huobi.quantification.strategy.risk.entity;

import java.math.BigDecimal;

/**
 * 账号持有的一个币种的信息
 * @author lichenyang
 * @since  2018年7月27日
 */
public class SpotBalance {

	//总计
    private BigDecimal total;
    
    //可用
    private BigDecimal available;
    
    //冻结
    private BigDecimal frozen;

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
}
