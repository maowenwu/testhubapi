package com.huobi.quantification.dao;

import java.util.List;

public interface QuanTickerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanTicker record);

    QuanTicker selectByPrimaryKey(Long id);

    List<QuanTicker> selectAll();

    int updateByPrimaryKey(QuanTicker record);
}