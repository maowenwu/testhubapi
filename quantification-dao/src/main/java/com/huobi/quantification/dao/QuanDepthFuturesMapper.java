package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanDepthFutures;
import java.util.List;

public interface QuanDepthFuturesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanDepthFutures record);

    QuanDepthFutures selectByPrimaryKey(Long id);

    List<QuanDepthFutures> selectAll();

    int updateByPrimaryKey(QuanDepthFutures record);
}