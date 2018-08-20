package com.huobi.quantification.service.account.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.util.StorageSupport;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.dao.QuanAccountFuturePositionMapper;
import com.huobi.quantification.dao.QuanAccountFutureSecretMapper;
import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.entity.QuanAccountFutureSecret;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.execeptions.APIException;
import com.huobi.quantification.response.future.HuobiFuturePositionResponse;
import com.huobi.quantification.response.future.HuobiFutureUserInfoResponse;
import com.huobi.quantification.service.account.FutureAccountService;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    public HuobiFutureUserInfoResponse queryUserInfoByAPI(Long accountId) {
        HashMap<String, String> params = new HashMap<>();
        String body = httpService.doHuobiFuturePostJson(accountId, HttpConstant.HUOBI_FUTURE_ACCOUNT_INFO, params);
        HuobiFutureUserInfoResponse response = JSON.parseObject(body, HuobiFutureUserInfoResponse.class);
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
        HuobiFutureUserInfoResponse response = queryUserInfoByAPI(accountSourceId);
        saveFutureAccount(response, accountFutureId, accountSourceId);
        logger.info("[HuobiUserInfo][accountId={}]任务结束，耗时：" + started, accountSourceId);
    }

    private void saveFutureAccount(HuobiFutureUserInfoResponse response, Long accountFutureId, Long accountSourceId) {
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            long queryId = System.currentTimeMillis();
            Map<String, QuanAccountFutureAsset> data = new ConcurrentHashMap<>();
            List<HuobiFutureUserInfoResponse.DataBean> dataBeans = response.getData();
            for (HuobiFutureUserInfoResponse.DataBean dataBean : dataBeans) {
                data.put(dataBean.getSymbol().toLowerCase(), convertToDto(dataBean));
            }
            boolean isSave = StorageSupport.getInstance("updateHuobiAccount").checkSavepoint();
            data.forEach((k, v) -> {
                v.setCoinType(k);
                v.setQueryId(queryId);
                v.setAccountFutureId(accountFutureId);
                if (isSave) {
                    quanAccountFutureAssetMapper.insert(v);
                }
            });
            redisService.saveAccountFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), accountSourceId, data);
        }
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
                futurePosition.setProfitUnreal(e.getProfitUnreal());
                futurePosition.setProfitRate(e.getProfitRate());
                futurePosition.setLeverRate(e.getLeverRate());
                futurePosition.setCreateTime(new Date());
                futurePosition.setUpdateTime(new Date());
                beanList.add(futurePosition);
            });
            boolean isSave = StorageSupport.getInstance("saveFuturePosition").checkSavepoint();
            beanList.forEach(e -> {
                e.setAccountFutureId(accountFutureId);
                e.setQueryId(queryId);
                if (isSave) {
                    quanAccountFuturePositionMapper.insert(e);
                }
            });
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
}
