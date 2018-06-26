package com.huobi.contract.index.taskjob.grab;

import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.contract.index.common.HttpType;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.contract.index.service.ExchangeRateService;
import com.huobi.contract.index.contract.index.service.IndexPriceService;
import com.huobi.contract.index.dao.ExchangeGrabConfMapper;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dao.IpUsageRecordMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public abstract class AbstractGrabJob extends AbstractSimpleElasticJob {

    public static final Logger logger = LoggerFactory.getLogger(AbstractGrabJob.class);

    @Autowired
    protected IndexPriceService indexPriceService;

    @Autowired
    protected ExchangeRateService exchangeRateService;

    @Autowired
    protected RedisService redisService;

    @Autowired
    protected ExchangeInfoMapper exchangeInfoMapper;

    @Autowired
    protected ExchangeGrabConfMapper exchangeGrabConfMapper;
    @Autowired
    protected IpUsageRecordMapper ipUsageRecordMapper;

    public void batchSaveContractPriceIndexHis(List<ContractPriceIndexHis> hisList) {

        indexPriceService.batchSaveContractPriceIndexHis(hisList);
    }


    public ContractPriceIndexHis createInvalidObjFromLatestHis(ExchangeIndex exchangeIndex, Origin origin) {
        return indexPriceService.createInvalidObjFromLatestHis(exchangeIndex, origin);
    }


    public void batchSaveToRedis(List<ContractPriceIndexHis> hisList) {
        String exchangeName = getExchangeNameById(hisList.get(0).getExchangeId());
        for (ContractPriceIndexHis indexHis : hisList) {
            ContractPriceIndexHis oldObj = redisService.getHttpHisIndexPrice(exchangeName, indexHis.getTargetSymbol());
            if (oldObj == null) {
                redisService.updateHttpHisIndexPrice(exchangeName, indexHis.getTargetSymbol(), indexHis);
            } else if(indexHis.getStatus().equals(1) && indexHis.getTradeTime().after(oldObj.getTradeTime())) {
                redisService.updateHttpHisIndexPrice(exchangeName, indexHis.getTargetSymbol(), indexHis);
            }else{
                continue;
            }
        }
    }


    public String getExchangeNameById(Long exchangeId) {
        ExchangeInfo exchangeInfo = exchangeInfoMapper.selectByPrimaryKey(exchangeId);
        if (exchangeId != null) {
            return exchangeInfo.getShortName();
        }
        throw new RuntimeException("ExchangeInfo 不存在,exchangeId=" + exchangeId);
    }

    /**
     * 如果获取汇率失败则抛出异常
     *
     * @param exchangeIndex
     * @return
     */
    public BigDecimal getExchangeRate(ExchangeIndex exchangeIndex) {
        if (exchangeIndex.getSourceSymbol().equals(exchangeIndex.getIndexSymbol())) {
            return BigDecimal.ONE;
        } else {
            String[] indexSymbolArr = exchangeIndex.getIndexSymbol().split("-");
            String[] sourceSymbolArr = exchangeIndex.getSourceSymbol().split("-");
            StringBuffer sb = new StringBuffer(sourceSymbolArr[1]);
            sb.append("-");
            sb.append(indexSymbolArr[1]);
            BigDecimal rate = exchangeRateService.getLastExchangeRatePrice(sb.toString());
            if (rate != null) {
                return rate;
            }
            return null;
        }
    }
    /**
     * 获取代理对象
     * @param ExchangeId
     * @return
     */
    public ProxyConfig getProxy(Long ExchangeId, IpPool ipPool){
        ipPool = handleSleepCondition(ExchangeId,ipPool);
        ProxyConfig proxyConfig = new ProxyConfig();
        if(ipPool != null){
            proxyConfig.setEnabled(true);
            proxyConfig.setHost(ipPool.getIp());
            proxyConfig.setPassword(ipPool.getPasswd());
            proxyConfig.setPort(ipPool.getPort());
            if(ipPool.getProxyType() == HttpType.HTTP.getType()){
                proxyConfig.setProxyType(HttpType.HTTP.getName());
            }else if(ipPool.getProxyType() == HttpType.HTTPS.getType()){
                proxyConfig.setProxyType(HttpType.HTTPS.getName());
            }else{
                proxyConfig.setProxyType(HttpType.WEBSOCKET.getName());
            }
            proxyConfig.setUsername(ipPool.getUserName());
            return proxyConfig;
        }
        return null;
    }
    /**
     * 根据IP的使用记录来设置睡眠
     * @param exchangeId
     * @param ipPool
     * @return
     */
    public IpPool handleSleepCondition(long exchangeId,IpPool ipPool){
        if(ipPool == null){
            return ipPool;
        }
        ExchangeGrabConf conf = exchangeGrabConfMapper.getExchangeGrabConfByExchangeId(exchangeId);
        if(conf == null || conf.getGrabIntervalTime().equals(0l) || conf.getIpUnfreezeTime().equals(0l)){
            return ipPool;
        }
        IpUsageRecord record =ipUsageRecordMapper.getLatestUseRecordByExchangeIdIp(exchangeId,ipPool.getIp());
        if(record==null){
            return ipPool;
        }
        long difference = new Date().getTime()-record.getInputTime().getTime();
        if(conf.getGrabIntervalTime()>difference){
            try {
                Thread.sleep(conf.getGrabIntervalTime()-difference);
            } catch (InterruptedException e) {
                logger.error("抓取休眠异常", e);
                Thread.currentThread().interrupt();
            }
        }
        return ipPool;
    }
}
