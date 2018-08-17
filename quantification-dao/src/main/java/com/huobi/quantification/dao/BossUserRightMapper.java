package com.huobi.quantification.dao;

import com.huobi.quantification.entity.BossUserRight;
import java.util.List;

public interface BossUserRightMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BossUserRight record);

    BossUserRight selectByPrimaryKey(Integer id);

    List<BossUserRight> selectAll();

    int updateByPrimaryKey(BossUserRight record);
}