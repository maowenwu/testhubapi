package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFutureSecret;
import com.huobi.quantification.entity.QuanAccountSecret;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanAccountFutureSecretMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFutureSecret record);

    int insertSelective(QuanAccountFutureSecret record);

    QuanAccountFutureSecret selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountFutureSecret record);

    int updateByPrimaryKey(QuanAccountFutureSecret record);

    List<QuanAccountFutureSecret> selectBySourceId(@Param("id") Long id);

    List<QuanAccountFutureSecret> selectSecretById(@Param("id")Long id);
}