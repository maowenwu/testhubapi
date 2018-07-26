package com.huobi.quantification.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FuturePositionRespDto implements Serializable {


    private Date ts;

    private Map<String, List<DataBean>> data;

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Map<String, List<DataBean>> getData() {
        return data;
    }

    public void setData(Map<String, List<DataBean>> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        @JSONField(name = "contract_code")
        private String contractCode;
        @JSONField(name = "base_coin")
        private String baseCoin;
        private String quoteCoin;
        @JSONField(name = "contract_type")
        private String contractType;
        @JSONField(name = "long_amount")
        private BigDecimal longAmount;
        @JSONField(name = "long_available")
        private BigDecimal longAvailable;
        @JSONField(name = "long_frozen")
        private BigDecimal longFrozen;
        @JSONField(name = "long_cost_open")
        private BigDecimal longCostOpen;
        @JSONField(name = "long_cost_hold")
        private BigDecimal longCostHold;
        @JSONField(name = "lever_rate")
        private BigDecimal leverRate;


        @JSONField(name = "short_amount")
        private BigDecimal shortAmount;
        @JSONField(name = "short_available")
        private BigDecimal shortAvailable;
        @JSONField(name = "short_frozen")
        private BigDecimal shortFrozen;
        @JSONField(name = "short_cost_open")
        private BigDecimal shortCostOpen;
        @JSONField(name = "short_cost_hold")
        private BigDecimal shortCostHold;

        public String getContractCode() {
            return contractCode;
        }

        public void setContractCode(String contractCode) {
            this.contractCode = contractCode;
        }

        public String getBaseCoin() {
            return baseCoin;
        }

        public void setBaseCoin(String baseCoin) {
            this.baseCoin = baseCoin;
        }

        public String getQuoteCoin() {
            return quoteCoin;
        }

        public void setQuoteCoin(String quoteCoin) {
            this.quoteCoin = quoteCoin;
        }

        public String getContractType() {
            return contractType;
        }

        public void setContractType(String contractType) {
            this.contractType = contractType;
        }

        public BigDecimal getLongAmount() {
            return longAmount;
        }

        public void setLongAmount(BigDecimal longAmount) {
            this.longAmount = longAmount;
        }

        public BigDecimal getLongAvailable() {
            return longAvailable;
        }

        public void setLongAvailable(BigDecimal longAvailable) {
            this.longAvailable = longAvailable;
        }

        public BigDecimal getLongFrozen() {
            return longFrozen;
        }

        public void setLongFrozen(BigDecimal longFrozen) {
            this.longFrozen = longFrozen;
        }

        public BigDecimal getLongCostOpen() {
            return longCostOpen;
        }

        public void setLongCostOpen(BigDecimal longCostOpen) {
            this.longCostOpen = longCostOpen;
        }

        public BigDecimal getLongCostHold() {
            return longCostHold;
        }

        public void setLongCostHold(BigDecimal longCostHold) {
            this.longCostHold = longCostHold;
        }

        public BigDecimal getLeverRate() {
            return leverRate;
        }

        public void setLeverRate(BigDecimal leverRate) {
            this.leverRate = leverRate;
        }

        public BigDecimal getShortAmount() {
            return shortAmount;
        }

        public void setShortAmount(BigDecimal shortAmount) {
            this.shortAmount = shortAmount;
        }

        public BigDecimal getShortAvailable() {
            return shortAvailable;
        }

        public void setShortAvailable(BigDecimal shortAvailable) {
            this.shortAvailable = shortAvailable;
        }

        public BigDecimal getShortFrozen() {
            return shortFrozen;
        }

        public void setShortFrozen(BigDecimal shortFrozen) {
            this.shortFrozen = shortFrozen;
        }

        public BigDecimal getShortCostOpen() {
            return shortCostOpen;
        }

        public void setShortCostOpen(BigDecimal shortCostOpen) {
            this.shortCostOpen = shortCostOpen;
        }

        public BigDecimal getShortCostHold() {
            return shortCostHold;
        }

        public void setShortCostHold(BigDecimal shortCostHold) {
            this.shortCostHold = shortCostHold;
        }
    }
}
