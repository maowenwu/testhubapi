package com.huobi.contract.index.contract.index.service.impl;

import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.contract.index.service.IndexPriceService;
import com.huobi.contract.index.dao.ContractPriceIndexHisMapper;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.ContractPriceIndexHis;
import com.huobi.contract.index.entity.ExchangeInfo;
import com.huobi.contract.index.entity.ValidEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class IndexPriceServiceImpl implements IndexPriceService {

    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;

    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public void batchSaveContractPriceIndexHis(List<ContractPriceIndexHis> hisList) {
        hisList.stream().parallel().forEach(his->contractPriceIndexHisMapper.insertSelective(his));
    }

    @Override
    public ContractPriceIndexHis createInvalidObjFromLatestHis(ExchangeIndex exchangeIndex, Origin origin) {
        String exchangeName = getExchangeNameById(exchangeIndex.getExchangeId());
        ContractPriceIndexHis hisIndexPrice = null;
        if (Origin.HTTP == origin) {
            hisIndexPrice = redisService.getHttpHisIndexPrice(exchangeName, exchangeIndex.getIndexSymbol());
        } else {
            hisIndexPrice = redisService.getWsHisIndexPrice(exchangeName, exchangeIndex.getIndexSymbol());
        }
        if (hisIndexPrice != null) {
            hisIndexPrice.setStatus(ValidEnum.FAIL.getStatus());
            hisIndexPrice.setInputTime(new Date());
            return hisIndexPrice;
        }

        hisIndexPrice = contractPriceIndexHisMapper.getLastContractPriceIndexHis(exchangeIndex.getExchangeId(), exchangeIndex.getIndexSymbol(), origin.value());
        if (hisIndexPrice != null) {
            hisIndexPrice.setStatus(ValidEnum.FAIL.getStatus());
            hisIndexPrice.setInputTime(new Date());
            return hisIndexPrice;
        }

        return createInvalidZeroIndexHis(exchangeIndex);
    }


    public ContractPriceIndexHis createInvalidZeroIndexHis(ExchangeIndex ei) {
        ContractPriceIndexHis contractPriceIndexHis = new ContractPriceIndexHis();
        contractPriceIndexHis.setExchangeId(ei.getExchangeId());
        contractPriceIndexHis.setSourceSymbol(ei.getSourceSymbol());
        contractPriceIndexHis.setTargetSymbol(ei.getIndexSymbol());
        contractPriceIndexHis.setInputTime(new Date());
        contractPriceIndexHis.setTradeTime(new Date());
        contractPriceIndexHis.setStatus(ValidEnum.FAIL.getStatus());
        contractPriceIndexHis.setRate(BigDecimal.ZERO);
        contractPriceIndexHis.setTargetPrice(BigDecimal.ZERO);
        contractPriceIndexHis.setSourcePrice(BigDecimal.ZERO);
        return contractPriceIndexHis;
    }

    public String getExchangeNameById(Long exchangeId) {
        ExchangeInfo exchangeInfo = exchangeInfoMapper.selectByPrimaryKey(exchangeId);
        if (exchangeId != null) {
            return exchangeInfo.getShortName();
        }
        throw new RuntimeException("ExchangeInfo 不存在,exchangeId=" + exchangeId);
    }
}
