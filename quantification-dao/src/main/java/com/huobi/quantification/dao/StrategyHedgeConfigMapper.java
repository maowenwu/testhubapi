package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyHedgeConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyHedgeConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyHedgeConfig record);

    int insertSelective(StrategyHedgeConfig record);

    StrategyHedgeConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyHedgeConfig record);

    int updateByPrimaryKey(StrategyHedgeConfig record);

    StrategyHedgeConfig selectBySymbolContractType(@Param("symbol") String symbol, @Param("contractType") String contractType);

    List<StrategyHedgeConfig> selectList(StrategyHedgeConfig record);

}