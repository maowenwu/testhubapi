package com.huobi.contract.index.common.util;

/**
 * @desc
 * @Author mingjianyong
 */
public enum HttpType {
    HTTP(1,"http"),
    HTTPS(2,"https"),
    WEBSOCKET(3,"websocket");
    private int type;
    private String name;

    HttpType(int type, String name) {
        this.type = type;
        this.name = name;
    }
    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
