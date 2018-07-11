package com.huobi.quantification.provider;

import com.huobi.quantification.api.spot.SpotAccountService;
import org.springframework.stereotype.Service;

@Service
public class SpotAccountServiceImpl implements SpotAccountService {


    @Override
    public String ping() {
        return "pong";
    }
}
