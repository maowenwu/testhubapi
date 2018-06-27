package com.huobi.quantification.dao;

import java.util.List;

public interface QuanOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanOrder record);

    QuanOrder selectByPrimaryKey(Long id);

    List<QuanOrder> selectAll();

    int updateByPrimaryKey(QuanOrder record);
}