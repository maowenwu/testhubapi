package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanDepthDetail;

import java.util.List;

public interface QuanDepthDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanDepthDetail record);

    QuanDepthDetail selectByPrimaryKey(Long id);

    List<QuanDepthDetail> selectAll();

    int updateByPrimaryKey(QuanDepthDetail record);
}