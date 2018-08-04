package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccount record);

    int insertSelective(QuanAccount record);

    QuanAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccount record);

    int updateByPrimaryKey(QuanAccount record);

    List<Long> selectAccountByExchangeId(@Param("exId") int exId);

    Long selectAccountId(@Param("exchangeId") int exchangeId, @Param("accountSourceId") Long accountSourceId);
}