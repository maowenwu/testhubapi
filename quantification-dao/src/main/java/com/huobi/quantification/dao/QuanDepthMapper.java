package com.huobi.quantification.dao;

import java.util.List;

public interface QuanDepthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanDepth record);

    QuanDepth selectByPrimaryKey(Long id);

    List<QuanDepth> selectAll();

    int updateByPrimaryKey(QuanDepth record);
}