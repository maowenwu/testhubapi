package com.huobi.quantification.common.util;

import java.math.BigDecimal;

public class BigDecimalUtils {
    /**
     * 比较param1与param2的大小,大于等于 true，
     *
     * @param param1
     * @param param2
     * @return
     */
    public static Boolean moreThanOrEquals(BigDecimal param1, BigDecimal param2) {
        int compareResult = param1.compareTo(param2);
        if (compareResult >= 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 比较param1与param2的大小
     *
     * @param param1
     * @param param2
     * @return
     */
    public static Boolean moreThan(BigDecimal param1, BigDecimal param2) {
        if (param1.compareTo(param2) > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 比较 param1 是否小于 param2
     *
     * @param param1
     * @param param2
     * @return
     */
    public static Boolean lessThan(BigDecimal param1, BigDecimal param2) {
        if (param1.compareTo(param2) < 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 比较 param1 是否小于等于 param2
     *
     * @param param1
     * @param param2
     * @return
     */
    public static Boolean lessThanOrEquals(BigDecimal param1, BigDecimal param2) {
        int compareResult = param1.compareTo(param2);
        if (compareResult <= 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 比较param1与param2的大小 是否相等
     *
     * @param param1
     * @param param2
     * @return
     */
    public static Boolean equals(BigDecimal param1, BigDecimal param2) {
        if (0 == param1.compareTo(param2)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        if (lessThan(a, b)) {
            return a;
        } else {
            return b;
        }
    }
}
