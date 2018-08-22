package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyRiskHistory;
import org.apache.ibatis.annotations.Param;

public interface StrategyRiskHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyRiskHistory record);

    int insertSelective(StrategyRiskHistory record);

    StrategyRiskHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyRiskHistory record);

    int updateByPrimaryKey(StrategyRiskHistory record);

    //通过 instanceId  和 baseCoin  查询最新的一条数据
    StrategyRiskHistory selectLatestByInstanceIdCoin(@Param("instanceId") Long instanceId);

}