package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountSub;
import java.util.List;

public interface QuanAccountSubMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountSub record);

    QuanAccountSub selectByPrimaryKey(Long id);

    List<QuanAccountSub> selectAll();

    int updateByPrimaryKey(QuanAccountSub record);
}