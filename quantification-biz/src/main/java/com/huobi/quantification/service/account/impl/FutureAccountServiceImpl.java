package com.huobi.quantification.service.account.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.util.StorageSupport;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.dao.QuanAccountFuturePositionMapper;
import com.huobi.quantification.dao.QuanAccountFutureSecretMapper;
import com.huobi.quantification.entity.*;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.execeptions.APIException;
import com.huobi.quantification.response.future.HuobiFuturePositionResponse;
import com.huobi.quantification.response.future.HuobiFutureAccountResponse;
import com.huobi.quantification.service.account.FutureAccountService;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    public HuobiFutureAccountResponse queryAccountByAPI(Long accountId) {
        HashMap<String, String> params = new HashMap<>();
        String body = httpService.doHuobiFuturePostJson(accountId, HttpConstant.HUOBI_FUTURE_ACCOUNT_INFO, params);
        HuobiFutureAccountResponse response = JSON.parseObject(body, HuobiFutureAccountResponse.class);
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            return response;
        }
        throw new APIException(body);
    }

    @Override
    public HuobiFuturePositionResponse queryPositionByAPI(Long accountId) {
        HashMap<String, String> params = new HashMap<>();
        String body = httpService.doHuobiFuturePostJson(accountId, HttpConstant.HUOBI_FUTURE_POSITION_INFO, params);
        HuobiFuturePositionResponse response = JSON.parseObject(body, HuobiFuturePositionResponse.class);
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            return response;
        }
        throw new APIException(body);
    }

    @Autowired
    private QuanAccountFutureMapper quanAccountFutureMapper;

    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    @Override
    public void updateHuobiAccount(Long accountSourceId) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiUserInfo][accountSourceId={}]任务开始", accountSourceId);
        Long accountFutureId = quanAccountFutureMapper.selectAccountFutureId(ExchangeEnum.HUOBI_FUTURE.getExId(), accountSourceId);
        if (accountFutureId == null) {
            logger.error("更新火币期货账户信息失败，不存在该账户：accountSourceId={}", accountSourceId);
            return;
        }
        HuobiFutureAccountResponse response = queryAccountByAPI(accountSourceId);
        saveFutureAccount(response, accountFutureId, accountSourceId);
        logger.info("[HuobiUserInfo][accountId={}]任务结束，耗时：" + started, accountSourceId);
    }

    private void saveFutureAccount(HuobiFutureAccountResponse response, Long accountFutureId, Long accountSourceId) {
        Map<String, QuanAccountFutureAsset> dataMap = parseFutureAccountAsset(response, accountFutureId);
        boolean isSave = StorageSupport.getInstance("saveFutureAccount").checkSavepoint();
        if (isSave) {
            dataMap.forEach((k, v) -> {
                quanAccountFutureAssetMapper.insert(v);
            });
        }
        redisService.saveAccountFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), accountSourceId, dataMap);
    }

    public Map<String, QuanAccountFutureAsset> parseFutureAccountAsset(HuobiFutureAccountResponse response, Long accountFutureId) {
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            long queryId = System.currentTimeMillis();
            Map<String, QuanAccountFutureAsset> dataMap = new ConcurrentHashMap<>();
            List<HuobiFutureAccountResponse.DataBean> dataBeans = response.getData();
            for (HuobiFutureAccountResponse.DataBean dataBean : dataBeans) {
                dataMap.put(dataBean.getSymbol().toLowerCase(), convertToDto(dataBean));
            }
            dataMap.forEach((k, v) -> {
                v.setCoinType(k);
                v.setQueryId(queryId);
                v.setAccountFutureId(accountFutureId);
            });
            return dataMap;
        } else {
            throw new RuntimeException("FutureAccountAsset 返回状态不为ok");
        }
    }

    private QuanAccountFutureAsset convertToDto(HuobiFutureAccountResponse.DataBean dataBean) {
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
        Long accountFutureId = quanAccountFutureMapper.selectAccountFutureId(ExchangeEnum.HUOBI_FUTURE.getExId(), accountSourceId);
        HuobiFuturePositionResponse response = queryPositionByAPI(accountSourceId);
        saveFuturePosition(response, accountFutureId, accountSourceId);
        logger.info("[HuobiPosition][accountId={},]任务结束，耗时：" + started, accountSourceId);
    }

    private void saveFuturePosition(HuobiFuturePositionResponse response, Long accountFutureId, Long accountSourceId) {
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            long queryId = System.currentTimeMillis();
            List<QuanAccountFuturePosition> beanList = new ArrayList<>();
            response.getData().forEach((e) -> {
                QuanAccountFuturePosition futurePosition = new QuanAccountFuturePosition();
                futurePosition.setContractCode(e.getContractCode());
                futurePosition.setBaseCoin(e.getSymbol());
                futurePosition.setQuoteCoin("usdt");
                futurePosition.setContractType(e.getContractType());
                if ("buy".equalsIgnoreCase(e.getDirection())) {
                    futurePosition.setSide(SideEnum.BUY.getSideType());
                    futurePosition.setAmount(e.getVolume());
                    futurePosition.setAvailable(e.getAvailable());
                    // 多仓冻结张数
                    futurePosition.setFrozen(e.getFrozen());
                    futurePosition.setCostOpen(e.getCostOpen());
                    // 多仓持仓均价
                    futurePosition.setCostHold(e.getCostHold());
                } else {
                    futurePosition.setSide(SideEnum.SELL.getSideType());
                    futurePosition.setAmount(e.getVolume());
                    futurePosition.setAvailable(e.getAvailable());
                    // 多仓冻结张数
                    futurePosition.setFrozen(e.getFrozen());
                    futurePosition.setCostOpen(e.getCostOpen());
                    // 多仓持仓均价
                    futurePosition.setCostHold(e.getCostHold());
                }
                futurePosition.setProfitUnreal(e.getProfitUnreal());
                futurePosition.setProfitRate(e.getProfitRate());
                futurePosition.setLeverRate(e.getLeverRate());
                futurePosition.setCreateTime(new Date());
                futurePosition.setUpdateTime(new Date());
                beanList.add(futurePosition);
            });
            boolean isSave = StorageSupport.getInstance("saveFuturePosition").checkSavepoint();
            if (isSave) {
                beanList.forEach(e -> {
                    e.setAccountFutureId(accountFutureId);
                    e.setQueryId(queryId);
                    quanAccountFuturePositionMapper.insert(e);
                });
            }
            redisService.savePositionFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), accountSourceId, beanList);
        }

    }


    @Override
    public List<QuanAccountFuture> selectByExId(int exId) {
        return quanAccountFutureMapper.selectByExId(exId);
    }

    @Override
    public List<QuanAccountFutureSecret> selectSecretById(Long id) {
        return quanAccountFutureSecretMapper.selectSecretById(id);
    }

    @Override
    public void initFutureAccountAsset() {
        List<QuanAccountFuture> accountList = quanAccountFutureMapper.selectAll();
        Map<Integer, List<QuanAccountFuture>> accountMap = accountList.stream().collect(Collectors.groupingBy(e -> e.getExchangeId()));
        accountMap.forEach((k, v) -> {
            switch (ExchangeEnum.valueOf(k)) {
                case HUOBI_FUTURE:
                    initHuobiFutureAccountAsset(v);
                    break;
            }
        });
    }

    private void initHuobiFutureAccountAsset(List<QuanAccountFuture> accountList) {
        accountList.forEach(e -> {
            List<QuanAccountFutureAsset> assetList = quanAccountFutureAssetMapper.selectInitedAssetByAccountFutureId(e.getId());
            if (CollectionUtils.isEmpty(assetList)) {
                HuobiFutureAccountResponse response = queryAccountByAPI(e.getAccountSourceId());
                Map<String, QuanAccountFutureAsset> dataMap = parseFutureAccountAsset(response, e.getId());
                dataMap.forEach((k, v) -> {
                    v.setInit(1);
                    quanAccountFutureAssetMapper.insert(v);
                });
            }
        });
    }
}
