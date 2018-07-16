package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

public class OKFutureOrderResponse {


    @JSONField(name = "order_id")
    private long orderId;
    private boolean result;

    @JSONField(name = "error_code")
    private int errorCode;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
