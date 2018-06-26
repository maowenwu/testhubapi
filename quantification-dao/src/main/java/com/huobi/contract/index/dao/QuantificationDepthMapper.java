package com.huobi.contract.index.dao;

import com.huobi.quantification.index.entity.QuantificationDepth;

public interface QuantificationDepthMapper {
    int insert(QuantificationDepth record);

    int insertSelective(QuantificationDepth record);
}