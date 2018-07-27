package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyRiskManagementConfig;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface StrategyRiskManagementConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyRiskManagementConfig record);

    StrategyRiskManagementConfig selectByPrimaryKey(Integer id);

    List<StrategyRiskManagementConfig> selectAll();

    int updateByPrimaryKey(StrategyRiskManagementConfig record);

	StrategyRiskManagementConfig selectByContractCode(@Param("contractCode")String contractCode);
	
	int updateOrderRiskManagement(@Param("contractCode")String contractCode, @Param("orderType")int orderType);
	
	int updateHedgingRiskManagement(@Param("contractCode")String contractCode, @Param("hedgingType")int hedgingType);
}