package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanTrade;
import java.util.List;

public interface QuanTradeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanTrade record);

    QuanTrade selectByPrimaryKey(Long id);

    List<QuanTrade> selectAll();

    int updateByPrimaryKey(QuanTrade record);
}