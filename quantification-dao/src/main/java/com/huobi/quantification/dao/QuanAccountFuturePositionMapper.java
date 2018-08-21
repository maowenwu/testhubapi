package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFuturePosition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanAccountFuturePositionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFuturePosition record);

    int insertSelective(QuanAccountFuturePosition record);

    QuanAccountFuturePosition selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountFuturePosition record);

    int updateByPrimaryKey(QuanAccountFuturePosition record);

    List<QuanAccountFuturePosition> selectByBaseCoin(QuanAccountFuturePosition quanAccountFuturePosition);

    //通过accountSourceId 和 baseCoin 查询最新的资产信息
    QuanAccountFuturePosition selectLatestByAccountSourceIdBaseCoin(@Param("accountSourceId") Long accountSourceId, @Param("baseCoin") String baseCoin);
}