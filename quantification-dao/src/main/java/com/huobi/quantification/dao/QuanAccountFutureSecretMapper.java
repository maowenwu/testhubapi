package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFutureSecret;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanAccountFutureSecretMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFutureSecret record);

    QuanAccountFutureSecret selectByPrimaryKey(Long id);

    List<QuanAccountFutureSecret> selectAll();

    int updateByPrimaryKey(QuanAccountFutureSecret record);

    List<QuanAccountFutureSecret> selectBySourceId(@Param("id") Long id);
}