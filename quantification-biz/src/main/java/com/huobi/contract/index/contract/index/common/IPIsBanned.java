package com.huobi.contract.index.contract.index.common;

/**
 * @desc
 * @Author mingjianyong
 */
public enum IPIsBanned {

    BANNED(1, "是"),
    NOTBANNED(0, "否");
    private int banStatus;
    private String msg;

    IPIsBanned(int banStatus, String msg) {
        this.banStatus = banStatus;
        this.msg = msg;
    }

    public int getBanStatus() {
        return banStatus;
    }

    private void setBanStatus(int banStatus) {
        this.banStatus = banStatus;
    }

    public String getMsg() {
        return msg;
    }

    private void setMsg(String msg) {
        this.msg = msg;
    }

    public int value() {
        return banStatus;
    }
}
