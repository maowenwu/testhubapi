package com.huobi.quantification.service.account.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.util.StorageSupport;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.dao.QuanAccountSecretMapper;
import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountSecret;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.execeptions.APIException;
import com.huobi.quantification.response.spot.HuobiSpotAccountResponse;
import com.huobi.quantification.service.account.SpotAccountService;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */

@Service("spotAccountService")
@Transactional
public class SpotAccountServiceImpl implements SpotAccountService {

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
        logger.info("[HuobiSpotAccount]任务开始");
        Stopwatch started = Stopwatch.createStarted();
        Long accountId = quanAccountMapper.selectAccountId(ExchangeEnum.HUOBI.getExId(), accountSourceId);
        if (accountId == null) {
            logger.error("更新火币现货账户信息失败，账户不存在：accountSourceId={}", accountSourceId);
            return;
        }
        HuobiSpotAccountResponse response = queryAccountByAPI(accountSourceId);
        saveSpotAccount(response, accountId, accountSourceId);
        logger.info("[HuobiSpotAccount]任务结束，耗时：" + started);
    }

    private void saveSpotAccount(HuobiSpotAccountResponse response, Long accountId, Long accountSourceId) {
        List<QuanAccountAsset> accountAssets = parseSpotAccountAsset(response, accountId);
        boolean isSave = StorageSupport.getInstance("saveSpotAccount").checkSavepoint();
        if (isSave) {
            accountAssets.stream().forEach(e -> {
                quanAccountAssetMapper.insert(e);
            });
        }
        redisService.saveAccountSpot(accountAssets, ExchangeEnum.HUOBI.getExId(), accountSourceId);
    }

    public List<QuanAccountAsset> parseSpotAccountAsset(HuobiSpotAccountResponse response, Long accountId) {
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            List<QuanAccountAsset> accountAssets = new ArrayList<>();
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
            accountAssets.stream().forEach(e -> {
                e.setAccountId(accountId);
            });
            return accountAssets;
        } else {
            throw new RuntimeException("HuobiSpotAccountResponse 返回状态不为ok");
        }
    }

    private HuobiSpotAccountResponse queryAccountByAPI(Long accountId) {
        Map<String, String> params = new HashMap<>();
        params.put("account-id", accountId + "");
        String body = httpService.doHuobiSpotGet(accountId,
                HttpConstant.HUOBI_ACCOUNT.replaceAll("\\{account-id\\}", accountId + ""), params);
        HuobiSpotAccountResponse response = JSON.parseObject(body, HuobiSpotAccountResponse.class);
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            return response;
        }
        throw new APIException(body);
    }


    @Override
    public List<QuanAccount> selectByExId(int exId) {
        return quanAccountMapper.selectByExId(exId);
    }

    @Override
    public List<QuanAccountSecret> selectSecretById(Long accountId) {
        return quanAccountSecretMapper.selectByAccountId(accountId);
    }


    @Override
    public void initSpotAccountAsset() {
        List<QuanAccount> accountList = quanAccountMapper.selectAll();
        Map<Integer, List<QuanAccount>> accountMap = accountList.stream().collect(Collectors.groupingBy(e -> e.getExchangeId()));
        accountMap.forEach((k, v) -> {
            switch (ExchangeEnum.valueOf(k)) {
                case HUOBI:
                    initHuobiSpotAccountAsset(v);
                    break;
            }
        });
    }

    private void initHuobiSpotAccountAsset(List<QuanAccount> accountList) {
        accountList.forEach(e -> {
            List<QuanAccountAsset> assetList = quanAccountAssetMapper.selectInitedAssetByAccountId(e.getId());
            if (CollectionUtils.isEmpty(assetList)) {
                HuobiSpotAccountResponse response = queryAccountByAPI(e.getAccountSourceId());
                List<QuanAccountAsset> accountAssets = parseSpotAccountAsset(response, e.getId());
                accountAssets.stream().forEach(e2 -> {
                    e2.setInit(1);
                    quanAccountAssetMapper.insert(e2);
                });
            }
        });
    }
}
