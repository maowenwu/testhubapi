package com.huobi.contract.index.api;

import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.dao.ContractPriceIndexHisMapper;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.ContractPriceIndexHis;
import com.huobi.contract.index.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class ExchangeIsGrabCalcServiceTest {
    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;
    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;
    @Autowired
    @Qualifier("exchangeIsGrapCalcService")
    private ExchangeIsQualifiedGrabCalcService exchangeIsGrapCalcService;
    /**
     * 可抓取状态定时任务单元测试（清空历史价格表）
     * 1.生成12批BTC-USD测试数据，每批数据时间间隔50s,共12条
     * 2.修改effectives集合的值，设置生成有效点的数量
     * 3.运行可抓取状态计算任务
     * 5.查看权重配置表，可抓取状态变更
     */
    @Test
    public void ExchangeIsGrapTest(){
        List<ExchangeIndex> list = exchangeInfoMapper.listValidExchangeIndex();
        Date date = new Date();
        int count = 12;
        //配置第几批的数据生成无效的值
        List<Integer> effectives = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11);
        //配置设置BTC-USD价格记录状态为无效
        String indexSymbol = "BTC-USD";Long exchangeId = 1L;
        BigDecimal price = new BigDecimal(1000);
        ContractPriceIndexHis indexhis = null;
        for(int i=1;i<=count;i++){
            indexhis = new ContractPriceIndexHis();
            indexhis.setExchangeId(exchangeId);
            indexhis.setTargetSymbol(indexSymbol);
            indexhis.setRate(BigDecimal.ONE);
            price = price.add(new BigDecimal(10).add(new BigDecimal(i)));
            indexhis.setSourcePrice(price);
            indexhis.setTargetPrice(price);
            indexhis.setSourceSymbol(indexSymbol);
            if(!effectives.contains(i)){
                indexhis.setStatus(0);//无效
            }else {
                indexhis.setStatus(1);
            }
            indexhis.setInputTime(date);
            contractPriceIndexHisMapper.insertSelective(indexhis);
            date = DateUtil.getSecondBefore(date,50);
        }
        exchangeIsGrapCalcService.httpQualifiedCalc();
    }
}
