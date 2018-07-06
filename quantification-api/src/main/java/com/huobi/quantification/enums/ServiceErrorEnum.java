package com.huobi.quantification.enums;

public enum ServiceErrorEnum {

    SUCCESS(0, "调用成功"),
    JOB_START_ERROR(100, "任务启动异常"),
    JOB_STOP_ERROR(100, "任务停止异常");


    private int code;
    private String message;

    ServiceErrorEnum(int code, String message) {
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
