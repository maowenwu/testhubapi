package com.huobi.contract.index.contract.index.service.impl;

import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.dao.ContractPriceIndexHisMapper;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dao.ExchangeRateHisMapper;
import com.huobi.contract.index.entity.ContractPriceIndexHis;
import com.huobi.contract.index.entity.ExchangeInfo;
import com.huobi.contract.index.entity.ExchangeRateHis;
import com.huobi.contract.index.entity.ValidEnum;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BaseIndexGrabService {
    private static final Logger LOG = LoggerFactory.getLogger(BaseIndexGrabService.class);
    @Autowired
    private RedisService redisService;
    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;
    @Autowired
    private ExchangeRateHisMapper exchangeRateHisMapper;

    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;









    /**
     * 插入成功的记录到汇率历史表
     *
     * @param list
     */
    protected void batchInsertSuccExchangeRateHis(List<ExchangeRateHis> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (ExchangeRateHis his : list) {
                try {
                    exchangeRateHisMapper.insertSelective(his);
                    redisService.updateIndexRate(his.getExchangeSymbol(), his.getExchangeRate());
                } catch (Exception e) {
                    LOG.error("插入[" + his.getExchangeSymbol() + "]到汇率表和redis出错", e);
                }
            }
        }
    }

    /**
     * 插入汇率获取失败的记录
     * 1.获取上次采集的汇率作为本次汇率
     *
     * @param rateSymbol
     * @param date
     */
    protected void batchInsertFailExchangeRateHis(String rateSymbol, Date date) {
        ExchangeRateHis exchangeRateHis = exchangeRateHisMapper.getLastRateHisBySymbol(rateSymbol);
        if (exchangeRateHis == null) {
            return;
        }
        ExchangeRateHis failedRecord = new ExchangeRateHis(Constant.DEFAULT_EXCHANGEID, rateSymbol, exchangeRateHis.getExchangeRate(), date, date,ValidEnum.FAIL.getStatus());
        exchangeRateHisMapper.insertSelective(failedRecord);
    }

    /**
     * 批量插入汇率获取失败的记录
     * 1.获取上次采集的汇率作为本次汇率
     * @param rateSymbols
     * @param date
     */
    protected void batchInsertFailExchangeRateHis(List<String> rateSymbols, Date date) {
        for (String rateSymbol : rateSymbols) {
            String exchangeSymbol = Constant.USD_SHORTNAME + "-" + rateSymbol;
            ExchangeRateHis exchangeRateHis = exchangeRateHisMapper.getLastRateHisBySymbol(exchangeSymbol);
            if (exchangeRateHis == null) {
                continue;
            }
            ExchangeRateHis failedRecord = new ExchangeRateHis(Constant.DEFAULT_EXCHANGEID, exchangeSymbol, exchangeRateHis.getExchangeRate(), date, date,ValidEnum.FAIL.getStatus());
            exchangeRateHisMapper.insertSelective(failedRecord);
        }
    }

    /**
     *
     * @param his
     * @return
     */
    protected ContractPriceIndexHis getLastGrapHisPriceByExchangeAndSymbol(ContractPriceIndexHis his) {
        String exchangeName = getExchangeNameById(his.getExchangeId());
        ContractPriceIndexHis httpHisIndexPrice = redisService.getHttpHisIndexPrice(exchangeName, his.getTargetSymbol());
        if (httpHisIndexPrice != null) {
            return httpHisIndexPrice;
        }
        return contractPriceIndexHisMapper.getLastSymbolPriceByExchangeAndSymbol(his);
    }


    public String getExchangeNameById(Long exchangeId) {
        ExchangeInfo exchangeInfo = exchangeInfoMapper.selectByPrimaryKey(exchangeId);
        if (exchangeId != null) {
            return exchangeInfo.getShortName();
        }
        throw new RuntimeException("ExchangeInfo 不存在,exchangeId=" + exchangeId);
    }
}
