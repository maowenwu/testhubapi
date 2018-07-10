package com.huobi.quantification.service.account.impl;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.response.future.HuobiFutureAccountInfoResponse;
import com.huobi.quantification.response.future.HuobiFuturePositionResponse;
import com.huobi.quantification.service.account.HuobiFutureAccountService;
import com.huobi.quantification.service.http.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@DependsOn("httpServiceImpl")
@Service
@Transactional
public class HuobiFutureAccountServiceImpl implements HuobiFutureAccountService {

    @Autowired
    private HttpService httpService;

    @Override
    public HuobiFutureAccountInfoResponse queryAccountInfoByAPI(String symbol) {
        HashMap<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        String body = httpService.doPost(HttpConstant.HUOBI_FUTURE_ACCOUNTINFO, params);
        return JSON.parseObject(body, HuobiFutureAccountInfoResponse.class);
    }

    @Override
    public HuobiFuturePositionResponse queryPositionByAPI(String symbol) {
        HashMap<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        String body = httpService.doPost(HttpConstant.HUOBI_FUTURE_POSITION, params);
        return JSON.parseObject(body, HuobiFuturePositionResponse.class);
    }
}
