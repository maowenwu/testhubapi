package com.huobi.quantification.service.account.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.huobi.quantification.common.util.StorageSupport;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.response.spot.HuobiSpotAccountResponse;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.dao.QuanAccountSecretMapper;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountSecret;
import com.huobi.quantification.service.account.HuobiAccountService;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
@DependsOn("httpServiceImpl")
@Service
@Transactional
public class HuobiAccountServiceImpl implements HuobiAccountService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpService httpService;
    @Autowired
    private QuanAccountMapper quanAccountMapper;
    @Autowired
    private QuanAccountAssetMapper quanAccountAssetMapper;
    @Autowired
    private QuanAccountSecretMapper quanAccountSecretMapper;
    @Autowired
    private RedisService redisService;


    @Override
    public void updateAccount(Long accountSourceId) {
        logger.info("[HuobiUserInfo]任务开始");
        Stopwatch started = Stopwatch.createStarted();
        Long accountId = quanAccountMapper.selectAccountId(ExchangeEnum.HUOBI.getExId(), accountSourceId);
        if (accountId == null) {
            logger.error("更新火币现货账户信息失败，账户不存在：accountSourceId={}", accountSourceId);
            return;
        }
        String body = queryAccountByAPI(accountSourceId);
        List<QuanAccountAsset> accountAssets = parseAccount(body);
        boolean isSave = StorageSupport.getInstance("updateSpotAccount").checkSavepoint();
        accountAssets.stream().forEach(e -> {
            e.setAccountId(accountId);
            if (isSave) {
                quanAccountAssetMapper.insert(e);
            }
        });
        if (CollectionUtils.isNotEmpty(accountAssets)) {
            redisService.saveAccountSpot(accountAssets, ExchangeEnum.HUOBI.getExId(), accountSourceId);
        }
        logger.info("[HuobiUserInfo]任务结束，耗时：" + started);
    }

    private String queryAccountByAPI(Long accountId) {
        Map<String, String> params = new HashMap<>();
        params.put("account-id", accountId + "");
        String body = httpService.doHuobiGet(accountId,
                HttpConstant.HUOBI_ACCOUNT.replaceAll("\\{account-id\\}", accountId + ""), params);
        return body;
    }

    private List<QuanAccountAsset> parseAccount(String body) {
        HuobiSpotAccountResponse response = JSON.parseObject(body, HuobiSpotAccountResponse.class);
        List<QuanAccountAsset> accountAssets = new ArrayList<>();
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            List<HuobiSpotAccountResponse.DataBean.ListBean> listBeans = response.getData().getList();
            if (CollectionUtils.isNotEmpty(listBeans)) {
                Map<String, List<HuobiSpotAccountResponse.DataBean.ListBean>> listMap = listBeans.stream().collect(Collectors.groupingBy(e -> e.getCurrency()));
                listMap.forEach((k, v) -> {
                    QuanAccountAsset accountAsset = new QuanAccountAsset();
                    v.forEach(e -> {
                        if ("trade".equalsIgnoreCase(e.getType())) {
                            accountAsset.setAvailable(e.getBalance());
                        }
                        if ("frozen".equalsIgnoreCase(e.getType())) {
                            accountAsset.setFrozen(e.getBalance());
                        }
                    });
                    accountAsset.setCoinType(k);
                    accountAsset.setTotal(accountAsset.getAvailable().add(accountAsset.getFrozen()));
                    accountAsset.setInit(0);
                    accountAsset.setCreateTime(new Date());
                    accountAsset.setUpdateTime(new Date());
                    accountAssets.add(accountAsset);
                });
            }
        }
        return accountAssets;
    }


    @Override
    public List<Long> findAccountByExchangeId(int exId) {
        return quanAccountMapper.selectAccountByExchangeId(exId);
    }

    @Override
    public List<QuanAccountSecret> findAccountSecretById(Long accountId) {
        return quanAccountSecretMapper.selectByAccountId(accountId);
    }


}
