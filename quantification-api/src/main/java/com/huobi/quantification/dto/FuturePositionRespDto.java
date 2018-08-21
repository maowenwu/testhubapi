package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class FuturePositionRespDto implements Serializable {

    private Map<String, List<Position>> dataMap;

    public FuturePositionRespDto(Map<String, List<Position>> dataMap) {
        this.dataMap = dataMap;
    }

    public Map<String, List<Position>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, List<Position>> dataMap) {
        this.dataMap = dataMap;
    }

    public static class Position implements Serializable {

        private String baseCoin;
        private String quoteCoin;
        private String contractType;
        private String contractCode;

        private int side;
        private BigDecimal amount;
        private BigDecimal available;
        private BigDecimal frozen;
        private BigDecimal costOpen;
        private BigDecimal costHold;
        private BigDecimal leverRate;

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

        public String getContractCode() {
            return contractCode;
        }

        public void setContractCode(String contractCode) {
            this.contractCode = contractCode;
        }

        public int getSide() {
            return side;
        }

        public void setSide(int side) {
            this.side = side;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
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

        public BigDecimal getLeverRate() {
            return leverRate;
        }

        public void setLeverRate(BigDecimal leverRate) {
            this.leverRate = leverRate;
        }
    }
}
