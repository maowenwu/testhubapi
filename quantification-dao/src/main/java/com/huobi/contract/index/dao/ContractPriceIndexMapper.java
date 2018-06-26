package com.huobi.contract.index.dao;

import com.huobi.contract.index.dto.SymbolPriceDiffScaleAvg;
import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.entity.HisPriceTimePointQueryBean;
import com.huobi.contract.index.facade.entity.ExchangePrice;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ContractPriceIndexMapper {
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
    int insert(ContractPriceIndex record);
    /**
     * @desc 根据对象属性值插入数据
     * @param record
     * @return
     */
    Long insertSelective(ContractPriceIndex record);
    /**
     * 根据主键ID 查询数据
     * @param id
     * @return
     */
    ContractPriceIndex selectByPrimaryKey(Long id);
    /**
     * 根据对象值跟更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ContractPriceIndex record);
    /**
     * 更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(ContractPriceIndex record);

    /**
     * 根据symbol查询指数价格
     * @param indexSymbol
     * @return
     */
    BigDecimal selectByIndexSymbol(String indexSymbol);

    /**
     * 根据币对获取最新的价格信息
     * @param indexSymbol
     * @return
     */
    ContractPriceIndex getLastIndex(String indexSymbol);

    /**
     *获取最新的指数价格
     * @return
     */
    List<ContractPriceIndex> listContractIndexPrice();

    /**
     * 获取历史时刻的价格
     * @param bean
     * @return
     */
    List<ExchangePrice> getHisTimePointPrice(HisPriceTimePointQueryBean bean);


    /**
     * 获取一段时间内 每一分钟最新指数价格与okex价格偏差（大-小）/ 小 的平均值
     * @param startTime
     * @param endTime
     */
    List<SymbolPriceDiffScaleAvg> listIndexPriceWithOkexDiffScale(@Param("startTime") String startTime, @Param("endTime") String endTime);
}