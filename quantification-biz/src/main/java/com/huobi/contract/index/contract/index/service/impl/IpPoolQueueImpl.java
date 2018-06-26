package com.huobi.contract.index.contract.index.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.contract.index.service.IpPoolQueue;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dao.IpPoolMapper;
import com.huobi.contract.index.dao.IpUsageRecordMapper;
import com.huobi.contract.index.dto.ExchangeIpPool;
import com.huobi.contract.index.entity.ExchangeInfo;
import com.huobi.contract.index.entity.IpPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("ipPoolQueue")
public class IpPoolQueueImpl implements IpPoolQueue {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateGrabServiceImpl.class);

    @Autowired
    private IpPoolMapper ipPoolMapper;
    @Autowired
    private IpUsageRecordMapper ipUsageRecordMapper;
    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;

    @Autowired
    private RedisService redisService;
    @Override
    public void holdIpPoolQueue() {
        //各交易所可用的所有IP
        List<ExchangeIpPool> exchangeIpPoolList =  ipPoolMapper.listExchangeIp();
        //各交易所最新被封禁ip
        List<ExchangeIpPool> unfreezeIpList = ipUsageRecordMapper.listLatestUnfreezeIp();
        //获取各个交易所可用的IP
        List<ExchangeIpPool> availExchangeIpPool =filterUnfreezeIp(exchangeIpPoolList,unfreezeIpList);

        setIpQueueToRedis(availExchangeIpPool);
    }

    /**
     * 过滤封禁的IP
     * 1.
     * @param exchangeIpPoolList
     * @param unfreezeIpList
     * @return
     */
    public List<ExchangeIpPool> filterUnfreezeIp(List<ExchangeIpPool> exchangeIpPoolList, List<ExchangeIpPool> unfreezeIpList){
        if(CollectionUtils.isEmpty(unfreezeIpList)){
            return exchangeIpPoolList;
        }
        List<ExchangeIpPool> newExchangeIpPoolList = Lists.newArrayList();
        ExchangeIpPool newExchangeIpPool = null;
        for(ExchangeIpPool exchangeIpPool : exchangeIpPoolList){
            long exchangeid = exchangeIpPool.getExchangeId();
            //封禁时长
            long unfreezeTime = exchangeIpPool.getIpUnfreezeTime();
            //取出 该exchangeid 交易所所有可用的ip
            List<IpPool> ipPools  = exchangeIpPool.getIpPoolList();
            //获取该交易所最新被封禁的IP
            List<IpPool> unfreezeIpPools = null;
            for(ExchangeIpPool pool:unfreezeIpList){
                if(pool.getExchangeId().equals(exchangeid)){
                    unfreezeIpPools = pool.getIpPoolList();
                    break;
                }
            }
            /**
             * 根据封禁时间与封禁时长计算是否被封禁
             * 1.封禁就移除
             */
            if(!CollectionUtils.isEmpty(unfreezeIpPools)){
                for(IpPool unfreeIp:unfreezeIpPools){
                    //移除所有iP中被封禁的IP
                    ipPools.removeIf(ip->mayRemove(unfreezeTime,ip,unfreeIp));
                }
            }
            newExchangeIpPool = new ExchangeIpPool(exchangeIpPool.getExchangeId(),exchangeIpPool.getGrabIntervalTime(),
                    exchangeIpPool.getIpUnfreezeTime(),ipPools);
            newExchangeIpPoolList.add(newExchangeIpPool);
        }
        return newExchangeIpPoolList;
    }

    /**
     * 存入IP队列
     */
    public void setIpQueueToRedis(List<ExchangeIpPool> exchangeIpPools){
        for(ExchangeIpPool exchangeIpPool:exchangeIpPools){
            if(exchangeIpPool==null){
                continue;
            }
            ExchangeInfo exinfo = exchangeInfoMapper.selectByPrimaryKey(exchangeIpPool.getExchangeId());
            List<IpPool> ipPools = exchangeIpPool.getIpPoolList();
            if(CollectionUtils.isEmpty(ipPools)){
                continue;
            }
            for(IpPool ipPool:ipPools){
                redisService.setLastIpPool(exinfo.getShortName(), ipPool);
            }
        }
    }

    /**
     * 判断IP是否在封禁期间
     * @param unfreezeTime
     * @param Ip
     * @param unfreeIp
     * @return
     */
    private boolean mayRemove(long unfreezeTime, IpPool Ip, IpPool unfreeIp){
        if(Ip.getIp().equals(unfreeIp.getIp())){
            Date unfreeDate = unfreeIp.getInputTime();
            Date currDate = new Date();
            long secondOffset = Math.abs(currDate.getTime() - unfreeDate.getTime())/1000;
            if(secondOffset<unfreezeTime){
                //处于封禁时间内,需要移除
                return true;
            }
        }
        return false;
    }

    @Override
    public IpPool getFristIpPool(String exchangeName) {
        return redisService.getFristIpPoolAndRemove(exchangeName);
    }

    @Override
    public void setIpPoolToQueueLast(String exchangeName, IpPool ipPool) {
        redisService.setLastIpPool(exchangeName,ipPool);
    }

    @Override
    public long getIpQueueLength(String exchangeName) {
        return redisService.getDQueueLength(exchangeName);
    }
}
