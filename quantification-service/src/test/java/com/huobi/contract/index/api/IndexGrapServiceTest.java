package com.huobi.contract.index.api;

import com.huobi.contract.index.contract.index.service.IndexGrabService;
import com.huobi.contract.index.service.ServiceApplication;
import com.huobi.contract.index.taskjob.grab.http.AbstractHttpGrab;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 1.汇率抓取 单元测试
 * 2.okex指数抓取单元测试
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class IndexGrapServiceTest {
    @Autowired
    @Qualifier("exchangeRateGrabService")
    private IndexGrabService exchangeRateGrabService;
    @Autowired
    @Qualifier("exchangeRateCalcService")
    private ExchangeRateCalcServiceImpl exchangeRateCalcService;
    @Autowired
    @Qualifier("lastestTickerNotifyService")
    private LastestTickerNotifyService lastestTickerNotifyService;
    @Autowired
    @Qualifier("bitfinexIndexGrabJob")
    private AbstractHttpGrab bitfinexIndexGrabJob;

    /**
     * 汇率抓取
     */
    @Test
    public void exchangeRateGrapTest(){
        exchangeRateGrabService.grab();
    }

    /**
     * okex指数价格抓取
     */
    @Test
    public void lastestTickerNotifyJobTest(){
        bitfinexIndexGrabJob.grab();
    }

}
