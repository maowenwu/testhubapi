package com.huobi.quantification.huobi.response;

import com.huobi.quantification.huobi.api.ApiException;

/**
 * @author shaoxiaofeng
 * @since 2018/6/29
 */

public class KlineResponse<T> {

    private String status;
    private String ch;
    private String ts;
    public String errCode;
    public String errMsg;
    public T data;

    public T checkAndReturn() {
        if ("ok".equals(status)) {
            return data;
        }
        throw new ApiException(errCode, errMsg);
    }


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

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
