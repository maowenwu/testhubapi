package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FutureHuobiOrderInfoResponse {

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
        @JSONField(name = "contract_type")
        private String contractType;
        @JSONField(name = "contract_code")
        private String contractCode;
        private BigDecimal volume;
        private BigDecimal price;
        @JSONField(name = "order_price_type")
        private String orderPriceType;
        private String direction;
        private String offset;
        @JSONField(name = "lever_rate")
        private Integer leverRate;
        @JSONField(name = "order_id")
        private Long orderId;
        @JSONField(name = "client_order_id")
        private Long clientOrderId;
        @JSONField(name = "order_source")
        private String orderSource;
        @JSONField(name = "created_at")
        private Date createdAt;
        @JSONField(name = "trade_volume")
        private BigDecimal tradeVolume;
        @JSONField(name = "trade_turnover")
        private BigDecimal tradeTurnover;
        private BigDecimal fee;
        @JSONField(name = "trade_avg_price")
        private BigDecimal tradeAvgPrice;
        @JSONField(name = "margin_frozen")
        private BigDecimal marginFrozen;
        private BigDecimal profit;
        private int status;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
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

        public BigDecimal getVolume() {
            return volume;
        }

        public void setVolume(BigDecimal volume) {
            this.volume = volume;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getOrderPriceType() {
            return orderPriceType;
        }

        public void setOrderPriceType(String orderPriceType) {
            this.orderPriceType = orderPriceType;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getOffset() {
            return offset;
        }

        public void setOffset(String offset) {
            this.offset = offset;
        }

        public Integer getLeverRate() {
            return leverRate;
        }

        public void setLeverRate(Integer leverRate) {
            this.leverRate = leverRate;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public Long getClientOrderId() {
            return clientOrderId;
        }

        public void setClientOrderId(Long clientOrderId) {
            this.clientOrderId = clientOrderId;
        }

        public String getOrderSource() {
            return orderSource;
        }

        public void setOrderSource(String orderSource) {
            this.orderSource = orderSource;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public BigDecimal getTradeVolume() {
            return tradeVolume;
        }

        public void setTradeVolume(BigDecimal tradeVolume) {
            this.tradeVolume = tradeVolume;
        }

        public BigDecimal getTradeTurnover() {
            return tradeTurnover;
        }

        public void setTradeTurnover(BigDecimal tradeTurnover) {
            this.tradeTurnover = tradeTurnover;
        }

        public BigDecimal getFee() {
            return fee;
        }

        public void setFee(BigDecimal fee) {
            this.fee = fee;
        }

        public BigDecimal getTradeAvgPrice() {
            return tradeAvgPrice;
        }

        public void setTradeAvgPrice(BigDecimal tradeAvgPrice) {
            this.tradeAvgPrice = tradeAvgPrice;
        }

        public BigDecimal getMarginFrozen() {
            return marginFrozen;
        }

        public void setMarginFrozen(BigDecimal marginFrozen) {
            this.marginFrozen = marginFrozen;
        }

        public BigDecimal getProfit() {
            return profit;
        }

        public void setProfit(BigDecimal profit) {
            this.profit = profit;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
