package com.huobi.quantification.service.http.impl;

import com.huobi.quantification.common.api.OkSignature;
import com.huobi.quantification.entity.QuanAccountFutureSecret;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.service.account.FutureAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OkFutureSecretHolder {

    @Autowired
    private FutureAccountService futureAccountService;

    private Map<Long, List<QuanAccountFutureSecret>> map = new ConcurrentHashMap<>();

    private Map<Long, Long> accountUsageCounter = new HashMap<>();

    //@PostConstruct
    public void loadAllSecret() {

    }

    public OkSignature getOkSignatureById(Long accountId) {
        List<QuanAccountFutureSecret> secrets = map.get(accountId);
        Long counter = accountUsageCounter.getOrDefault(accountId, 0L);
        int index = (int) (counter % secrets.size());
        QuanAccountFutureSecret secret = secrets.get(index);
        accountUsageCounter.put(accountId, counter + 1);
        OkSignature okSignature = new OkSignature(secret.getAccessKey(), secret.getSecretKey());
        return okSignature;
    }

}
