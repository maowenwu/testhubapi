package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanJobFuture;
import java.util.List;

public interface QuanJobFutureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuanJobFuture record);

    QuanJobFuture selectByPrimaryKey(Integer id);

    List<QuanJobFuture> selectAll();

    int updateByPrimaryKey(QuanJobFuture record);

    void insertOrUpdate(QuanJobFuture jobFuture);
}