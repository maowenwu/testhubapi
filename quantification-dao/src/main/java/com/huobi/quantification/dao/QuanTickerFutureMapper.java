package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanTickerFuture;
import java.util.List;

public interface QuanTickerFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanTickerFuture record);

    QuanTickerFuture selectByPrimaryKey(Long id);

    List<QuanTickerFuture> selectAll();

    int updateByPrimaryKey(QuanTickerFuture record);
}