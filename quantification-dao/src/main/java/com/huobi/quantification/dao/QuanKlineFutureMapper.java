package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanKlineFuture;
import java.util.List;

public interface QuanKlineFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanKlineFuture record);

    QuanKlineFuture selectByPrimaryKey(Long id);

    List<QuanKlineFuture> selectAll();

    int updateByPrimaryKey(QuanKlineFuture record);
}