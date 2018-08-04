package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFutureAsset;

public interface QuanAccountFutureAssetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFutureAsset record);

    int insertSelective(QuanAccountFutureAsset record);

    QuanAccountFutureAsset selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountFutureAsset record);

    int updateByPrimaryKey(QuanAccountFutureAsset record);
}