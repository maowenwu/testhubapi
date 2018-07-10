package com.huobi.quantification.response.future;

import java.math.BigDecimal;
import java.util.List;

/**
 * http://127.0.0.1:8882/market/detail/merged?symbol=btc_cw
 */
public class HuobiFutureTickerResponse {


    private String ch;
    private String status;
    private TickBean tick;
    private long ts;

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TickBean getTick() {
        return tick;
    }

    public void setTick(TickBean tick) {
        this.tick = tick;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public static class TickBean {
        private BigDecimal amount;
        private BigDecimal close;
        private BigDecimal count;
        private BigDecimal high;
        private long id;
        private BigDecimal low;
        private BigDecimal open;
        private long ts;
        private BigDecimal vol;
        private List<List<BigDecimal>> ask;
        private List<List<BigDecimal>> bid;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getClose() {
            return close;
        }

        public void setClose(BigDecimal close) {
            this.close = close;
        }

        public BigDecimal getCount() {
            return count;
        }

        public void setCount(BigDecimal count) {
            this.count = count;
        }

        public BigDecimal getHigh() {
            return high;
        }

        public void setHigh(BigDecimal high) {
            this.high = high;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public BigDecimal getLow() {
            return low;
        }

        public void setLow(BigDecimal low) {
            this.low = low;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public long getTs() {
            return ts;
        }

        public void setTs(long ts) {
            this.ts = ts;
        }

        public BigDecimal getVol() {
            return vol;
        }

        public void setVol(BigDecimal vol) {
            this.vol = vol;
        }

        public List<List<BigDecimal>> getAsk() {
            return ask;
        }

        public void setAsk(List<List<BigDecimal>> ask) {
            this.ask = ask;
        }

        public List<List<BigDecimal>> getBid() {
            return bid;
        }

        public void setBid(List<List<BigDecimal>> bid) {
            this.bid = bid;
        }
    }
}
