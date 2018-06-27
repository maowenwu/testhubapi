package com.huobi.quantification.dao;

import com.huobi.quantification.TOrderSequence;

import java.util.List;

public interface TOrderSequenceMapper {
    int deleteByPrimaryKey(Long fId);

    int insert(TOrderSequence record);

    TOrderSequence selectByPrimaryKey(Long fId);

    List<TOrderSequence> selectAll();

    int updateByPrimaryKey(TOrderSequence record);
}