package com.huobi.quantification.response.spot;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class HuobiSpotDepthResponse {


    private String status;
    private String ch;
    private Date ts;
    private TickBean tick;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public TickBean getTick() {
        return tick;
    }

    public void setTick(TickBean tick) {
        this.tick = tick;
    }

    public static class TickBean {
        private long ts;
        private long version;
        private List<List<BigDecimal>> bids;
        private List<List<BigDecimal>> asks;

        public long getTs() {
            return ts;
        }

        public void setTs(long ts) {
            this.ts = ts;
        }

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
        }

        public List<List<BigDecimal>> getBids() {
            return bids;
        }

        public void setBids(List<List<BigDecimal>> bids) {
            this.bids = bids;
        }

        public List<List<BigDecimal>> getAsks() {
            return asks;
        }

        public void setAsks(List<List<BigDecimal>> asks) {
            this.asks = asks;
        }
    }
}
