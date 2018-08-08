package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyOrderConfig;
import org.apache.ibatis.annotations.Param;

public interface StrategyOrderConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyOrderConfig record);

    int insertSelective(StrategyOrderConfig record);

    StrategyOrderConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyOrderConfig record);

    int updateByPrimaryKey(StrategyOrderConfig record);

    StrategyOrderConfig selectBySymbolContractType(@Param("symbol") String symbol, @Param("contractType") String contractType);
}