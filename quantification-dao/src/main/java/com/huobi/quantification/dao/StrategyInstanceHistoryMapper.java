package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyInstanceHistory;

public interface StrategyInstanceHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyInstanceHistory record);

    int insertSelective(StrategyInstanceHistory record);

    StrategyInstanceHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyInstanceHistory record);

    int updateByPrimaryKey(StrategyInstanceHistory record);

    void updateByInstanceId(StrategyInstanceHistory record);
}