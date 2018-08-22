package com.huobi.quantification.dao;

import com.huobi.quantification.entity.StrategyFinanceHistory;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StrategyFinanceHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StrategyFinanceHistory record);

    int insertSelective(StrategyFinanceHistory record);

    StrategyFinanceHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StrategyFinanceHistory record);

    int updateByPrimaryKey(StrategyFinanceHistory record);

    BigDecimal getNetBorrow(@Param("exchangeId") int exchangeId, @Param("accountSourceId") Long accountSourceId, @Param("coinType") String coinType, @Param("initialOnly") boolean initialOnly);

    List<StrategyFinanceHistory> selectList(StrategyFinanceHistory strategyFinanceHistory);
}