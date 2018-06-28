package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanDepthFutureDetail;
import java.util.List;

public interface QuanDepthFutureDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanDepthFutureDetail record);

    QuanDepthFutureDetail selectByPrimaryKey(Long id);

    List<QuanDepthFutureDetail> selectAll();

    int updateByPrimaryKey(QuanDepthFutureDetail record);
}