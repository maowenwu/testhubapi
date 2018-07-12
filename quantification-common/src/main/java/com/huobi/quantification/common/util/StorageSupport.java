package com.huobi.quantification.common.util;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageSupport {

    private static Map<String, LocalDateTime> map = new ConcurrentHashMap<>();

    private static final int saveInterval = 60;

    public synchronized static boolean checkSavepoint(String key) {
        LocalDateTime dateTime = map.get(key);
        if (dateTime == null) {
            map.put(key, LocalDateTime.now());
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.plusSeconds(saveInterval).isBefore(now)) {
            map.put(key, now);
            return true;
        } else {
            return false;
        }
    }
}
