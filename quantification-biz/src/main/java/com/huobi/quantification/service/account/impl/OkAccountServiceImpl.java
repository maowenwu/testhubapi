package com.huobi.quantification.service.account.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.dao.QuanAccountFuturePositionMapper;
import com.huobi.quantification.dao.QuanAccountFutureSecretMapper;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.entity.QuanAccountFutureSecret;
import com.huobi.quantification.service.account.OkAccountService;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@DependsOn("httpServiceImpl")
@Service
@Transactional
public class OkAccountServiceImpl implements OkAccountService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpService httpService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    @Autowired
    private QuanAccountFuturePositionMapper quanAccountFuturePositionMapper;

    @Resource
    private QuanAccountFutureMapper quanAccountFutureMapper;

    @Resource
    private QuanAccountFutureSecretMapper quanAccountFutureSecretMapper;


    public void updateOkUserInfo(Long accountId) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[OkUserInfo][accountId={}]任务开始", accountId);
        long queryId = System.currentTimeMillis();
        List<QuanAccountFutureAsset> list = queryOkUserInfoByAPI(accountId);
        for (QuanAccountFutureAsset asset : list) {
            asset.setQueryId(queryId);
            asset.setAccountSourceId(accountId);
            quanAccountFutureAssetMapper.insert(asset);
        }
        redisService.saveOkUserInfo(accountId, list);
        logger.info("[OkUserInfo][accountId={}]任务结束，耗时：" + started, accountId);
    }

    private List<QuanAccountFutureAsset> queryOkUserInfoByAPI(Long accountId) {
        Map<String, String> params = new HashMap<>();
        String body = httpService.doOkSignedPost(accountId, HttpConstant.OK_USER_INFO, params);
        return parseAndSaveUserInfo(body);
    }

    private List<QuanAccountFutureAsset> parseAndSaveUserInfo(String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        Boolean b = jsonObject.getBoolean("result");
        List<QuanAccountFutureAsset> list = new ArrayList<>();
        if (b) {
            JSONObject info = jsonObject.getJSONObject("info");
            list.add(parseFutureAsset(info.getJSONObject("btc"), "btc"));
            list.add(parseFutureAsset(info.getJSONObject("btg"), "btg"));
            list.add(parseFutureAsset(info.getJSONObject("etc"), "etc"));
            list.add(parseFutureAsset(info.getJSONObject("bch"), "bch"));
            list.add(parseFutureAsset(info.getJSONObject("xrp"), "xrp"));
            list.add(parseFutureAsset(info.getJSONObject("eth"), "eth"));
            list.add(parseFutureAsset(info.getJSONObject("eos"), "eos"));
            list.add(parseFutureAsset(info.getJSONObject("ltc"), "ltc"));
        }
        return list;
    }

    private QuanAccountFutureAsset parseFutureAsset(JSONObject obj, String symbol) {
        QuanAccountFutureAsset asset = new QuanAccountFutureAsset();
        asset.setSymbol(symbol);
        asset.setRiskRate(obj.getBigDecimal("risk_rate"));
        asset.setAccountRights(obj.getBigDecimal("account_rights"));
        asset.setProfitUnreal(obj.getBigDecimal("profit_unreal"));
        asset.setProfitReal(obj.getBigDecimal("profit_real"));
        asset.setKeepDeposit(obj.getBigDecimal("keep_deposit"));
        return asset;
    }


    public void updateOkPosition(Long accountId, String symbol, String contractType) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[OkPosition][symbol={},contractType={}]任务开始", symbol, contractType);
        long queryId = System.currentTimeMillis();
        List<QuanAccountFuturePosition> list = queryOkPositionByAPI(accountId, symbol, contractType);
        for (QuanAccountFuturePosition position : list) {
            position.setQueryId(queryId);
            position.setAccountSourceId(accountId);
            quanAccountFuturePositionMapper.insert(position);
        }
        redisService.saveOkPosition(accountId, symbol, contractType, list);
        logger.info("[OkPosition][symbol={},contractType={}]任务结束，耗时：" + started, symbol, contractType);
    }

    public List<QuanAccountFuturePosition> queryOkPositionByAPI(Long accountId, String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        String body = httpService.doOkSignedPost(accountId, HttpConstant.OK_POSITION, params);
        return parseAndSavePosition(body);
    }

    private List<QuanAccountFuturePosition> parseAndSavePosition(String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        Boolean b = jsonObject.getBoolean("result");
        List<QuanAccountFuturePosition> list = new ArrayList<>();
        if (b) {
            String liquPrice = jsonObject.getString("force_liqu_price").replaceAll(",", "");
            BigDecimal forceLiquPrice = new BigDecimal(liquPrice);
            JSONArray holding = jsonObject.getJSONArray("holding");
            for (int i = 0; i < holding.size(); i++) {
                JSONObject holdingJSONObject = holding.getJSONObject(i);
                list.add(parseFuturePosition(holdingJSONObject, forceLiquPrice));
            }
        }
        return list;
    }

    private QuanAccountFuturePosition parseFuturePosition(JSONObject obj, BigDecimal forceLiquPrice) {
        QuanAccountFuturePosition position = new QuanAccountFuturePosition();
        position.setForceLiquPrice(forceLiquPrice);
        position.setBuyAmount(obj.getBigDecimal("buy_amount"));
        position.setBuyAvailable(obj.getBigDecimal("buy_available"));
        position.setBuyPriceAvg(obj.getBigDecimal("buy_price_avg"));
        position.setBuyPriceCost(obj.getBigDecimal("buy_price_cost"));
        position.setBuyProfitReal(obj.getBigDecimal("buy_profit_real"));
        position.setContractCode(obj.getString("contract_id"));
        position.setContractName(obj.getString("contract_type"));
        position.setLeverRate(obj.getBigDecimal("lever_rate"));
        position.setSellAmount(obj.getBigDecimal("sell_amount"));
        position.setSellAvailable(obj.getBigDecimal("sell_available"));
        position.setSellPriceAvg(obj.getBigDecimal("sell_price_avg"));
        position.setSellPriceCost(obj.getBigDecimal("sell_price_cost"));
        position.setSellProfitReal(obj.getBigDecimal("sell_profit_real"));
        position.setSymbol(obj.getString("symbol"));
        position.setDateCreate(new Date(obj.getLong("create_date")));
        return position;
    }

    @Override
    public List<Long> findAccountFutureByExchangeId(int exchangeId) {
        List<Long> list = quanAccountFutureMapper.selectByExchangeId(exchangeId);
        return list;
    }

    @Override
    public List<QuanAccountFutureSecret> findAccountFutureSecretById(Long id) {
        List<QuanAccountFutureSecret> list = quanAccountFutureSecretMapper.selectBySourceId(id);
        return list;
    }
}
