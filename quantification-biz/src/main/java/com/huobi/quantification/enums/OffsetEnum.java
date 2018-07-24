package com.huobi.quantification.enums;

public enum OffsetEnum {

    /**
     * 开多
     */
    LONG(1),
    /**
     * 开空
     */
    SHORT(2);

    private int offset;

    OffsetEnum(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
