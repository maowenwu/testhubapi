package com.huobi.quantification.strategy.order.entity;

import java.math.BigDecimal;

public class SpotBalance {

    private Coin coin;
    private Usdt usdt;

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public Usdt getUsdt() {
        return usdt;
    }

    public void setUsdt(Usdt usdt) {
        this.usdt = usdt;
    }

    public static class Coin {
        private BigDecimal total;
        private BigDecimal available;
        private BigDecimal frozen;

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
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
    }

    public static class Usdt {
        private BigDecimal total;
        private BigDecimal available;
        private BigDecimal frozen;

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
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
    }

}
