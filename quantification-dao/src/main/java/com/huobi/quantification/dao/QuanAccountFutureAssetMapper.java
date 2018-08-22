package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanAccountFutureAssetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFutureAsset record);

    int insertSelective(QuanAccountFutureAsset record);

    QuanAccountFutureAsset selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountFutureAsset record);

    int updateByPrimaryKey(QuanAccountFutureAsset record);

    QuanAccountFutureAsset selectByAccountSourceIdCoinType(@Param("accountSourceId") Long accountSourceId, @Param("coinType") String coinType);

    List<QuanAccountFutureAsset> selectByQuanAccountFuture(QuanAccountFuture quanAccountFuture);


    //通过 accountFutureId 和coinType查询最新的资产信息
    QuanAccountFutureAsset selectLastByAccountFutureIdCoinType(@Param("accountFutureId") Long accountFutureId, @Param("coinType") String coinType);

    List<QuanAccountFutureAsset> selectInitedAssetByAccountFutureId(@Param("accountFutureId") Long accountFutureId);
}