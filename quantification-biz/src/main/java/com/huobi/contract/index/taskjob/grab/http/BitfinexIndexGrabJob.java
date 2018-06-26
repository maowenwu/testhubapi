package com.huobi.contract.index.taskjob.grab.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.common.util.HttpClientUtil;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.contract.index.service.IpPoolQueue;
import com.huobi.contract.index.dao.IpUsageRecordMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("bitfinexIndexGrabJob")
public class BitfinexIndexGrabJob extends AbstractHttpGrab {
    private static final Logger logger = LoggerFactory.getLogger(BitfinexIndexGrabJob.class);
    @Override
    public ExchangeEnum getExchange() {
        return ExchangeEnum.BITFINEX;
    }

    /**
     * 该交易所被封禁的HTTP响应码
     */
    private  final static int banIpStatusCode = -1;
    @Autowired
    private IpPoolQueue ipPoolQueue;

    @Autowired
    private IpUsageRecordMapper ipUsageRecordMapper;

    @Override
    protected List<ContractPriceIndexHis> parseIndexPriceList(List<ExchangeIndex> indexList){
        List<String> exchangeSymbolList = indexList.stream().map(index->index.getExchangeSymbol()).collect(Collectors.toList());
        //批量获取的参数
        String queryParmas = StringUtils.collectionToDelimitedString(exchangeSymbolList,",");
        String newUrl = constructUrl(queryParmas);
        Date requestTime = new Date();
        logger.debug("bitfinex->request_time:"+DateUtil.parseDateToStr(requestTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        String response = httpRequest(newUrl, indexList);
        logger.debug("bitfinex->response:"+response);
        List<ContractPriceIndexHis> hisList = new ArrayList<>();
        if (StringUtils.isEmpty(response)) {
            for(ExchangeIndex exchangeIndex:indexList){
                hisList.add(createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP));
            }
            return hisList;
        }
        for (ExchangeIndex ei : indexList) {
            hisList.add(grabSingleIndexPrice(ei,response,requestTime));
        }
        return hisList;
    }

    public ContractPriceIndexHis grabSingleIndexPrice(ExchangeIndex exchangeIndex,String response,Date requestTime){
        JSONArray arraySymbol = JSON.parseArray(response);
        for(int i=0;i<arraySymbol.size();i++){
            String symbolInfo  = arraySymbol.get(i).toString();
            JSONArray array = JSON.parseArray(symbolInfo);
            if(exchangeIndex.getExchangeSymbol().equals(array.get(0))){
                BigDecimal sourcePrice = array.getBigDecimal(7);
                if (BigDecimalUtil.equals(sourcePrice, BigDecimal.ZERO)) {
                    return createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP);
                }
                BigDecimal rate = getExchangeRate(exchangeIndex);
                if (rate == null) {
                    return createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP);
                }
                ContractPriceIndexHis baseIndexHis = createBaseSuccIndexHis(exchangeIndex);
                baseIndexHis.setSourcePrice(sourcePrice);
                baseIndexHis.setRate(rate);
                baseIndexHis.setTradeTime(requestTime);
                baseIndexHis.setTargetPrice(sourcePrice.multiply(rate));
                return baseIndexHis;
            }
        }
        return createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP);
    }

    public String httpRequest(String url, List<ExchangeIndex> indexList) {
        ExchangeIndex exchangeIndex = indexList.get(0);
        String exchangeShortName = indexList.get(0).getShortName();
        IpPool ipPool = ipPoolQueue.getFristIpPool(exchangeShortName);
        ProxyConfig config = getProxy(exchangeIndex.getExchangeId(),ipPool);
        HttpResponseEntity responseEntity = null;
        try {
            responseEntity = HttpClientUtil.getInstance(config).get(url);
        } catch (IOException e) {
            logger.error("Bitfinex抓取失败",e.getMessage());
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

    @Override
    protected int getBanIpSattusCode() {
        return banIpStatusCode;
    }
}
