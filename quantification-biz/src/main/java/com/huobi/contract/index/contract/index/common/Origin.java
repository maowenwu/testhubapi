package com.huobi.contract.index.contract.index.common;

public enum Origin {

    HTTP(0), WEBSOCKET(1);

    private int origin;

    Origin(int origin) {
        this.origin = origin;
    }

    public int value() {
        return origin;
    }
}
