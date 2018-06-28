package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFuturePosition;
import java.util.List;

public interface QuanAccountFuturePositionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFuturePosition record);

    QuanAccountFuturePosition selectByPrimaryKey(Long id);

    List<QuanAccountFuturePosition> selectAll();

    int updateByPrimaryKey(QuanAccountFuturePosition record);
}