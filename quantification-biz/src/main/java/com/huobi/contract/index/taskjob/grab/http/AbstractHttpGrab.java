package com.huobi.contract.index.taskjob.grab.http;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.common.util.HttpClientUtil;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.contract.index.common.IPIsBanned;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.contract.index.service.IpPoolQueue;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dao.IpUsageRecordMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.*;
import com.huobi.contract.index.taskjob.grab.AbstractGrabJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractHttpGrab extends AbstractGrabJob {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpGrab.class);
    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;

    @Autowired
    private IpPoolQueue ipPoolQueue;

    @Autowired
    private IpUsageRecordMapper ipUsageRecordMapper;

    private final static int HTTP_SUCCESS_CODE = 200;

    @Transactional
    @Override
    public void process(JobExecutionMultipleShardingContext arg0) {
        logger.info("指数抓取任务开始");
        grab();
        logger.info("指数抓取任务结束");
    }

    public void grab() {
        ExchangeEnum exchange = getExchange();
        logger.debug(exchange.getExchangeName()+" start grab");
        List<ExchangeIndex> indexList = exchangeInfoMapper.getAllExchangeIndex(exchange.getExchangeName());
        List<ContractPriceIndexHis> hisList = parseIndexPriceList(indexList);
        if(exchange.getExchangeName().equals("poloniex")){
            logger.debug("poloniex end grab");
        }
        if(!CollectionUtils.isEmpty(hisList)){
            batchSaveContractPriceIndexHis(hisList);
            batchSaveToRedis(hisList);
        }
    }

    /**
     * 获取原始数据
     */
    protected List<ContractPriceIndexHis> parseIndexPriceList(List<ExchangeIndex> indexList) {
        List<ContractPriceIndexHis> hisList = new ArrayList<>();
        indexList.stream().parallel().forEach(ei->hisList.add(grabSingleIndexPrice(ei)));
        return hisList;
    }
    public abstract ExchangeEnum getExchange();

    protected Ticker parseIndexPrice(String response, ExchangeIndex exchangeIndex,Date requestTime) {
        throw new RuntimeException("操作不支持");
    }

    /**
     * 获取各交易所封禁IP的状态码
     * @return
     */
    protected int getBanIpSattusCode(){
        throw new RuntimeException("操作不支持");
    }

    /**
     * @param exchangeIndex
     * @return
     */
    public ContractPriceIndexHis grabSingleIndexPrice(ExchangeIndex exchangeIndex) {
        String newUrl = constructUrl(exchangeIndex.getExchangeSymbol());
        Date requestTime = new Date();
        logger.debug(exchangeIndex.getShortName()+"->request_time:"+DateUtil.parseDateToStr(requestTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        String response = httpRequest(newUrl, exchangeIndex);
        logger.debug(exchangeIndex.getShortName()+"->response:"+response);
        if (StringUtils.isEmpty(response)) {
            return createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP);
        }
        Ticker ticker = parseIndexPrice(response, exchangeIndex,requestTime);
        if (BigDecimalUtil.equals(ticker.getPrice(), BigDecimal.ZERO)) {
            return createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP);
        }
        BigDecimal rate = getExchangeRate(exchangeIndex);
        if (rate == null) {
            return createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP);
        }
        ContractPriceIndexHis baseIndexHis = createBaseSuccIndexHis(exchangeIndex);
        baseIndexHis.setSourcePrice(ticker.getPrice());
        baseIndexHis.setRate(rate);
        baseIndexHis.setTradeTime(ticker.getTradeTime());
        baseIndexHis.setTargetPrice(ticker.getPrice().multiply(rate));
        return baseIndexHis;
    }

    /**
     * 如果响应码不为200或超时报错都返回空字符串
     *
     * @param url
     * @param exchangeIndex
     * @return
     */
    public String httpRequest(String url, ExchangeIndex exchangeIndex) {
        IpPool ipPool = ipPoolQueue.getFristIpPool(exchangeIndex.getShortName());
        ProxyConfig config = getProxy(exchangeIndex.getExchangeId(),ipPool);
        HttpResponseEntity  responseEntity = null;
        try {
            responseEntity = HttpClientUtil.getInstance(config).get(url);
        } catch (Exception e) {
            logger.error(exchangeIndex.getShortName()+"抓取失败",e);
        }
        if(ipPool==null && responseEntity!=null){
            return responseEntity.getResponse();
        }
        if(responseEntity==null){
            createIpUsageRecord(url,ipPool,responseEntity,exchangeIndex);
            return null;
        }
        createIpUsageRecord(url,ipPool,responseEntity,exchangeIndex);
        return responseEntity.getResponse();
    }

    public void createIpUsageRecord(String url,IpPool ipPool,HttpResponseEntity responseEntity,ExchangeIndex exchangeIndex){
        String usedIp = ipPool.getIp();
        IpUsageRecord record = null;
        int banIpStatus = getBanIpSattusCode();
        if(responseEntity==null){
            record = new IpUsageRecord(exchangeIndex.getExchangeId() ,usedIp,url, ValidEnum.FAIL.getStatus().byteValue(), IPIsBanned.NOTBANNED.getBanStatus(),
                   null ,null, new Date());
            ipUsageRecordMapper.insertSelective(record);
            ipPoolQueue.setIpPoolToQueueLast(exchangeIndex.getShortName(), ipPool);
            return;
        }
        int responseCode = responseEntity.getStatusCode();
        if(responseCode == banIpStatus){
            record = new IpUsageRecord(exchangeIndex.getExchangeId() ,usedIp,url, ValidEnum.FAIL.getStatus(), IPIsBanned.BANNED.getBanStatus(),
                    responseCode,null , new Date());
        }else if(responseCode == HTTP_SUCCESS_CODE) {
            record = new IpUsageRecord(exchangeIndex.getExchangeId(),usedIp,url, ValidEnum.SUCC.getStatus(), IPIsBanned.NOTBANNED.getBanStatus(),
                    responseCode,null, new Date());
            ipPoolQueue.setIpPoolToQueueLast(exchangeIndex.getShortName(), ipPool);
        }else{
            record = new IpUsageRecord(exchangeIndex.getExchangeId() ,usedIp,url,ValidEnum.FAIL.getStatus() , IPIsBanned.NOTBANNED.getBanStatus(),
                    responseCode,null , new Date());
            ipPoolQueue.setIpPoolToQueueLast(exchangeIndex.getShortName(), ipPool);
        }
        ipUsageRecordMapper.insertSelective(record);
    }
    public ContractPriceIndexHis createBaseSuccIndexHis(ExchangeIndex exchangeIndex) {
        ContractPriceIndexHis contractPriceIndexHis = new ContractPriceIndexHis();
        contractPriceIndexHis.setExchangeId(exchangeIndex.getExchangeId());
        contractPriceIndexHis.setSourceSymbol(exchangeIndex.getSourceSymbol());
        contractPriceIndexHis.setTargetSymbol(exchangeIndex.getIndexSymbol());
        contractPriceIndexHis.setTradeTime(new Date());
        contractPriceIndexHis.setInputTime(new Date());
        contractPriceIndexHis.setStatus(ValidEnum.SUCC.getStatus());
        contractPriceIndexHis.setOrigin(Origin.HTTP.value());
        return contractPriceIndexHis;
    }


    public String constructUrl(String symbol) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(symbol)) {
            return getExchange().getUrl();
        }
        return getExchange().getUrl().replace("<symbol>", symbol);
    }
}
