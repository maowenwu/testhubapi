package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class HuobiFutureOrderCancelAllResponse {


    private String status;
    private DataBean data;
    private long ts;

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

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public static class DataBean {
        private List<ErrorsBean> errors;
        private List<Integer> success;

        public List<ErrorsBean> getErrors() {
            return errors;
        }

        public void setErrors(List<ErrorsBean> errors) {
            this.errors = errors;
        }

        public List<Integer> getSuccess() {
            return success;
        }

        public void setSuccess(List<Integer> success) {
            this.success = success;
        }

        public static class ErrorsBean {
            @JSONField(name = "order_id")
            private String orderId;
            @JSONField(name = "client_order_id")
            private int clientOrderId;
            @JSONField(name = "err_code")
            private int errCode;
            @JSONField(name = "err_msg")
            private String errMsg;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public int getClientOrderId() {
                return clientOrderId;
            }

            public void setClientOrderId(int clientOrderId) {
                this.clientOrderId = clientOrderId;
            }

            public int getErrCode() {
                return errCode;
            }

            public void setErrCode(int errCode) {
                this.errCode = errCode;
            }

            public String getErrMsg() {
                return errMsg;
            }

            public void setErrMsg(String errMsg) {
                this.errMsg = errMsg;
            }
        }
    }
}
