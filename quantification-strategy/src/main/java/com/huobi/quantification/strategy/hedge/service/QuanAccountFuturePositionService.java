package com.huobi.quantification.strategy.hedge.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class QuanAccountFuturePositionService {


    /**
     * 获取指定账户指定币种的净充值数
     *
     * @param accountId
     * @param exchangeId
     * @param coin
     * @return
     */
    public BigDecimal getInitAmount(Long accountId, int exchangeId, String coin) {
        //BigDecimal result = quanAccountHistoryMapper.getInitAmount(accountId, exchangeId, coin);
        return null;
    }

}
