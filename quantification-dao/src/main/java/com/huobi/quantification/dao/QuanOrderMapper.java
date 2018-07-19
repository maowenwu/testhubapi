package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanOrder;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface QuanOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanOrder record);

    QuanOrder selectByPrimaryKey(Long id);

    List<QuanOrder> selectAll();

    int updateByPrimaryKey(QuanOrder record);
    
    List<QuanOrder> selectList(QuanOrder record);

	List<Long> selectByOrderInfo(@Param("accountId")Long accountId, @Param("orderState")String orderState,@Param("symbol")String symbol);

	int updateOrderByOrderId(QuanOrder quanOrder);
}