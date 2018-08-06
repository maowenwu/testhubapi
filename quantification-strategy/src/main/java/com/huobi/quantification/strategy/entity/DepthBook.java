package com.huobi.quantification.strategy.entity;

import com.huobi.quantification.common.util.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DepthBook {

    private List<Depth> asks=new ArrayList<>();
    private List<Depth> bids=new ArrayList<>();

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

    public static class Depth {

        public Depth() {
        }

        public Depth(BigDecimal price, BigDecimal amount) {
            this.price = price;
            this.amount = amount;
        }

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Depth depth = (Depth) o;
            return BigDecimalUtils.equals(this.getPrice(), depth.getPrice());
        }

        @Override
        public int hashCode() {
            return Objects.hash(price);
        }
    }
}
