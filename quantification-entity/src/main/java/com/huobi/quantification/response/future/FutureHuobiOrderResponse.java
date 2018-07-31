package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class FutureHuobiOrderResponse {

    private String status;
    private DataBean data;
    private Date ts;

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

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public static class DataBean {
        @JSONField(name = "order_id")
        private Long orderId;
        @JSONField(name = "client_order_id")
        private Long clientOrderId;

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
    }
}
