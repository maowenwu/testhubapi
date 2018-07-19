package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

public class OKFutureCancelOrderResponse {

    // 单笔时返回
    @JSONField(name = "order_id")
    private long orderId;
    private boolean result;
    @JSONField(name = "error_code")
    private int errorCode;

    // 批量时返回
    private String error;
    private String success;


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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
