package com.huobi.quantification.dao;

import java.util.List;

public interface QuanKlineMapper {
    int insert(QuanKline record);

    List<QuanKline> selectAll();
}