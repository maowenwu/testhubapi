package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountsSub;
import java.util.List;

public interface QuanAccountsSubMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountsSub record);

    QuanAccountsSub selectByPrimaryKey(Long id);

    List<QuanAccountsSub> selectAll();

    int updateByPrimaryKey(QuanAccountsSub record);
}