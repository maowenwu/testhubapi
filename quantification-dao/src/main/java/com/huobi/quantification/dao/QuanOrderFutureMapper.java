package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanOrderFuture;

import java.util.List;

public interface QuanOrderFutureMapper {
    int deleteByPrimaryKey(Long innerOrderId);

    int insert(QuanOrderFuture record);

    int insertSelective(QuanOrderFuture record);

    QuanOrderFuture selectByPrimaryKey(Long innerOrderId);

    List<QuanOrderFuture> selectBySelective(QuanOrderFuture record);

    int updateByPrimaryKeySelective(QuanOrderFuture record);

    int updateByPrimaryKey(QuanOrderFuture record);
}