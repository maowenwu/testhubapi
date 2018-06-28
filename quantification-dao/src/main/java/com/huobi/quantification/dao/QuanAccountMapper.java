package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccount;
import java.util.List;

public interface QuanAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccount record);

    QuanAccount selectByPrimaryKey(Long id);

    List<QuanAccount> selectAll();

    int updateByPrimaryKey(QuanAccount record);
}