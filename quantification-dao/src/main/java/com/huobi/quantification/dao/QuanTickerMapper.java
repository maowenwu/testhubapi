package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanTicker;
import java.util.List;

public interface QuanTickerMapper {

    int insert(QuanTicker record);

    List<QuanTicker> selectAll();
}