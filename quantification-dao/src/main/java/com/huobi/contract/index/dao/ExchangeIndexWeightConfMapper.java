package com.huobi.contract.index.dao;

import com.huobi.contract.index.entity.ExchangeIndexWeightConf;
import com.huobi.contract.index.entity.IndexQualifiedPriceRatio;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeIndexWeightConfMapper {
    /**
     * 指数价格历史表
     *
     * @param weightId
     * @return
     * @desc 根据ID删除记录
     */
    int deleteByPrimaryKey(Long weightId);


    /**
     * @param record
     * @return
     * @desc 插入记录
     */
    int insert(ExchangeIndexWeightConf record);


    /**
     * 根据主键ID 查询数据
     *
     * @param weightId
     * @return
     */
    ExchangeIndexWeightConf selectByPrimaryKey(Long weightId);


    /**
     * 更新主键ID数据库记录
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(ExchangeIndexWeightConf record);

    /**
     * 根据exchangeID,indexSymbol 更新交易所币对可抓取状态
     *
     * @param record
     * @return
     */
    int updateIsQualifiedByExchangeIDAndIndexSymbol(ExchangeIndexWeightConf record);

    void updateHttpQualifiedValue(@Param("exchangeId") Long exchangeId, @Param("indexSymbol") String indexSymbol, @Param("status") int status);

    void updateWsQualifiedValue(@Param("exchangeId") Long exchangeId, @Param("indexSymbol") String indexSymbol, @Param("status") int status);

    List<ExchangeIndexWeightConf> selectAll();

}