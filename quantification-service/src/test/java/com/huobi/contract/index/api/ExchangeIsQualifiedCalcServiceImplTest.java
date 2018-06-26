package com.huobi.contract.index.api;

import com.huobi.contract.index.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @desc
 * @Author mingjianyong
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class ExchangeIsQualifiedCalcServiceImplTest {

    @Autowired
    private ExchangeIsQualifiedCalcServiceImpl exchangeIsQualifiedCalcService;

    @Test
    public void httpQualifiedCalc() {
        exchangeIsQualifiedCalcService.wsQualifiedCalc();
    }

    @Test
    public void wsQualifiedCalc() {
        exchangeIsQualifiedCalcService.wsQualifiedCalc();
    }
}