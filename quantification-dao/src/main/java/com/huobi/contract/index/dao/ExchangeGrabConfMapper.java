package com.huobi.contract.index.dao;

import com.huobi.contract.index.entity.ExchangeGrabConf;

public interface ExchangeGrabConfMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(ExchangeGrabConf record);

    int insertSelective(ExchangeGrabConf record);

    ExchangeGrabConf selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(ExchangeGrabConf record);

    int updateByPrimaryKey(ExchangeGrabConf record);

    /**
     * 根据exchangeId获取配合记录
     * @param exchangeId
     */
    ExchangeGrabConf getExchangeGrabConfByExchangeId(Long exchangeId);

}