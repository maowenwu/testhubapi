package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanIndexFuture;
import java.util.List;

public interface QuanIndexFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanIndexFuture record);

    QuanIndexFuture selectByPrimaryKey(Long id);

    List<QuanIndexFuture> selectAll();

    int updateByPrimaryKey(QuanIndexFuture record);
}