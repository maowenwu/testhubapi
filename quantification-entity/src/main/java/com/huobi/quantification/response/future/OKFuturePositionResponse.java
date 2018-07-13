package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OKFuturePositionResponse {


    @JSONField(name = "force_liqu_price")
    private String forceLiquPrice;
    private boolean result;
    private List<HoldingBean> holding;

    public String getForceLiquPrice() {
        return forceLiquPrice;
    }

    public void setForceLiquPrice(String forceLiquPrice) {
        this.forceLiquPrice = forceLiquPrice;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<HoldingBean> getHolding() {
        return holding;
    }

    public void setHolding(List<HoldingBean> holding) {
        this.holding = holding;
    }

    public static class HoldingBean {
        @JSONField(name = "buy_amount")
        private BigDecimal buyAmount;
        @JSONField(name = "buy_available")
        private BigDecimal buyAvailable;
        @JSONField(name = "buy_price_avg")
        private BigDecimal buyPriceAvg;
        @JSONField(name = "buy_price_cost")
        private BigDecimal buyPriceCost;
        @JSONField(name = "buy_profit_real")
        private BigDecimal buyProfitReal;
        @JSONField(name = "contract_id")
        private long contractId;
        @JSONField(name = "contract_type")
        private String contractType;
        @JSONField(name = "create_date")
        private Date createDate;
        @JSONField(name = "lever_rate")
        private BigDecimal leverRate;
        @JSONField(name = "sell_amount")
        private BigDecimal sellAmount;
        @JSONField(name = "sell_available")
        private BigDecimal sellAvailable;
        @JSONField(name = "sell_price_avg")
        private BigDecimal sellPriceAvg;
        @JSONField(name = "sell_price_cost")
        private BigDecimal sellPriceCost;
        @JSONField(name = "sell_profit_real")
        private BigDecimal sellProfitReal;
        private String symbol;

        public BigDecimal getBuyAmount() {
            return buyAmount;
        }

        public void setBuyAmount(BigDecimal buyAmount) {
            this.buyAmount = buyAmount;
        }

        public BigDecimal getBuyAvailable() {
            return buyAvailable;
        }

        public void setBuyAvailable(BigDecimal buyAvailable) {
            this.buyAvailable = buyAvailable;
        }

        public BigDecimal getBuyPriceAvg() {
            return buyPriceAvg;
        }

        public void setBuyPriceAvg(BigDecimal buyPriceAvg) {
            this.buyPriceAvg = buyPriceAvg;
        }

        public BigDecimal getBuyPriceCost() {
            return buyPriceCost;
        }

        public void setBuyPriceCost(BigDecimal buyPriceCost) {
            this.buyPriceCost = buyPriceCost;
        }

        public BigDecimal getBuyProfitReal() {
            return buyProfitReal;
        }

        public void setBuyProfitReal(BigDecimal buyProfitReal) {
            this.buyProfitReal = buyProfitReal;
        }

        public long getContractId() {
            return contractId;
        }

        public void setContractId(long contractId) {
            this.contractId = contractId;
        }

        public String getContractType() {
            return contractType;
        }

        public void setContractType(String contractType) {
            this.contractType = contractType;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        public BigDecimal getLeverRate() {
            return leverRate;
        }

        public void setLeverRate(BigDecimal leverRate) {
            this.leverRate = leverRate;
        }

        public BigDecimal getSellAmount() {
            return sellAmount;
        }

        public void setSellAmount(BigDecimal sellAmount) {
            this.sellAmount = sellAmount;
        }

        public BigDecimal getSellAvailable() {
            return sellAvailable;
        }

        public void setSellAvailable(BigDecimal sellAvailable) {
            this.sellAvailable = sellAvailable;
        }

        public BigDecimal getSellPriceAvg() {
            return sellPriceAvg;
        }

        public void setSellPriceAvg(BigDecimal sellPriceAvg) {
            this.sellPriceAvg = sellPriceAvg;
        }

        public BigDecimal getSellPriceCost() {
            return sellPriceCost;
        }

        public void setSellPriceCost(BigDecimal sellPriceCost) {
            this.sellPriceCost = sellPriceCost;
        }

        public BigDecimal getSellProfitReal() {
            return sellProfitReal;
        }

        public void setSellProfitReal(BigDecimal sellProfitReal) {
            this.sellProfitReal = sellProfitReal;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
    }
}
