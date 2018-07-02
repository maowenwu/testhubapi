package com.huobi.quantification.enums;

public enum ServiceResultEnum {

    SUCCESS(0, "操作成功");

    private int code;
    private String message;

    ServiceResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
