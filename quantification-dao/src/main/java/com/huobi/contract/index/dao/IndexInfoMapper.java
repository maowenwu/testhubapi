package com.huobi.contract.index.dao;

import com.huobi.contract.index.entity.IndexInfo;

import java.util.List;

public interface IndexInfoMapper {
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
    int insert(IndexInfo record);
    /**
     * @desc 根据对象属性值插入数据
     * @param record
     * @return
     */
    int insertSelective(IndexInfo record);
    /**
     * 根据主键ID 查询数据
     * @param id
     * @return
     */
    IndexInfo selectByPrimaryKey(Long id);
    /**
     * 根据对象值跟更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(IndexInfo record);
    /**
     * 更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(IndexInfo record);

    /**
     * 获取所有有效的币对
     * @return
     */
    List<IndexInfo> listAvaidlIndexInfo();
}