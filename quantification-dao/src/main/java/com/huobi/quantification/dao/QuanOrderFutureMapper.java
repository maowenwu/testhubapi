package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanOrderFuture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanOrderFutureMapper {
    int deleteByPrimaryKey(Long innerOrderId);

    int insert(QuanOrderFuture record);

    int insertSelective(QuanOrderFuture record);

    QuanOrderFuture selectByPrimaryKey(Long innerOrderId);

    int updateByPrimaryKeySelective(QuanOrderFuture record);

    int updateByPrimaryKey(QuanOrderFuture record);

    List<QuanOrderFuture> selectBySelective(QuanOrderFuture record);

    List<Long> selectOrderIdBySourceStatus(@Param("exchangeId") int exchangeId, @Param("accountId") Long accountId, @Param("sourceStatus") List<Integer> sourceStatus);

    int updateByExIdAccountIdExOrderId(QuanOrderFuture record);
}