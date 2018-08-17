package com.huobi.quantification.dao;

import com.huobi.quantification.entity.BossShiroRight;

import java.util.List;

public interface BossShiroRightMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BossShiroRight record);

    BossShiroRight selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(BossShiroRight record);

    List<BossShiroRight> selectList(BossShiroRight entity);
}