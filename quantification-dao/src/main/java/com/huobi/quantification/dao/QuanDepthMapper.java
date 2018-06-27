package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanDepth;
import java.util.List;

public interface QuanDepthMapper {
    int insert(QuanDepth record);

    List<QuanDepth> selectAll();
}