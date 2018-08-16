package com.huobi.quantification.service.account.impl;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.service.http.HttpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class OkFutureFutureAccountServiceImplTest {


    @Autowired
    private HttpService httpService;


    @Test
    public void queryOkPositionByAPI(){
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "btc_usd");
        params.put("contract_type", "this_week");
        String body = httpService.doOkFuturePost(1L, HttpConstant.OK_POSITION, params);
        System.out.println(body);
    }
}