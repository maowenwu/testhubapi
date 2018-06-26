package com.huobi.contract.index.api;

import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.contract.index.common.CurrencyEnum;
import com.huobi.contract.index.dao.ExchangeRateHisMapper;
import com.huobi.contract.index.dao.ExchangeRateMapper;
import com.huobi.contract.index.entity.ExchangeRate;
import com.huobi.contract.index.entity.ExchangeRateHis;
import com.huobi.contract.index.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class ExchangeRateCalcServiceTest {
    @Autowired
    private ExchangeRateMapper exchangeRateMapper;
    @Autowired
    private ExchangeRateHisMapper exchangeRateHisMapper;
    @Autowired
    @Qualifier("exchangeRateCalcService")
    private ExchangeRateCalcService exchangeRateCalcService;
    /**
     * 汇率计算任务 单元测试
     * 1.redis数据清空 flush all;
     * 2.汇率历史表，汇率表清空
     * 2.运行测试
     */
    @Test
    public void ExchangeRateCalcTest(){
        String cny = CurrencyEnum.CNY.getShortName();
        String symbol = "USD-"+cny;
        ExchangeRateHis exchangeRateHis = null;
        Date date = new Date();
        ExchangeRate exchangeRate = new ExchangeRate(symbol,BigDecimal.valueOf(7.52d),date,null);
        exchangeRateMapper.insertSelective(exchangeRate);
        BigDecimal rate = new BigDecimal(1);
        for(int i=1;i<=14;i++){
            exchangeRateHis = new ExchangeRateHis();
            exchangeRateHis.setInputTime(date);
            exchangeRateHis.setExchangeTime(date);
            exchangeRateHis.setExchangeRate(rate);
            exchangeRateHis.setExchangeId(1L);
            exchangeRateHis.setExchangeSymbol(symbol);
            exchangeRateHisMapper.insertSelective(exchangeRateHis);
            date = DateUtil.getSpecifiedDayBefore(date, 1);
            rate = rate.add(new BigDecimal(1));
        }
        exchangeRateCalcService.calc();
    }
}
