package com.huobi.contract.index.facade.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.facade.entity.ContractIndexListResult;
import com.huobi.contract.index.facade.entity.TargetHisPriceListResult;
import com.huobi.contract.index.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 对面接口 单元测试
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class ContractIndexMonitorServiceImplTest {
    @Reference
    private ContractIndexMonitorService contractIndexMonitorService;

    @Test
    public void listContrctIndexBySymbolTest(){
        String symbol = "BTC-USD";
        Long begin = System.currentTimeMillis();
        ContractIndexListResult result = contractIndexMonitorService.listContrctIndexBySymbol(symbol);
        Long end = System.currentTimeMillis();

        System.out.println("运行时间："+(end-begin));
    }
    @Test
    public void listContractIndexHistoryPriceTest(){
        String symbol = "BTC-USD";
        Long begin = System.currentTimeMillis();
        List<TargetHisPriceListResult> re = contractIndexMonitorService.listContractIndexHistoryPrice(symbol, 60);
        System.out.println(JSONObject.toJSONString(re));
        Long end = System.currentTimeMillis();
        System.out.println("运行时间："+(end-begin));
    }
    @Test
    public void listContractIndexChangeRecordTest(){
        Long begin = System.currentTimeMillis();
        String symbol = "BTC-USD";
        contractIndexMonitorService.listContractIndexChangeRecord(symbol, 1, 4);
        Long end = System.currentTimeMillis();
        System.out.println("运行时间："+(end-begin));
    }
    @Test
    public void getLastIndexPrice(){

    }
    @Test
    public void getExchangeHisPirce(){

    }
}
