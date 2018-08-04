package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFuture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanAccountFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFuture record);

    int insertSelective(QuanAccountFuture record);

    QuanAccountFuture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountFuture record);

    int updateByPrimaryKey(QuanAccountFuture record);

    Long selectAccountFutureId(@Param("exchangeId") int exchangeId, @Param("accountSourceId") long accountSourceId);

    List<Long> selectByExchangeId(@Param("exchangeId")int exchangeId);
}