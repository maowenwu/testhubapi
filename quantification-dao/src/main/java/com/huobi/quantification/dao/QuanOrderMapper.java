package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanOrder;

import java.util.List;

public interface QuanOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanOrder record);

    QuanOrder selectByPrimaryKey(Long id);

    List<QuanOrder> selectAll();

    int updateByPrimaryKey(QuanOrder record);
}