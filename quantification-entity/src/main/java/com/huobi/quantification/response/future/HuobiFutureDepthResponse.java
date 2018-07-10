package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.List;

/**
 * http://127.0.0.1:8882/market/depth?symbol=btc_cw&type=step5
 */
public class HuobiFutureDepthResponse {


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
        private String ch;
        @JSONField(name = "commodity-type")
        private String commodityType;
        private long id;
        private long mrid;
        private long ts;
        private long version;
        private List<List<BigDecimal>> asks;
        private List<List<BigDecimal>> bids;

        public String getCh() {
            return ch;
        }

        public void setCh(String ch) {
            this.ch = ch;
        }

        public String getCommodityType() {
            return commodityType;
        }

        public void setCommodityType(String commodityType) {
            this.commodityType = commodityType;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getMrid() {
            return mrid;
        }

        public void setMrid(long mrid) {
            this.mrid = mrid;
        }

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

        public List<List<BigDecimal>> getAsks() {
            return asks;
        }

        public void setAsks(List<List<BigDecimal>> asks) {
            this.asks = asks;
        }

        public List<List<BigDecimal>> getBids() {
            return bids;
        }

        public void setBids(List<List<BigDecimal>> bids) {
            this.bids = bids;
        }
    }
}
