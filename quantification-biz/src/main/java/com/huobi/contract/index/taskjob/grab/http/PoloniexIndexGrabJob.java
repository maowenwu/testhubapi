package com.huobi.contract.index.taskjob.grab.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.common.util.HttpClientUtil;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.contract.index.service.IpPoolQueue;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PoloniexIndexGrabJob extends AbstractHttpGrab {
    private static final Logger logger = LoggerFactory.getLogger(PoloniexIndexGrabJob.class);
    /**
     * 该交易所被封禁的HTTP响应码
     */
    private  final static int banIpStatusCode = -1;
    @Autowired
    private IpPoolQueue ipPoolQueue;
    @Override
    public ExchangeEnum getExchange() {
        return ExchangeEnum.POLONIEX;
    }

    @Override
    protected List<ContractPriceIndexHis> parseIndexPriceList(List<ExchangeIndex> indexList) {
        String newUrl = constructUrl(null);
        Date requestTime = new Date();
        logger.debug("poloniex->request_time:"+DateUtil.parseDateToStr(requestTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        String response = httpRequest(newUrl, indexList);
        logger.debug("poloniex->response:"+response);
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

    public ContractPriceIndexHis grabSingleIndexPrice(ExchangeIndex exchangeIndex,String response,Date requestTime) {
        BigDecimal sourcePrice = null;
        BigDecimal rate = null;
        try {
            JSONObject object = JSON.parseObject(response);
            sourcePrice = object.getJSONObject(exchangeIndex.getExchangeSymbol()).getBigDecimal("last");
            if (BigDecimalUtil.equals(sourcePrice, BigDecimal.ZERO)) {
                return createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP);
            }
            rate = getExchangeRate(exchangeIndex);
            if (rate == null) {
                return createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP);
            }
        } catch (Exception e) {
            return createInvalidObjFromLatestHis(exchangeIndex, Origin.HTTP);
        }
        ContractPriceIndexHis baseIndexHis = createBaseSuccIndexHis(exchangeIndex);
        baseIndexHis.setSourcePrice(sourcePrice);
        baseIndexHis.setRate(rate);
        baseIndexHis.setTradeTime(requestTime);
        baseIndexHis.setTargetPrice(sourcePrice.multiply(rate));
        return baseIndexHis;
    }

    public String httpRequest(String url, List<ExchangeIndex> indexList) {
        ExchangeIndex exchangeIndex = indexList.get(0);
        IpPool ipPool = ipPoolQueue.getFristIpPool(exchangeIndex.getShortName());
        ProxyConfig config = getProxy(exchangeIndex.getExchangeId(),ipPool);
        HttpResponseEntity responseEntity = null;
        logger.debug("poloniex proxy"+ipPool.toString());
        try {
            responseEntity = HttpClientUtil.getInstance(config).get(url);
        } catch (IOException e) {
            logger.error("Poloniex抓取失败",e);
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
