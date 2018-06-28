package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFutureAsset;
import java.util.List;

public interface QuanAccountFutureAssetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFutureAsset record);

    QuanAccountFutureAsset selectByPrimaryKey(Long id);

    List<QuanAccountFutureAsset> selectAll();

    int updateByPrimaryKey(QuanAccountFutureAsset record);
}