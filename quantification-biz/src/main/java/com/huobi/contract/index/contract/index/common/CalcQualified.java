package com.huobi.contract.index.contract.index.common;


/**
 * @desc
 * @Author mingjianyong
 */
public enum CalcQualified {

    INVALID(0),
    HTTP(1),
    WEBSOCKET(2);

    private int calcSource;

    CalcQualified(int calcSource) {
        this.calcSource = calcSource;
    }

    public int value() {
        return calcSource;
    }
}
