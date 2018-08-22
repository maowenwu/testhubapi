package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.List;


/**
 * https://api.hcontract.com/contract/v1/contract_accountinfo
 */
public class HuobiFutureAccountResponse {

    private String status;
    private long ts;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String symbol;
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
        @JSONField(name = "liquidation_price")
        private BigDecimal liquidationPrice;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

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

        public BigDecimal getLiquidationPrice() {
            return liquidationPrice;
        }

        public void setLiquidationPrice(BigDecimal liquidationPrice) {
            this.liquidationPrice = liquidationPrice;
        }
    }
}
