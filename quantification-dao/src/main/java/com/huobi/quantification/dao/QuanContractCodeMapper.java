package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanContractCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuanContractCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuanContractCode record);

    int insertSelective(QuanContractCode record);

    QuanContractCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuanContractCode record);

    int updateByPrimaryKey(QuanContractCode record);

    /*自定义方法*/
    List<QuanContractCode> selectByExchangeId(int exId);

    QuanContractCode selectContractCode(@Param("exchangeId") int exchangeId, @Param("symbol") String symbol, @Param("contractType") String contractType);

    QuanContractCode selectContractCodeByCode(@Param("exchangeId") int exchangeId, @Param("contractCode") String contractCode);
}