package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OKFutureQueryOrderResponse {


    private boolean result;
    private List<OrdersBean> orders;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {
        //委托数量
        private BigDecimal amount;
        //合约名称
        @JSONField(name = "contract_name")
        private String contractName;
        // 委托时间
        @JSONField(name = "create_date")
        private Date createDate;
        //成交数量
        @JSONField(name = "deal_amount")
        private BigDecimal dealAmount;
        //手续费
        private BigDecimal fee;
        //杠杆倍数
        @JSONField(name = "lever_rate")
        private int leverRate;
        //订单ID
        @JSONField(name = "order_id")
        private long orderId;
        //订单价格
        private BigDecimal price;
        //平均价格
        @JSONField(name = "price_avg")
        private BigDecimal priceAvg;
        //订单状态(0等待成交 1部分成交 2全部成交 -1撤单 4撤单处理中)
        private int status;
        private String symbol;
        //订单类型
        private int type;
        //合约面值
        @JSONField(name = "unit_amount")
        private BigDecimal unitAmount;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getContractName() {
            return contractName;
        }

        public void setContractName(String contractName) {
            this.contractName = contractName;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        public BigDecimal getDealAmount() {
            return dealAmount;
        }

        public void setDealAmount(BigDecimal dealAmount) {
            this.dealAmount = dealAmount;
        }

        public BigDecimal getFee() {
            return fee;
        }

        public void setFee(BigDecimal fee) {
            this.fee = fee;
        }

        public int getLeverRate() {
            return leverRate;
        }

        public void setLeverRate(int leverRate) {
            this.leverRate = leverRate;
        }

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getPriceAvg() {
            return priceAvg;
        }

        public void setPriceAvg(BigDecimal priceAvg) {
            this.priceAvg = priceAvg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public BigDecimal getUnitAmount() {
            return unitAmount;
        }

        public void setUnitAmount(BigDecimal unitAmount) {
            this.unitAmount = unitAmount;
        }
    }
}
