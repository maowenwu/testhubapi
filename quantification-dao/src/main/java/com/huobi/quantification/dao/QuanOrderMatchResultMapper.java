package com.huobi.quantification.dao;

import java.util.List;

import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.entity.QuanOrderMatchResult;

public interface QuanOrderMatchResultMapper {
	int deleteByPrimaryKey(Long id);

    int insert(QuanOrderMatchResult record);

    QuanOrder selectByPrimaryKey(Long id);

    List<QuanOrder> selectAll();

    int updateByPrimaryKey(QuanOrderMatchResult record);
}
