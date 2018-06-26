package com.huobi.contract.index.api;

import com.huobi.contract.index.dao.ContractPriceIndexHisMapper;
import com.huobi.contract.index.service.ServiceApplication;
import com.huobi.contract.index.taskjob.grab.http.AbstractHttpGrab;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 指数抓取 单元测试
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class ExchangeIndexSymbolGrapTest {


    @Autowired
    @Qualifier("huobiIndexGrabJob")
    private AbstractHttpGrab huobiIndexGrabJob;

    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;

    /**
     * bitfinex交易所
     */
    @Test
    public void bitfinexIndexGrabServiceTest(){
    }

    /**
     * bitstamp 交易所
     */
    @Test
    public void bitstampIndexGrabServiceTest(){
    }

    /**
     * bittrex 交易所
     */
    @Test
    public void bittrexIndexGrabServiceTest(){
    }

    /**
     * gdax 交易所
     */
    @Test
    public void gdaxIndexGrabServiceTest(){
    }
    /**
     * gemini 交易所
     */
    @Test
    public void geminiIndexGrabServiceTest(){
    }
    /**
     * huobi 交易所
     */
    @Test
    public void huobiIndexGrabServiceTest(){
        huobiIndexGrabJob.grab();
    }
    /**
     * kraken 交易所
     */
    @Test
    public void krakenIndexGrabServiceTest(){
    }
    /**
     * poloniex 交易所
     */
    @Test
    public void poloniexIndexGrabServiceTest(){
    }

    /**
     * 汇率抓取
     */
    @Test
    public void exchangeRateGrabServiceTest(){
    }


    /**
     * 批量交易所抓取  单元测试
     */
    @Test
    public void batchContractIndexPriceHisGrapTest(){

    }
}
