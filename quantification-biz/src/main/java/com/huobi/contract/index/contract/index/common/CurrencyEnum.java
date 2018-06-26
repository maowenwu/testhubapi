package com.huobi.contract.index.contract.index.common;

import java.util.ArrayList;
import java.util.List;

public enum CurrencyEnum {
    CNY("人民币", "CNY"),
    USD("美元", "USD"),
    EUR("欧元", "EUR"),
    HKD("港币", "HKD"),
    GBP("英镑", "GBP"),
    JPY("日元", "JPY"),
    KRW("韩元", "KRW"),
    CAD("加元", "CAD"),
    AUD("澳元", "AUD"),
    SGD("新加坡元", "SGD");

    private String name;
    private String shortName;

    CurrencyEnum(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }
    public static List<String> listShortNames(){
        CurrencyEnum[] le = CurrencyEnum.values();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < le.length; i++) {
            list.add(le[i].shortName);
        }
        return list;
    }
    public static boolean isContainShortName(String shortName){
        try {
            CurrencyEnum.valueOf(shortName);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    private void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
