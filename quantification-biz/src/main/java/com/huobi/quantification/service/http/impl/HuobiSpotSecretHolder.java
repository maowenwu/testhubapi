package com.huobi.quantification.service.http.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.enums.ExchangeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huobi.quantification.common.api.HuobiSignature;
import com.huobi.quantification.entity.QuanAccountSecret;
import com.huobi.quantification.service.account.SpotAccountService;

/**
 * 将数据库配置的所有密钥对读取到map
 *
 * @author lichenyang
 * @since 2018年7月17日
 */
@Component
public class HuobiSpotSecretHolder {

    @Autowired
    private SpotAccountService spotAccountService;

    private Map<Long, List<QuanAccountSecret>> map = new ConcurrentHashMap<>();

    private Map<Long, AtomicLong> usageCounter = new HashMap<>();

    @PostConstruct
    public void loadAllSecret() {
        List<QuanAccount> accounts = spotAccountService.selectByExId(ExchangeEnum.HUOBI.getExId());
        for (QuanAccount account : accounts) {
            List<QuanAccountSecret> secretList = spotAccountService.selectSecretById(account.getId());
            map.put(account.getAccountSourceId(), secretList);
            usageCounter.put(account.getAccountSourceId(), new AtomicLong(0));
        }
    }

    public HuobiSignature getHuobiSpotSignature(Long accountSourceId) {
        List<QuanAccountSecret> secrets = map.get(accountSourceId);
        if (CollectionUtils.isEmpty(secrets)) {
            throw new RuntimeException(String.format("查找火币现货账户Secret失败，accountSourceId：", accountSourceId));
        }
        AtomicLong counter = usageCounter.get(accountSourceId);
        int index = (int) (counter.getAndIncrement() % secrets.size());
        QuanAccountSecret secret = secrets.get(index);
        return new HuobiSignature(secret.getAccessKey(), secret.getSecretKey());
    }
}
