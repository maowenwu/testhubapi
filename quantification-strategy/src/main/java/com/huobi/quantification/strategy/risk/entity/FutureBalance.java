package com.huobi.quantification.strategy.risk.entity;

import java.math.BigDecimal;

public class FutureBalance {
	//用户权益
    private BigDecimal marginBalance;
    
    //持仓保证金
    private BigDecimal marginPosition;
    
    //冻结保证金
    private BigDecimal marginFrozen;
    
    //可用保证金
    private BigDecimal marginAvailable;
    
    //已实现盈亏
    private BigDecimal profitReal;
    
    //未实现盈亏
    private BigDecimal profitUnreal;
    
    //保证金率
    private BigDecimal riskRate;

    public BigDecimal getMarginBalance() {
        return marginBalance;
    }

    public void setMarginBalance(BigDecimal marginBalance) {
        this.marginBalance = marginBalance;
    }

    public BigDecimal getMarginPosition() {
        return marginPosition;
    }

    public void setMarginPosition(BigDecimal marginPosition) {
        this.marginPosition = marginPosition;
    }

    public BigDecimal getMarginFrozen() {
        return marginFrozen;
    }

    public void setMarginFrozen(BigDecimal marginFrozen) {
        this.marginFrozen = marginFrozen;
    }

    public BigDecimal getMarginAvailable() {
        return marginAvailable;
    }

    public void setMarginAvailable(BigDecimal marginAvailable) {
        this.marginAvailable = marginAvailable;
    }

    public BigDecimal getProfitReal() {
        return profitReal;
    }

    public void setProfitReal(BigDecimal profitReal) {
        this.profitReal = profitReal;
    }

    public BigDecimal getProfitUnreal() {
        return profitUnreal;
    }

    public void setProfitUnreal(BigDecimal profitUnreal) {
        this.profitUnreal = profitUnreal;
    }

    public BigDecimal getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(BigDecimal riskRate) {
        this.riskRate = riskRate;
    }
}
