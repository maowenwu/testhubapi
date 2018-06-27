package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanDepthFuturesDetail;
import java.util.List;

public interface QuanDepthFuturesDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanDepthFuturesDetail record);

    QuanDepthFuturesDetail selectByPrimaryKey(Long id);

    List<QuanDepthFuturesDetail> selectAll();

    int updateByPrimaryKey(QuanDepthFuturesDetail record);
}