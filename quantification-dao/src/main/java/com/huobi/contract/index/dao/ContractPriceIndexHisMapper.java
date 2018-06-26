package com.huobi.contract.index.dao;

import com.huobi.contract.index.dto.ContractPriceIndexHisCount;
import com.huobi.contract.index.dto.ContractPriceIndexHisStatusCount;
import com.huobi.contract.index.entity.ContractPriceIndexCalcRecord;
import com.huobi.contract.index.entity.ContractPriceIndexHis;
import com.huobi.contract.index.entity.ExchangeInfo;
import com.huobi.contract.index.entity.IndexQualifiedPriceRatio;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ContractPriceIndexHisMapper {
    /**
     * 指数价格历史表
     *
     * @param id
     * @return
     * @desc 根据ID删除记录
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     * @desc 插入记录
     */
    int insert(ContractPriceIndexHis record);

    /**
     * @param record
     * @return
     * @desc 根据对象属性值插入数据
     */
    int insertSelective(ContractPriceIndexHis record);

    /**
     * 根据主键ID 查询数据
     *
     * @param id
     * @return
     */
    ContractPriceIndexHis selectByPrimaryKey(Long id);

    /**
     * 根据对象值跟更新数据库记录
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ContractPriceIndexHis record);

    /**
     * 更新数据库记录
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(ContractPriceIndexHis record);

    /**
     * @param contractPriceIndexCalcRecord
     * @return
     * @desc 获取该币对前100次的抓取成功数
     */
    int getBefore100SuccCount(ContractPriceIndexCalcRecord contractPriceIndexCalcRecord);

    /**
     * @param record
     * @return getLastSymbolPriceByExchangeAndSymbol
     * @desc 获取最新抓取的价格
     */
    ContractPriceIndexHis getLastSymbolPriceByExchangeAndSymbol(ContractPriceIndexHis record);

    /**
     * @param record
     * @return getLastSymbolPriceByExchangeAndSymbol
     * @desc 获取最新抓取的价格
     */
    /**
     *
     * @param exchangeId 交易所ID
     * @param targetSymbol 币对
     * @param origin    来源
     * @return
     */
    ContractPriceIndexHis getLastContractPriceIndexHis(@Param("exchangeId") Long exchangeId, @Param("targetSymbol") String targetSymbol, @Param("origin") int origin);


    /**
     *
     * @param startTime 查询开始时间
     * @param origin    来源
     * @return
     */
    List<ContractPriceIndexHisCount> listContractIndexHisCount(@Param("startTime") String startTime,@Param("origin")int origin);

    /**
     * @param startTime 开始时间
     * @Param origin 来源（0：http,1ws）
     * @return
     * @desc 获取时间间隔内各币对的有效的历史记录数
     */
    List<ContractPriceIndexHisCount> listEffectiveContractIndexHisCount(@Param("startTime") String startTime,@Param("origin")int origin);

    /**
     * 根据币对获取指数价格历史记录
     *
     * @param symbol
     * @return
     */
    List<ContractPriceIndexHis> listContractIndexHisBySymbol(String symbol);

    void insertBatch(@Param("hisList") List<ContractPriceIndexHis> hisList);

    List<IndexQualifiedPriceRatio> selectIndexQualifiedPriceRatio(@Param("origin")int origin, @Param("date")Date date);

    /**
     * 统计交易所-币对 抓取失败和成功的记录数量
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<ContractPriceIndexHisStatusCount> listIndexHisStatusCount(@Param("startTime") String startTime, @Param("endTime") String endTime);
}