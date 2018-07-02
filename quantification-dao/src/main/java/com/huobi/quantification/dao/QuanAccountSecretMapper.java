package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountSecret;
import java.util.List;

public interface QuanAccountSecretMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountSecret record);

    QuanAccountSecret selectByPrimaryKey(Long id);

    List<QuanAccountSecret> selectAll();

    int updateByPrimaryKey(QuanAccountSecret record);
}