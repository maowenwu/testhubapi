package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanOrderFuture;
import java.util.List;

public interface QuanOrderFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanOrderFuture record);

    QuanOrderFuture selectByPrimaryKey(Long id);

    List<QuanOrderFuture> selectAll();

    int updateByPrimaryKey(QuanOrderFuture record);

    void insertOrUpdate(QuanOrderFuture orderFuture);

    List<Long> selectUnfinishOrderSourceId();


}