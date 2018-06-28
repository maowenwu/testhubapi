package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanDepthFuture;
import java.util.List;

public interface QuanDepthFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanDepthFuture record);

    int insertAndGetId(QuanDepthFuture record);

    QuanDepthFuture selectByPrimaryKey(Long id);

    List<QuanDepthFuture> selectAll();

    int updateByPrimaryKey(QuanDepthFuture record);
}