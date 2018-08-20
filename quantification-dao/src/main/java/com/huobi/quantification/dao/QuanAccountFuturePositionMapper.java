package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanAccountFuturePosition;

import java.util.List;

public interface QuanAccountFuturePositionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanAccountFuturePosition record);

    int insertSelective(QuanAccountFuturePosition record);

    QuanAccountFuturePosition selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuanAccountFuturePosition record);

    int updateByPrimaryKey(QuanAccountFuturePosition record);

    List<QuanAccountFuturePosition> selectByBaseCoin(QuanAccountFuturePosition quanAccountFuturePosition);
}