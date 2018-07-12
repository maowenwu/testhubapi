package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanTradeFuture;
import java.util.List;

public interface QuanTradeFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanTradeFuture record);

    QuanTradeFuture selectByPrimaryKey(Long id);

    List<QuanTradeFuture> selectAll();

    int updateByPrimaryKey(QuanTradeFuture record);
}