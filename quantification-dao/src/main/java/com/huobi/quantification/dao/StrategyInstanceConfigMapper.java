package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyInstanceConfig;

import java.util.List;

public interface StrategyInstanceConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyInstanceConfig record);

    int insertSelective(StrategyInstanceConfig record);

    StrategyInstanceConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyInstanceConfig record);

    int updateByPrimaryKey(StrategyInstanceConfig record);

    List<StrategyInstanceConfig> selectList(StrategyInstanceConfig record);
}