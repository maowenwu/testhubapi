package com.huobi.quantification.service.account.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanAccountFuturePositionMapper;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.service.account.AccountService;
import com.huobi.quantification.service.http.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private HttpService httpService;

    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    @Autowired
    private QuanAccountFuturePositionMapper quanAccountFuturePositionMapper;

    @Override
    public Object getOkUserInfo() {
        Map<String, String> params = new HashMap<>();
        String body = httpService.okSignedPost(HttpConstant.OK_USER_INFO, params);
        parseAndSaveUserInfo(body);
        return null;
    }

    private void parseAndSaveUserInfo(String body) {
        long queryId = System.currentTimeMillis();
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

        for (QuanAccountFutureAsset asset : list) {
            asset.setQueryId(queryId);
            quanAccountFutureAssetMapper.insert(asset);
        }

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

    @Override
    public Object getOkPosition() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        String body = httpService.okSignedPost(HttpConstant.OK_POSITION, params);
        parseAndSavePosition(body);
        return null;
    }


    private void parseAndSavePosition(String body) {
        long queryId = System.currentTimeMillis();
        JSONObject jsonObject = JSON.parseObject(body);
        Boolean b = jsonObject.getBoolean("result");
        List<QuanAccountFuturePosition> list = new ArrayList<>();
        if (b) {
            BigDecimal forceLiquPrice = jsonObject.getBigDecimal("force_liqu_price");
            JSONArray holding = jsonObject.getJSONArray("holding");
            for (int i = 0; i < holding.size(); i++) {
                JSONObject holdingJSONObject = holding.getJSONObject(i);
                list.add(parseFuturePosition(holdingJSONObject, forceLiquPrice));
            }
        }

        for (QuanAccountFuturePosition position : list) {
            position.setQueryId(queryId);
            quanAccountFuturePositionMapper.insert(position);
        }
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
}
