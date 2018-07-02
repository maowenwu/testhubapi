package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFutureSecret;
import java.util.List;

public interface QuanAccountFutureSecretMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFutureSecret record);

    QuanAccountFutureSecret selectByPrimaryKey(Long id);

    List<QuanAccountFutureSecret> selectAll();

    int updateByPrimaryKey(QuanAccountFutureSecret record);
}