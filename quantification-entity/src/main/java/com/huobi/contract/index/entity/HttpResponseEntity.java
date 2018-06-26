package com.huobi.contract.index.entity;

/**
 * @desc
 * @Author mingjianyong
 */
public class HttpResponseEntity {
    private int statusCode;
    private String response;
    private int status;

    public HttpResponseEntity(){}

    public HttpResponseEntity(int statusCode, String response, int status) {
        this.statusCode = statusCode;
        this.response = response;
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
