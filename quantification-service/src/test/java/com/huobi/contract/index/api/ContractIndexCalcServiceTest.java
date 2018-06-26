package com.huobi.contract.index.api;


import com.huobi.contract.index.dao.ContractPriceIndexHisMapper;
import com.huobi.contract.index.dao.ContractPriceIndexMapper;
import com.huobi.contract.index.dao.ExchangeIndexWeightConfMapper;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.entity.ContractPriceIndexHis;
import com.huobi.contract.index.entity.ExchangeIndexWeightConf;
import com.huobi.contract.index.entity.ValidEnum;
import com.huobi.contract.index.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 指数计算单元测试
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class ContractIndexCalcServiceTest {
    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;
    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;
    @Autowired
    private ExchangeIndexWeightConfMapper exchangeIndexWeightConfMapper;
    @Autowired
    private ContractPriceIndexMapper contractPriceIndexMapper;
    @Autowired
    @Qualifier("contractIndexCalcService")
    private ContractIndexCalcService contractIndexCalcService;

    /**
     * 指数计算单元测试
     * 1.生成测试数据
     * 2.修改某一个交易所-币对 可抓取状态为无效
     * 3.验证数据库结果
     */
    @Test
    public void ContractIndxCalcTest() {
        //配置BTC可抓取的交易所数量
        Integer effectiveExchangeCount = 3;
        //配置默认的交易币对
        String indexSymbol = "BTC-USD";
        //800
        BigDecimal indexPrice = new BigDecimal(800);
        ContractPriceIndexHis indexhis = null;
        BigDecimal price = new BigDecimal(1000);
        List<ExchangeIndex> list = exchangeInfoMapper.getValidExchangeIndexBySymbol(indexSymbol);
        List<Long> notEffectives = null;
        List<Long> exchangeList = list.stream().map(ei -> ei.getExchangeId()).collect(Collectors.toList());
        //生成有效的交易所ID
        if (effectiveExchangeCount == 1) {
            notEffectives = new ArrayList<Long>();
            notEffectives.addAll(exchangeList);
            notEffectives.remove(exchangeList.get(0));
        } else if (effectiveExchangeCount == 2) {
            notEffectives = new ArrayList<Long>();
            notEffectives.addAll(exchangeList);
            notEffectives.remove(exchangeList.get(0));
            notEffectives.remove(exchangeList.get(1));
        } else {
            notEffectives = new ArrayList<Long>();
        }
        //生成历史价格数据
        for (ExchangeIndex index : list) {
            indexhis = new ContractPriceIndexHis();
            indexhis.setExchangeId(index.getExchangeId());
            indexhis.setTargetSymbol(index.getIndexSymbol());
            indexhis.setRate(BigDecimal.ONE);
            indexhis.setSourcePrice(price);
            indexhis.setTargetPrice(price);
            //260,10
            price = price.add(new BigDecimal(260));
            indexhis.setSourceSymbol(index.getIndexSymbol());
            indexhis.setInputTime(new Date());
            indexhis.setStatus(ValidEnum.SUCC.getStatus());
            contractPriceIndexHisMapper.insertSelective(indexhis);
        }
        //修改可扎取状态
        for (Long exId : notEffectives) {
            ExchangeIndexWeightConf conf = new ExchangeIndexWeightConf();
            conf.setIndexSymbol(indexSymbol);
            conf.setExchangeId(exId);
            //conf.setIsGrabValid(ValidEnum.FAIL.getStatus().shortValue());
            conf.setUpdateTime(new Date());
            exchangeIndexWeightConfMapper.updateIsQualifiedByExchangeIDAndIndexSymbol(conf);
        }
        //生成BTC-USD最新价格指数
        ContractPriceIndex index = new ContractPriceIndex(indexSymbol, indexPrice, new Date(), null, Byte.valueOf("1"), new Date());
        contractPriceIndexMapper.insertSelective(index);

        contractIndexCalcService.calc();
    }
}
