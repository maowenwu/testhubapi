package com.huobi.quantification.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class DateUtils {

    public static Date plusMinutes(Date date, long minutes) {
        return new Date(date.getTime() + 60 * 1000 * minutes);
    }

    public static String format(Date date, String pattern) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }


    /**
     * 判断时间是否在允许的最大延时内
     *
     * @param date     更新时间
     * @param maxDelay 单位秒
     * @return
     */
    public static boolean withinMaxDelay(Date date, long maxDelay) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        if (LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).plusSeconds(maxDelay).isAfter(LocalDateTime.now())) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[] args) {
        Date date = new Date(1531389988379L);
        System.out.println(withinMaxDelay(date, 150));
    }


}