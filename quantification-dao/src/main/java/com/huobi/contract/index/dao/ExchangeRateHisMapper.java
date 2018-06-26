package com.huobi.contract.index.dao;

import com.huobi.contract.index.dto.ExchangeRateHisSymbol;
import com.huobi.contract.index.entity.ExchangeRateHis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeRateHisMapper {
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
    int insert(ExchangeRateHis record);
    /**
     * @desc 根据对象属性值插入数据
     * @param record
     * @return
     */
    int insertSelective(ExchangeRateHis record);
    /**
     * 根据主键ID 查询数据
     * @param id
     * @return
     */
    ExchangeRateHis selectByPrimaryKey(Long id);
    /**
     * 根据对象值跟更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ExchangeRateHis record);
    /**
     * 更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(ExchangeRateHis record);

    /**
     * 根据币对列出前一段时间的记录
     * @param symbol
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ExchangeRateHis> listBeforeRecordBySymbol(@Param("symbol") String symbol,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    /**
     * 获取币对的最新采集的汇率
     * @param symbol
     * @return
     */
    ExchangeRateHis getLastRateHisBySymbol(String symbol);

    /**
     * 查询当前日期的汇率是否录入
     * @param currDate
     * @param likeString
     * @return
     */
    List<ExchangeRateHis> listExchangeRateHisLikeSymbol(@Param("currDate") String currDate,@Param("likeString") String likeString);

    /**
     * 获取各币对前n天时间的价格抓取状态记录
     * @param beginTime
     */
    List<ExchangeRateHisSymbol> listExchangeRateHisGroupSymbol(String beginTime);
}