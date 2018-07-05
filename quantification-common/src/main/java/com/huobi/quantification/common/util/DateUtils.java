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











}