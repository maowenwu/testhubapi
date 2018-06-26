package com.huobi.contract.index.dao;

import com.huobi.contract.index.entity.ContractPriceIndexOkex;

import java.util.List;

public interface ContractPriceIndexOkexMapper {
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
    int insert(ContractPriceIndexOkex record);
    /**
     * @desc 根据对象属性值插入数据
     * @param record
     * @return
     */
    int insertSelective(ContractPriceIndexOkex record);
    /**
     * 根据主键ID 查询数据
     * @param id
     * @return
     */
    ContractPriceIndexOkex selectByPrimaryKey(Long id);
    /**
     * 根据对象值跟更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ContractPriceIndexOkex record);
    /**
     * 更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(ContractPriceIndexOkex record);

    /**
     * 获取所有币对OKEX的最新价格
     * @return
     */
    List<ContractPriceIndexOkex> listLastOkexPrice();
}