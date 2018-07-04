package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanJob;
import java.util.List;

public interface QuanJobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuanJob record);

    QuanJob selectByPrimaryKey(Integer id);

    List<QuanJob> selectAll();

    int updateByPrimaryKey(QuanJob record);
}