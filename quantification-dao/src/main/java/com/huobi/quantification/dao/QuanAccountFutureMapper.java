package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFuture;
import java.util.List;

public interface QuanAccountFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFuture record);

    QuanAccountFuture selectByPrimaryKey(Long id);

    List<QuanAccountFuture> selectAll();

    int updateByPrimaryKey(QuanAccountFuture record);
}