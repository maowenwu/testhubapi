package com.huobi.contract.index.dao;

import com.huobi.contract.index.entity.ExchangeRate;

import java.math.BigDecimal;

public interface ExchangeRateMapper {
    /**
     * 指数价格历史表
     * @desc 根据ID删除记录
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);
    /**
     * @desc 插入记录
     * @param record
     * @return
     */
    int insert(ExchangeRate record);
    /**
     * @desc 根据对象属性值插入数据
     * @param record
     * @return
     */
    int insertSelective(ExchangeRate record);
    /**
     * 根据主键ID 查询数据
     * @param id
     * @return
     */
    ExchangeRate selectByPrimaryKey(Long id);
    /**
     * 根据对象值跟更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ExchangeRate record);
    /**
     * 更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(ExchangeRate record);
    /**
     * 获取最新的汇率
     * @param symbol 根据币对
     * @return
     */
    ExchangeRate getLastExchangeRateBySymbol(String symbol);
}