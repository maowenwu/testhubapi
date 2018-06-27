package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanTickerFutures;
import java.util.List;

public interface QuanTickerFuturesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanTickerFutures record);

    QuanTickerFutures selectByPrimaryKey(Long id);

    List<QuanTickerFutures> selectAll();

    int updateByPrimaryKey(QuanTickerFutures record);
}