package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanJobFuture;

import java.util.List;

public interface QuanJobFutureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuanJobFuture record);

    int insertSelective(QuanJobFuture record);

    QuanJobFuture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuanJobFuture record);

    int updateByPrimaryKey(QuanJobFuture record);

    void insertOrUpdate(QuanJobFuture jobFuture);

    List<QuanJobFuture> selectAll();

    List<QuanJobFuture> selectList(QuanJobFuture quanJob);

}