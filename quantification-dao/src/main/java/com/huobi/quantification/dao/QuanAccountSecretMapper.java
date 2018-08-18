package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountSecret;
import java.util.List;

public interface QuanAccountSecretMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountSecret record);

    int insertSelective(QuanAccountSecret record);

    QuanAccountSecret selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountSecret record);

    int updateByPrimaryKey(QuanAccountSecret record);

	List<QuanAccountSecret> selectByAccountId(Long id);
}