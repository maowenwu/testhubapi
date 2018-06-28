package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountAsset;
import java.util.List;

public interface QuanAccountAssetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountAsset record);

    QuanAccountAsset selectByPrimaryKey(Long id);

    List<QuanAccountAsset> selectAll();

    int updateByPrimaryKey(QuanAccountAsset record);
}