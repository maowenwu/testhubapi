package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountsAsset;
import java.util.List;

public interface QuanAccountsAssetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountsAsset record);

    QuanAccountsAsset selectByPrimaryKey(Long id);

    List<QuanAccountsAsset> selectAll();

    int updateByPrimaryKey(QuanAccountsAsset record);
}