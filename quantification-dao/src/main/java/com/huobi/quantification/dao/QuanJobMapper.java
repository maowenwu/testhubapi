package com.huobi.quantification.dao;

import java.util.List;

import com.huobi.quantification.entity.QuanJob;

public interface QuanJobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuanJob record);

    QuanJob selectByPrimaryKey(Integer id);

    List<QuanJob> selectAll();

    int updateByPrimaryKey(QuanJob record);
    
    void insertOrUpdate(QuanJob job);

    List<QuanJob> selectList(QuanJob quanJob);

    int updateByPrimaryKeySelective(QuanJob quanJob);

}