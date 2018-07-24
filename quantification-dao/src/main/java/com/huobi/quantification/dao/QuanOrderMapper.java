package com.huobi.quantification.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.huobi.quantification.entity.QuanOrder;

public interface QuanOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanOrder record);

    QuanOrder selectByPrimaryKey(Long id);

    List<QuanOrder> selectAll();

    int updateByPrimaryKey(QuanOrder record);
    
    List<QuanOrder> selectList(QuanOrder record);

	List<QuanOrder> selectByOrderInfo(@Param("orderState")Integer orderState);

	int updateOrderByOrderId(QuanOrder quanOrder);
	
	int insertAndGetId(QuanOrder record);
	
	List<Long> selectOrderIdsByParams(Map<String, Object> map);
}