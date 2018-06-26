package com.huobi.contract.index.contract.index.common;

/**
 * @desc
 * @Author mingjianyong
 */
public enum EmailType {

    TEXT(1), HTML(2);

    private int emailType;

    EmailType(int emailType) {
        this.emailType = emailType;
    }

    public int value() {
        return emailType;
    }

}
