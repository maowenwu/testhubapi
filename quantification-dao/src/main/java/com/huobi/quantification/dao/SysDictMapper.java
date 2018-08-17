package com.huobi.quantification.dao;

import com.huobi.quantification.entity.SysDict;

import java.util.List;

public interface SysDictMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysDict record);

    SysDict selectByPrimaryKey(Long id);

    int updateByPrimaryKey(SysDict record);

    List<SysDict> selectList(SysDict entity);
}