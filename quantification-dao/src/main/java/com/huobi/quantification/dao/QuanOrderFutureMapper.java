package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanOrderFuture;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    List<QuanOrderFuture> selectByInnerOrderIds(@Param("innerOrderId") List<Long> innerOrderId);

    List<QuanOrderFuture> selectByExOrderIds(Map params);

    List<QuanOrderFuture> selectByLinkOrderIds(Map params);

    List<Long> selectExOrderIdByStatus(@Param("exchangeId") int exchangeId, @Param("accountId") Long accountId, @Param("baseCoin") String baseCoin, @Param("statusList") List<Integer> statusList);

    List<QuanOrderFuture> selectOrderByStatus(@Param("exchangeId") int exchangeId, @Param("accountId") Long accountId, @Param("statusList") List<Integer> statusList);
}