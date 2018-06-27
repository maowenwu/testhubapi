package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountsFutures;
import java.util.List;

public interface QuanAccountsFuturesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountsFutures record);

    QuanAccountsFutures selectByPrimaryKey(Long id);

    List<QuanAccountsFutures> selectAll();

    int updateByPrimaryKey(QuanAccountsFutures record);
}