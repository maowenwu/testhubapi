package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyHedgeConfig;

public interface StrategyHedgeConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyHedgeConfig record);

    int insertSelective(StrategyHedgeConfig record);

    StrategyHedgeConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyHedgeConfig record);

    int updateByPrimaryKey(StrategyHedgeConfig record);
}