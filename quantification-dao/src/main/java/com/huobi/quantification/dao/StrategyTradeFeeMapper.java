package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyTradeFee;
import org.apache.ibatis.annotations.Param;

public interface StrategyTradeFeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyTradeFee record);

    int insertSelective(StrategyTradeFee record);

    StrategyTradeFee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyTradeFee record);

    int updateByPrimaryKey(StrategyTradeFee record);

    StrategyTradeFee selectBySymbolContractType(@Param("symbol") String symbol, @Param("contractType")String contractType);
}