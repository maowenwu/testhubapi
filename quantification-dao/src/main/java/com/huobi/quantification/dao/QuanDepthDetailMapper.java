package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanDepthDetail;
import java.util.List;

public interface QuanDepthDetailMapper {
    int insert(QuanDepthDetail record);

    List<QuanDepthDetail> selectAll();
}