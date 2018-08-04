package com.huobi.quantification.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class FutureBalanceRespDto implements Serializable {

    private Map<String,DataBean> data;

    public Map<String, DataBean> getData() {
        return data;
    }

    public void setData(Map<String, DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        @JSONField(name = "margin_balance")
        private BigDecimal marginBalance;
        @JSONField(name = "margin_position")
        private BigDecimal marginPosition;
        @JSONField(name = "margin_frozen")
        private BigDecimal marginFrozen;
        @JSONField(name = "margin_available")
        private BigDecimal marginAvailable;
        @JSONField(name = "profit_real")
        private BigDecimal profitReal;
        @JSONField(name = "profit_unreal")
        private BigDecimal profitUnreal;
        @JSONField(name = "risk_rate")
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

}
