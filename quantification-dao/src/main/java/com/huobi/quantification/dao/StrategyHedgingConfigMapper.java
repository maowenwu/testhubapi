package com.huobi.quantification.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.huobi.quantification.entity.StrategyHedgingConfig;

public interface StrategyHedgingConfigMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(StrategyHedgingConfig record);

	StrategyHedgingConfig selectByPrimaryKey(Integer id);

	List<StrategyHedgingConfig> selectAll();

	int updateByPrimaryKey(StrategyHedgingConfig record);

	StrategyHedgingConfig selectStrategyHedging(@Param("coin") String coin,
			@Param("contractType") String contractType);

}