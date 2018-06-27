package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountsFuturesAsset;
import java.util.List;

public interface QuanAccountsFuturesAssetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountsFuturesAsset record);

    QuanAccountsFuturesAsset selectByPrimaryKey(Long id);

    List<QuanAccountsFuturesAsset> selectAll();

    int updateByPrimaryKey(QuanAccountsFuturesAsset record);
}