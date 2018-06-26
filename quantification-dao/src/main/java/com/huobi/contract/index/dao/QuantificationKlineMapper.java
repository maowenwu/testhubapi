package com.huobi.contract.index.dao;

import com.huobi.quantification.index.entity.QuantificationKline;

public interface QuantificationKlineMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuantificationKline record);

    int insertSelective(QuantificationKline record);

    QuantificationKline selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuantificationKline record);

    int updateByPrimaryKey(QuantificationKline record);
}