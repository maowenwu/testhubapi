package com.huobi.quantification.enums;

public enum ServiceErrorEnum {

    SUCCESS(0, "调用成功"),
    JOB_START_ERROR(100, "任务启动异常"),
    JOB_STOP_ERROR(101, "任务停止异常"),
    EXECUTION_ERROR(102, "方法执行异常"),
    TIMEOUT_ERROR(103, "方法超时异常"),
    PARAM_MISS(204, "请求参数缺失");

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
