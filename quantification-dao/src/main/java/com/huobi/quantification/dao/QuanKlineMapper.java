package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanKline;
import java.util.List;

public interface QuanKlineMapper {
    int insert(QuanKline record);

    List<QuanKline> selectAll();
}