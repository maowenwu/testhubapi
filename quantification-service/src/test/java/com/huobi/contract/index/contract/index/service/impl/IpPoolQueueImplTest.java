package com.huobi.contract.index.contract.index.service.impl;

import com.google.common.collect.Lists;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dto.ExchangeIpPool;
import com.huobi.contract.index.entity.ExchangeInfo;
import com.huobi.contract.index.entity.IpPool;
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
public class IpPoolQueueImplTest {
    @Autowired
    private IpPoolQueueImpl ipPoolQueue;
    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;

    @Test
    public void setIpQueueToRedis() {
        List<ExchangeIpPool> list = Lists.newArrayList();
        List<IpPool> pools = Lists.newArrayList();
        IpPool pool = new IpPool();pool.setIp("1.1.1.1");
        IpPool pool2 = new IpPool();pool2.setIp("2.2.2.2");
        IpPool pool3 = new IpPool();pool3.setIp("3.3.3.3");
        pools.add(pool);pools.add(pool2);pools.add(pool3);
        long exchangeId = 1l;
        ExchangeIpPool exchangeIpPool = new ExchangeIpPool(exchangeId,3l,7200l,pools);
        list.add(exchangeIpPool);
        //ipPoolQueue.setIpQueueToRedis(list);
        ipPoolQueue.holdIpPoolQueue();
        ExchangeInfo exchangeInfo = exchangeInfoMapper.selectByPrimaryKey(exchangeId);
        ipPoolQueue.getFristIpPool(exchangeInfo.getShortName());
        System.out.println("========================");
    }

    @Test
    public void filterUnfreezeIpTest() {
        //设置有效的IP
        List<ExchangeIpPool> list = Lists.newArrayList();
        List<IpPool> pools = Lists.newArrayList();
        IpPool pool = new IpPool();pool.setIp("1.1.1.1");
        IpPool pool2 = new IpPool();pool2.setIp("2.2.2.2");
        IpPool pool3 = new IpPool();pool3.setIp("3.3.3.3");
        pools.add(pool);pools.add(pool2);pools.add(pool3);
        ExchangeIpPool exchangeIpPool = new ExchangeIpPool(1l,3l,7200l,pools);
        list.add(exchangeIpPool);
        //设置封禁的IP
        IpPoolQueueImpl unip = new IpPoolQueueImpl();
        List<ExchangeIpPool> unlist = Lists.newArrayList();
        List<IpPool> unpools = Lists.newArrayList();
        IpPool unpool = new IpPool();unpool.setIp("1.1.1.1");unpool.setInputTime(DateUtil.getMinuteBefore(20));
        IpPool unpool2 = new IpPool();unpool2.setIp("2.2.2.2");unpool2.setInputTime(DateUtil.getMinuteBefore(20));
        unpools.add(unpool);unpools.add(unpool2);
        ExchangeIpPool unexchangeIpPool = new ExchangeIpPool(1l,3l,7200l,unpools);
        unlist.add(unexchangeIpPool);
        List<ExchangeIpPool> news = ipPoolQueue.filterUnfreezeIp(list, unlist);
    }
    @Test
    public void getIpTest(){
        IpPool ip = ipPoolQueue.getFristIpPool("bitfinex");
        System.out.println(ip);
        System.out.println("=======");
        ipPoolQueue.setIpPoolToQueueLast("bitfinex", ip);
    }
}