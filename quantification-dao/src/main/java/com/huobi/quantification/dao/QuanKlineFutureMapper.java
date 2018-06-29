package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanKlineFuture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanKlineFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanKlineFuture record);

    QuanKlineFuture selectByPrimaryKey(Long id);

    List<QuanKlineFuture> selectAll();

    int updateByPrimaryKey(QuanKlineFuture record);

    QuanKlineFuture selectLatestKlineFuture(@Param("symbol") String symbol, @Param("type") String type, @Param("contractType") String contractType);
}