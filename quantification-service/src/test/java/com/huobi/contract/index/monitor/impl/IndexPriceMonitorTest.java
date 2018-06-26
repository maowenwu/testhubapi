package com.huobi.contract.index.monitor.impl;

import com.google.common.collect.Lists;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.dao.IndexInfoMapper;
import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.entity.IndexInfo;
import com.huobi.contract.index.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @desc
 * @Author mingjianyong
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class IndexPriceMonitorTest {
    @Autowired
    private IndexInfoMapper indexInfoMapper;
    @Autowired
    private IndexPriceMonitor indexPriceMonitor;

    @Test
    public void handMonitor() {
        List<IndexInfo> exchangeIndexList = indexInfoMapper.listAvaidlIndexInfo();
        List<ContractPriceIndex> priceIndices = Lists.newArrayList();
        ContractPriceIndex index = null;
        for(IndexInfo indexInfo:exchangeIndexList){
            index = new ContractPriceIndex();
            index.setIndexSymbol(indexInfo.getIndexSymbol());
            index.setInputTime(DateUtil.getMinuteBefore(1));
            priceIndices.add(index);
        }
        indexPriceMonitor.handMonitor(priceIndices);
    }
}