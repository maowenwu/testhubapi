package com.huobi.quantification.service.http.impl;

import com.huobi.quantification.common.api.HuobiSignature;
import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureSecret;
import com.huobi.quantification.entity.QuanAccountSecret;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.service.account.FutureAccountService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 将数据库配置的所有密钥对读取到map
 *
 * @author lichenyang
 * @since 2018年7月17日
 */
@Component
public class HuobiFutureSecretHolder {

    @Autowired
    private FutureAccountService futureAccountService;

    private Map<Long, List<QuanAccountFutureSecret>> map = new ConcurrentHashMap<>();

    private Map<Long, AtomicLong> usageCounter = new HashMap<>();

    @PostConstruct
    public void loadAllSecret() {
        List<QuanAccountFuture> accounts = futureAccountService.selectByExId(ExchangeEnum.HUOBI_FUTURE.getExId());
        for (QuanAccountFuture account : accounts) {
            List<QuanAccountFutureSecret> secretList = futureAccountService.selectSecretById(account.getId());
            map.put(account.getAccountSourceId(), secretList);
            usageCounter.put(account.getAccountSourceId(), new AtomicLong(0));
        }
    }

    public HuobiSignature getHuobiFutureSignature(Long accountSourceId) {
        List<QuanAccountFutureSecret> secrets = map.get(accountSourceId);
        if (CollectionUtils.isEmpty(secrets)) {
            throw new RuntimeException(String.format("查找火币期货账户Secret失败，accountSourceId：", accountSourceId));
        }
        AtomicLong counter = usageCounter.get(accountSourceId);
        int index = (int) (counter.getAndIncrement() % secrets.size());
        QuanAccountFutureSecret secret = secrets.get(index);
        return new HuobiSignature(secret.getAccessKey(), secret.getSecretKey());
    }
}
