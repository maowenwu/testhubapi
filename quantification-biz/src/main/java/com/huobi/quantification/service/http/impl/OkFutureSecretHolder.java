package com.huobi.quantification.service.http.impl;

import com.huobi.quantification.common.api.OkSignature;
import com.huobi.quantification.entity.QuanAccountFutureSecret;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.service.account.OkFutureAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OkFutureSecretHolder {

    @Autowired
    private OkFutureAccountService okFutureAccountService;

    private Map<Long, List<QuanAccountFutureSecret>> map = new ConcurrentHashMap<>();

    private Map<Long, Long> accountUsageCounter = new HashMap<>();

    //@PostConstruct
    public void loadAllSecret() {
        List<Long> accountIds = okFutureAccountService.findAccountFutureByExchangeId(ExchangeEnum.OKEX.getExId());
        if (accountIds.size() <= 0) {
            throw new RuntimeException("quan_account_future表未初始化账户数据");
        }
        for (Long accountId : accountIds) {
            List<QuanAccountFutureSecret> secretList = okFutureAccountService.findAccountFutureSecretById(accountId);
            if (secretList.size() <= 0) {
                throw new RuntimeException("账户[" + accountId + "]，未配置对应的accessKey");
            }
            map.put(accountId, secretList);
            accountUsageCounter.put(accountId, 0L);
        }
    }

    public synchronized OkSignature getOkSignatureById(Long accountId) {
        List<QuanAccountFutureSecret> secrets = map.get(accountId);
        Long counter = accountUsageCounter.getOrDefault(accountId,0L);
        int index = (int) (counter % secrets.size());
        QuanAccountFutureSecret secret = secrets.get(index);
        accountUsageCounter.put(accountId, counter + 1);
        OkSignature okSignature = new OkSignature(secret.getAccessKey(), secret.getSecretKey());
        return okSignature;
    }

}
