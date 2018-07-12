package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FutureDepthRespDto implements Serializable {


    private Date ts;
    private DataBean data;

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private List<Depth> asks;
        private List<Depth> bids;

        public List<Depth> getAsks() {
            return asks;
        }

        public void setAsks(List<Depth> asks) {
            this.asks = asks;
        }

        public List<Depth> getBids() {
            return bids;
        }

        public void setBids(List<Depth> bids) {
            this.bids = bids;
        }
    }

    public static class Depth implements Serializable {
        private BigDecimal price;
        private BigDecimal amount;

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
