package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.List;

/**
 * https://api.hcontract.com/contract/v1/contract_position
 */
public class HuobiFuturePositionResponse {


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
        @JSONField(name = "contract_code")
        private String contractCode;
        @JSONField(name = "contract_type")
        private String contractType;
        private BigDecimal volume;
        private BigDecimal available;
        private BigDecimal frozen;
        @JSONField(name = "cost_open")
        private BigDecimal costOpen;
        @JSONField(name = "cost_hold")
        private BigDecimal costHold;
        @JSONField(name = "profit_unreal")
        private BigDecimal profitUnreal;
        @JSONField(name = "profit_rate")
        private BigDecimal profitRate;
        @JSONField(name = "position_margin")
        private BigDecimal positionMargin;
        @JSONField(name = "lever_rate")
        private BigDecimal leverRate;
        private String direction;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getContractCode() {
            return contractCode;
        }

        public void setContractCode(String contractCode) {
            this.contractCode = contractCode;
        }

        public String getContractType() {
            return contractType;
        }

        public void setContractType(String contractType) {
            this.contractType = contractType;
        }

        public BigDecimal getVolume() {
            return volume;
        }

        public void setVolume(BigDecimal volume) {
            this.volume = volume;
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

        public BigDecimal getCostOpen() {
            return costOpen;
        }

        public void setCostOpen(BigDecimal costOpen) {
            this.costOpen = costOpen;
        }

        public BigDecimal getCostHold() {
            return costHold;
        }

        public void setCostHold(BigDecimal costHold) {
            this.costHold = costHold;
        }

        public BigDecimal getProfitUnreal() {
            return profitUnreal;
        }

        public void setProfitUnreal(BigDecimal profitUnreal) {
            this.profitUnreal = profitUnreal;
        }

        public BigDecimal getProfitRate() {
            return profitRate;
        }

        public void setProfitRate(BigDecimal profitRate) {
            this.profitRate = profitRate;
        }

        public BigDecimal getPositionMargin() {
            return positionMargin;
        }

        public void setPositionMargin(BigDecimal positionMargin) {
            this.positionMargin = positionMargin;
        }

        public BigDecimal getLeverRate() {
            return leverRate;
        }

        public void setLeverRate(BigDecimal leverRate) {
            this.leverRate = leverRate;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }
}
