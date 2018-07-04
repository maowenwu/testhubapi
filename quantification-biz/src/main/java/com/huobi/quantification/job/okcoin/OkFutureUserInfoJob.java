package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OkFutureUserInfoJob {

    @Autowired
    private AccountService accountService;


    public void execute() {
        accountService.storeAllOkUserInfo();
    }
}
