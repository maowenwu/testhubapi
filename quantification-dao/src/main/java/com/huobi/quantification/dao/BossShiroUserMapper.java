package com.huobi.quantification.dao;

import com.huobi.quantification.entity.BossShiroUser;
import java.util.List;

public interface BossShiroUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BossShiroUser record);

    BossShiroUser selectByPrimaryKey(Integer id);

    List<BossShiroUser> selectAll();

    int updateByPrimaryKey(BossShiroUser record);

    List<BossShiroUser> selectList(BossShiroUser entity);
}