package com.huobi.contract.index.dao;

import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.ExchangeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeInfoMapper {
    /**
     * 指数价格历史表
     * @desc 根据ID删除记录
     * @param exchangeId
     * @return
     */
    int deleteByPrimaryKey(Long exchangeId);
    /**
     * @desc 插入记录
     * @param record
     * @return
     */
    int insert(ExchangeInfo record);
    /**
     * @desc 根据对象属性值插入数据
     * @param record
     * @return
     */
    int insertSelective(ExchangeInfo record);
    /**
     * 根据主键ID 查询数据
     * @param exchangeId
     * @return
     */
    ExchangeInfo selectByPrimaryKey(Long exchangeId);
    /**
     * 根据对象值跟更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ExchangeInfo record);
    /**
     * 更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(ExchangeInfo record);

    /**
     * 获取交易所下可抓取的币对信息
     * @param exhangeName
     * @return
     */
    List<ExchangeIndex> getValidIndexByExchangeName(String exhangeName);

    /**
     * 获取所有待抓取的指数信息
     * @param exhangeName
     * @return
     */
    List<ExchangeIndex> getAllExchangeIndex(String exhangeName);

    /**
     * 获取币对有效的交易所信息
     * @param symbol
     * @return
     */
    List<ExchangeIndex> getValidExchangeIndexBySymbol(String symbol);

    /**
     * 获取所有有效的交易所和币对信息
     * @return
     */
    List<ExchangeIndex> listValidExchangeIndex();

    /**
     * 获取中位数的ID
     * @return
     */
    ExchangeInfo getMidNumberId(String shortName);

    /**
     * 根据shortName 获取交易所
     * @param shortName
     * @return
     */
    ExchangeInfo getExchangeByShortName(String shortName);

    /**
     * 根据交易所名称和币对名称获取指数信息
     * @param shortName
     * @param symbol
     * @return
     */
    ExchangeIndex findByNameAndSymbol(@Param("shortName") String shortName, @Param("symbol") String symbol);
}