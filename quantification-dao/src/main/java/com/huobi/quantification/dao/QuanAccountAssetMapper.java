package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountAsset;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanAccountAssetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountAsset record);

    int insertSelective(QuanAccountAsset record);

    QuanAccountAsset selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountAsset record);

    int updateByPrimaryKey(QuanAccountAsset record);

    QuanAccountAsset selectByAccountSourceIdCoinType(@Param("accountSourceId") Long accountSourceId, @Param("coinType") String coinType);

    //通过accountId和coinType查询最新的资产信息
    QuanAccountAsset selectLatestByAccountIdCoinType(@Param("accountId") Long accountId, @Param("coinType") String coinType);

    List<QuanAccountAsset> selectInitedAssetByAccountId(@Param("accountId") Long accountId);
}