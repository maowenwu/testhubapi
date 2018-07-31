package com.huobi.quantification.enums;

public enum OffsetEnum {

    /**
     * 多仓
     */
    LONG(1),
    /**
     * 空仓
     */
    SHORT(2);

    private int offset;

    OffsetEnum(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }


    public static OffsetEnum valueOf(int offset) {
        for (OffsetEnum offsetEnum : values()) {
            if (offsetEnum.getOffset() == offset) {
                return offsetEnum;
            }
        }
        throw new RuntimeException("OffsetEnum 不存在，offset=" + offset);
    }
}
