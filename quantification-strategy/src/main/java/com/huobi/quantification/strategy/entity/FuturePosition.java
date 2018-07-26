package com.huobi.quantification.strategy.entity;

import java.math.BigDecimal;

public class FuturePosition {


    private LongPosi longPosi;
    private ShortPosi shortPosi;

    public LongPosi getLongPosi() {
        return longPosi;
    }

    public void setLongPosi(LongPosi longPosi) {
        this.longPosi = longPosi;
    }

    public ShortPosi getShortPosi() {
        return shortPosi;
    }

    public void setShortPosi(ShortPosi shortPosi) {
        this.shortPosi = shortPosi;
    }

    public static class LongPosi {
        private String contractCode;
        private String baseCoin;
        private String quoteCoin;
        private String contractType;
        private BigDecimal longAmount;
        private BigDecimal longAvailable;
        private BigDecimal longFrozen;
        private BigDecimal longCostOpen;
        private BigDecimal longCostHold;
        private BigDecimal leverRate;

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
    }

    public static class ShortPosi {
        private String contractCode;
        private String baseCoin;
        private String quoteCoin;
        private String contractType;
        private BigDecimal shortAmount;
        private BigDecimal shortAvailable;
        private BigDecimal shortFrozen;
        private BigDecimal shortCostOpen;
        private BigDecimal shortCostHold;
        private BigDecimal leverRate;

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

        public BigDecimal getLeverRate() {
            return leverRate;
        }

        public void setLeverRate(BigDecimal leverRate) {
            this.leverRate = leverRate;
        }
    }

}
