package com.huobi.quantification.dao;

import java.util.List;

import com.huobi.quantification.entity.QuanDepth;

public interface QuanDepthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanDepth record);

    QuanDepth selectByPrimaryKey(Long id);

    List<QuanDepth> selectAll();

    int updateByPrimaryKey(QuanDepth record);
    
    int insertAndGetId(QuanDepth record);
}