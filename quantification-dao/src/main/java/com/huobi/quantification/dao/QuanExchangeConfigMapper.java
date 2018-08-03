package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanExchangeConfig;
import java.util.List;

public interface QuanExchangeConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuanExchangeConfig record);

    QuanExchangeConfig selectByPrimaryKey(Integer id);

    List<QuanExchangeConfig> selectAll();

    int updateByPrimaryKey(QuanExchangeConfig record);
}