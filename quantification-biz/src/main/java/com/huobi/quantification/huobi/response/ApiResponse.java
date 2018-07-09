package com.huobi.quantification.huobi.response;

import com.huobi.quantification.huobi.api.ApiException;
/**
 * @author shaoxiaofeng
 * @since 2018/6/29
 */
public class ApiResponse<T> {

    public String status;
    public String errCode;
    public String errMsg;
    public T data;

    public T checkAndReturn() {
        if ("ok".equals(status)) {
            return data;
        }
        throw new ApiException(errCode, errMsg);
    }
}
