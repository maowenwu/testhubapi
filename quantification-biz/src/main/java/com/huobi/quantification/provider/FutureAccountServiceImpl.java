package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureAccountService;
import org.springframework.stereotype.Service;

@Service
public class FutureAccountServiceImpl implements FutureAccountService {


    @Override
    public String ping() {
        return "pong";
    }
}