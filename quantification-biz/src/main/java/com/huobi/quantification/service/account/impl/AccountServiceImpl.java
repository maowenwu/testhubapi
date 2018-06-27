package com.huobi.quantification.service.account.impl;

import com.huobi.quantification.common.util.MD5;
import com.huobi.quantification.service.account.AccountService;
import com.huobi.quantification.service.http.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private HttpService httpService;

    @Override
    public Object getOkUserInfo() {
        Map<String, String> params = new HashMap<>();
        String result = httpService.okSignedPost("https://www.okex.com/api/v1/future_userinfo_4fix.do", params);
        System.out.println(result);
        return null;
    }

    @Override
    public Object getOkPosition() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol","btc_usd");
        params.put("contract_type","this_week");
        String result = httpService.okSignedPost("https://www.okex.com/api/v1/future_position_4fix.do", params);
        System.out.println(result);
        return null;
    }
}
