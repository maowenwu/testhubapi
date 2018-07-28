package com.huobi.quantification.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.huobi.quantification.entity.StrategyRiskConfig;

public interface StrategyRiskConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyRiskConfig record);

    StrategyRiskConfig selectByPrimaryKey(Integer id);

    List<StrategyRiskConfig> selectAll();

    int updateByPrimaryKey(StrategyRiskConfig record);

	StrategyRiskConfig selectByContractCode(@Param("contractCode")String contractCode);
	
	int updateOrderRiskManagement(@Param("contractCode")String contractCode, @Param("orderType")int orderType);
	
	int updateHedgingRiskManagement(@Param("contractCode")String contractCode, @Param("hedgingType")int hedgingType);
}