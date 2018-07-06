package com.huobi.quantification.provider;

import com.huobi.quantification.api.AccountService;
import org.springframework.stereotype.Service;

@Service("accountService")
public class AccountServiceImpl implements AccountService {


    @Override
    public String ping() {
        return "pong";
    }
}
