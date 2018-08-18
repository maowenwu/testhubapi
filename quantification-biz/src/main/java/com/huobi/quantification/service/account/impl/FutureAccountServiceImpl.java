package com.huobi.quantification.service.account.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.common.util.StorageSupport;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.dao.QuanAccountFuturePositionMapper;
import com.huobi.quantification.dao.QuanAccountFutureSecretMapper;
import com.huobi.quantification.entity.*;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.response.future.HuobiFuturePositionResponse;
import com.huobi.quantification.response.future.HuobiFutureUserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.service.account.FutureAccountService;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;

@Service("futureAccountService")
@Transactional
public class FutureAccountServiceImpl implements FutureAccountService {

    @Autowired
    private HttpService httpService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QuanAccountFutureSecretMapper quanAccountFutureSecretMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String queryUserInfoByAPI(Long accountId) {
        HashMap<String, String> params = new HashMap<>();
        String body = httpService.doHuobiFuturePostJson(accountId, HttpConstant.HUOBI_FUTURE_ACCOUNT_INFO, params);
        return body;
    }

    @Override
    public String queryPositionByAPI(Long accountId) {
        HashMap<String, String> params = new HashMap<>();
        String body = httpService.doHuobiFuturePostJson(accountId, HttpConstant.HUOBI_FUTURE_POSITION_INFO, params);
        return body;
    }

    @Autowired
    private QuanAccountFutureMapper quanAccountFutureMapper;

    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    @Override
    public void updateHuobiUserInfo(Long accountSourceId) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiUserInfo][accountSourceId={}]任务开始", accountSourceId);
        Long accountFutureId = quanAccountFutureMapper.selectAccountFutureId(ExchangeEnum.HUOBI_FUTURE.getExId(), accountSourceId);
        if (accountFutureId == null) {
            logger.error("更新火币期货账户信息失败，不存在该账户：accountSourceId={}", accountSourceId);
            return;
        }
        long queryId = System.currentTimeMillis();
        String body = queryUserInfoByAPI(accountSourceId);
        Map<String, QuanAccountFutureAsset> assetMap = parseHuobiFutureBalance(body);
        boolean isSave = StorageSupport.getInstance("updateHuobiUserInfo").checkSavepoint();
        assetMap.forEach((k, v) -> {
            v.setCoinType(k);
            v.setQueryId(queryId);
            v.setAccountFutureId(accountFutureId);
            if (isSave) {
                quanAccountFutureAssetMapper.insert(v);
            }
        });
        redisService.saveUserInfoFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), accountSourceId, assetMap);
        logger.info("[HuobiUserInfo][accountId={}]任务结束，耗时：" + started, accountSourceId);
    }

    private Map<String, QuanAccountFutureAsset> parseHuobiFutureBalance(String body) {
        HuobiFutureUserInfoResponse response = JSON.parseObject(body, HuobiFutureUserInfoResponse.class);
        Map<String, QuanAccountFutureAsset> data = new ConcurrentHashMap<>();
        List<HuobiFutureUserInfoResponse.DataBean> dataBeans = response.getData();
        for (HuobiFutureUserInfoResponse.DataBean dataBean : dataBeans) {
            data.put(dataBean.getSymbol().toLowerCase(), convertToDto(dataBean));
        }
        return data;
    }

    private QuanAccountFutureAsset convertToDto(HuobiFutureUserInfoResponse.DataBean dataBean) {
        QuanAccountFutureAsset futureAsset = new QuanAccountFutureAsset();
        futureAsset.setMarginBalance(dataBean.getMarginBalance());
        // 持仓保证金
        futureAsset.setMarginPosition(dataBean.getMarginPosition());
        futureAsset.setMarginFrozen(dataBean.getMarginFrozen());
        futureAsset.setMarginAvailable(dataBean.getMarginAvailable());
        futureAsset.setProfitReal(dataBean.getProfitReal());
        futureAsset.setProfitUnreal(dataBean.getProfitUnreal());
        futureAsset.setRiskRate(dataBean.getRiskRate());
        futureAsset.setLiquidationPrice(dataBean.getLiquidationPrice());
        futureAsset.setInit(0);
        futureAsset.setCreateTime(new Date());
        futureAsset.setUpdateTime(new Date());
        return futureAsset;
    }

    @Autowired
    private QuanAccountFuturePositionMapper quanAccountFuturePositionMapper;

    @Override
    public void updateHuobiPosition(Long accountSourceId) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiPosition][accountId={}]任务开始", accountSourceId);
        long queryId = System.currentTimeMillis();
        Long accountFutureId = quanAccountFutureMapper.selectAccountFutureId(ExchangeEnum.HUOBI.getExId(), accountSourceId);
        String body = queryPositionByAPI(accountSourceId);
        List<QuanAccountFuturePosition> futurePositions = parseHuobiFuturePosition(body);
        boolean isSave = StorageSupport.getInstance("updateHuobiPosition").checkSavepoint();
        futurePositions.forEach(e -> {
            e.setAccountFutureId(accountFutureId);
            e.setQueryId(queryId);
            if (isSave) {
                quanAccountFuturePositionMapper.insert(e);
            }
        });
        redisService.savePositionFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), accountSourceId, futurePositions);
        logger.info("[HuobiPosition][accountId={},]任务结束，耗时：" + started, accountSourceId);
    }

    private List<QuanAccountFuturePosition> parseHuobiFuturePosition(String body) {
        HuobiFuturePositionResponse response = JSON.parseObject(body, HuobiFuturePositionResponse.class);
        List<QuanAccountFuturePosition> beanList = new ArrayList<>();
        response.getData().forEach((e) -> {
            QuanAccountFuturePosition futurePosition = new QuanAccountFuturePosition();
            futurePosition.setContractCode(e.getContractCode());
            futurePosition.setBaseCoin(e.getSymbol());
            futurePosition.setQuoteCoin("usdt");
            futurePosition.setContractType(e.getContractType());
            if ("buy".equalsIgnoreCase(e.getDirection())) {
                futurePosition.setOffset(OffsetEnum.OPEN.getOffset());
                futurePosition.setAmount(e.getVolume());
                futurePosition.setAvailable(e.getAvailable());
                // 多仓冻结张数
                futurePosition.setFrozen(e.getFrozen());
                futurePosition.setCostOpen(e.getCostOpen());
                // 多仓持仓均价
                futurePosition.setCostHold(e.getCostHold());
            } else {
                futurePosition.setOffset(OffsetEnum.CLOSE.getOffset());
                futurePosition.setAmount(e.getVolume());
                futurePosition.setAvailable(e.getAvailable());
                // 多仓冻结张数
                futurePosition.setFrozen(e.getFrozen());
                futurePosition.setCostOpen(e.getCostOpen());
                // 多仓持仓均价
                futurePosition.setCostHold(e.getCostHold());
            }
            futurePosition.setLeverRate(e.getLeverRate());
            futurePosition.setCreateTime(new Date());
            futurePosition.setUpdateTime(new Date());
            beanList.add(futurePosition);
        });
        return beanList;
    }


    @Override
    public List<QuanAccountFuture> selectByExId(int exId) {
        return quanAccountFutureMapper.selectByExId(exId);
    }

    @Override
    public List<QuanAccountFutureSecret> selectSecretById(Long id) {
        return quanAccountFutureSecretMapper.selectSecretById(id);
    }
}
