package com.huobi.quantification.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;


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


    public static LocalDateTime getFriday(String time) {
        TemporalField fieldISO = WeekFields.of(Locale.CHINA).dayOfWeek();
        LocalDate localDate = LocalDate.now().with(fieldISO, 6);
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
        return LocalDateTime.of(localDate, localTime);
    }

    public static long getSecond(String time1, String time2) {
        LocalTime t1 = LocalTime.parse(time1, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime t2 = LocalTime.parse(time2, DateTimeFormatter.ISO_LOCAL_TIME);
        return Duration.between(t1, t2).toMillis() / 1000;
    }

    public static long getSecond(LocalTime time1, LocalTime time2) {
        return Duration.between(time1, time2).toMillis() / 1000;
    }
}