package com.huobi.contract.index.dao;

import com.huobi.quantification.index.entity.QuantificationTicker;

public interface QuantificationTickerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuantificationTicker record);

    int insertSelective(QuantificationTicker record);

    QuantificationTicker selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuantificationTicker record);

    int updateByPrimaryKey(QuantificationTicker record);
}