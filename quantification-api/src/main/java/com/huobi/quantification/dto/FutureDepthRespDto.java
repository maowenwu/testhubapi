package com.huobi.quantification.dto;

import java.util.List;

public class FutureDepthRespDto {


    private int ts;
    private DataBean data;

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<List<Double>> asks;
        private List<List<Double>> bids;

        public List<List<Double>> getAsks() {
            return asks;
        }

        public void setAsks(List<List<Double>> asks) {
            this.asks = asks;
        }

        public List<List<Double>> getBids() {
            return bids;
        }

        public void setBids(List<List<Double>> bids) {
            this.bids = bids;
        }
    }
}
