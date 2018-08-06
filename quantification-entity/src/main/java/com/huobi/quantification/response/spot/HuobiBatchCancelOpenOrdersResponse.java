package com.huobi.quantification.response.spot;

import com.alibaba.fastjson.annotation.JSONField;

public class HuobiBatchCancelOpenOrdersResponse {


    private String status;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @JSONField(name = "success-count")
        private int successCount;
        @JSONField(name = "failed-count")
        private int failedCount;
        @JSONField(name = "next-id")
        private int nextid;

        public int getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
        }

        public int getFailedCount() {
            return failedCount;
        }

        public void setFailedCount(int failedCount) {
            this.failedCount = failedCount;
        }

        public int getNextid() {
            return nextid;
        }

        public void setNextid(int nextid) {
            this.nextid = nextid;
        }
    }
}
