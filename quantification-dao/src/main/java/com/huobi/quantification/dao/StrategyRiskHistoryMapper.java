package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyRiskHistory;

public interface StrategyRiskHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyRiskHistory record);

    int insertSelective(StrategyRiskHistory record);

    StrategyRiskHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyRiskHistory record);

    int updateByPrimaryKey(StrategyRiskHistory record);
}