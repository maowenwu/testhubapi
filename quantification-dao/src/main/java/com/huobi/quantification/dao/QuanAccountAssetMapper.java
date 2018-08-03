package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountAsset;

public interface QuanAccountAssetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountAsset record);

    int insertSelective(QuanAccountAsset record);

    QuanAccountAsset selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountAsset record);

    int updateByPrimaryKey(QuanAccountAsset record);
}