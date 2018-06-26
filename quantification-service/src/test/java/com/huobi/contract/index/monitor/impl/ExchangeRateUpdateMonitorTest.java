package com.huobi.contract.index.monitor.impl;

import com.huobi.contract.index.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @desc
 * @Author mingjianyong
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class ExchangeRateUpdateMonitorTest {
    @Autowired
    private  ExchangeRateUpdateMonitor exchangeRateUpdateMonitor;

    @Test
    public void monitor() {
        exchangeRateUpdateMonitor.monitor();
    }
}