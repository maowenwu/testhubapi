package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.entity.StrategyRiskConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyRiskConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyRiskConfig record);

    int insertSelective(StrategyRiskConfig record);

    StrategyRiskConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyRiskConfig record);

    int updateByPrimaryKey(StrategyRiskConfig record);

    StrategyRiskConfig selectBySymbolContractType(@Param("symbol") String symbol,@Param("contractType") String contractType);

    void updateBySymbolTypeSelective(StrategyRiskConfig riskConfig);

    int selectOrderAction(@Param("symbol") String symbol,@Param("contractType") String contractType);

    int selectHedgeAction(@Param("symbol") String symbol,@Param("contractType") String contractType);

    List<StrategyRiskConfig> selectList(StrategyRiskConfig quanJob);
}