package com.huobi.quantification.huobi.api;

/**
 * @author shaoxiaofeng
 * @since 2018/6/29
 */

public class ApiException extends RuntimeException {

    final String errCode;

    public ApiException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    public ApiException(Exception e) {
        super(e);
        this.errCode = e.getClass().getName();
    }

    public String getErrCode() {
        return this.errCode;
    }

}
