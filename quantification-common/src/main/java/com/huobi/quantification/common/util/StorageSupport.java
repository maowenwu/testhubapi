package com.huobi.quantification.common.util;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageSupport {

    private static Map<String, StorageSupport> storageMap = new ConcurrentHashMap<>();

    private LocalDateTime currDateTime = null;

    private int saveInterval;

    public StorageSupport(int interval) {
        this.saveInterval = interval;
    }

    public boolean checkSavepoint() {
        if (currDateTime == null) {
            currDateTime = LocalDateTime.now();
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        if (currDateTime.plusSeconds(saveInterval).isBefore(now)) {
            currDateTime = now;
            return true;
        } else {
            return false;
        }
    }

    public static StorageSupport getInstance(String key, int interval) {
        StorageSupport storageSupport = storageMap.get(key);
        if (storageSupport == null) {
            StorageSupport support = new StorageSupport(interval);
            storageMap.put(key, support);
        }
        return storageMap.get(key);
    }

    public static StorageSupport getInstance(String key) {
        StorageSupport storageSupport = storageMap.get(key);
        if (storageSupport == null) {
            StorageSupport support = new StorageSupport(60);
            storageMap.put(key, support);
        }
        return storageMap.get(key);
    }
}
