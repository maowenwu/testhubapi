package com.huobi.quantification.common;

import com.huobi.quantification.enums.ServiceErrorEnum;

import java.io.Serializable;

public class ServiceResult<T> implements Serializable {

    private int code;

    private String message;

    private T data;

    public ServiceResult() {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ServiceResult<T> buildSuccessResult(T t) {
        ServiceResult<T> serviceResult = new ServiceResult<>();
        serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
        serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
        serviceResult.setData(t);
        return serviceResult;
    }

    public static ServiceResult buildErrorResult(ServiceErrorEnum errorEnum) {
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setCode(errorEnum.getCode());
        serviceResult.setMessage(errorEnum.getMessage());
        return serviceResult;
    }

    public static ServiceResult buildAPIErrorResult(String message) {
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setCode(1101);
        serviceResult.setMessage("api异常：" + message);
        return serviceResult;
    }

    public boolean isSuccess() {
        return code == ServiceErrorEnum.SUCCESS.getCode();
    }
}
