package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccounts;

import java.util.List;

public interface QuanAccountsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccounts record);

    QuanAccounts selectByPrimaryKey(Long id);

    List<QuanAccounts> selectAll();

    int updateByPrimaryKey(QuanAccounts record);
}