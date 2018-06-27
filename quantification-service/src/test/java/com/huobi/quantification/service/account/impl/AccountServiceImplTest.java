package com.huobi.quantification.service.account.impl;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.service.account.AccountService;
import com.huobi.quantification.service.market.MarketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void getOkUserInfo() {
        accountService.getOkUserInfo();
    }


    @Test
    public void getOkPosition() {
        accountService.getOkPosition();
    }

}