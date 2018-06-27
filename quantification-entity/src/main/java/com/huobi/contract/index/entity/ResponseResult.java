package com.huobi.contract.index.entity;

public class ResponseResult {
    private Integer statusCode;
    private String response;

    public ResponseResult() {
    }

    public ResponseResult(Integer statusCode, String response) {
        this.statusCode = statusCode;
        this.response = response;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
