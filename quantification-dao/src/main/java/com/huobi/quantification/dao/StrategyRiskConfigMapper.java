package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyRiskConfig;

public interface StrategyRiskConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyRiskConfig record);

    int insertSelective(StrategyRiskConfig record);

    StrategyRiskConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyRiskConfig record);

    int updateByPrimaryKey(StrategyRiskConfig record);

    int updateBySymbolTypeSelective(StrategyRiskConfig record);
}