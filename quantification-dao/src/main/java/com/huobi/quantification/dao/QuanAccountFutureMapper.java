package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountFuture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanAccountFutureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFuture record);

    int insertSelective(QuanAccountFuture record);

    QuanAccountFuture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountFuture record);

    int updateByPrimaryKey(QuanAccountFuture record);

    List<QuanAccountFuture> selectByExId(@Param("exId") int exId);

    Long selectAccountFutureId(@Param("exId")int exId, @Param("accountSourceId")Long accountSourceId);

    List<QuanAccountFuture> selectList(QuanAccountFuture account);
}