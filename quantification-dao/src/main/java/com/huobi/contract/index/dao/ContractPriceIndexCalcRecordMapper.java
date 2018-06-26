package com.huobi.contract.index.dao;

import com.huobi.contract.index.dto.SymbolPriceDiffScaleAvg;
import com.huobi.contract.index.entity.ContractPriceIndexCalcRecord;
import com.huobi.contract.index.entity.HisPriceTimePointQueryBean;
import com.huobi.contract.index.facade.entity.ExchangePrice;
import com.huobi.contract.index.facade.entity.SymbolChangeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 指数计算记录表
 */
public interface ContractPriceIndexCalcRecordMapper {
    /**
     * @desc 根据ID删除记录
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);
    /**
     * @desc 插入对象记录
     * @param record
     * @return
     */
    int insert(ContractPriceIndexCalcRecord record);
    /**
     * @desc 根据对象属性值插入数据
     * @param record
     * @return
     */
    int insertSelective(ContractPriceIndexCalcRecord record);
    /**
     * 根据主键ID 查询数据
     * @param id
     * @return
     */
    ContractPriceIndexCalcRecord selectByPrimaryKey(Long id);
    /**
     * 根据对象值跟更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ContractPriceIndexCalcRecord record);
    /**
     * 更新数据库记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(ContractPriceIndexCalcRecord record);

    /**
     * @desc 根据symbol和price_index_id 获取记录
     * @param record
     * @return listPriceCalcRecordBySymbolAndPriceIndexId
     */
    List<ContractPriceIndexCalcRecord> listPriceCalcRecordBySymbolAndPriceIndexId(ContractPriceIndexCalcRecord record);

    /**
     * @desc 根据price_index_id 和 symbol 获取中位数
     * @param record
     * @return
     */
    ContractPriceIndexCalcRecord getMidNumByContractIndexIdAndSymbol(ContractPriceIndexCalcRecord record);

    /**
     * @desc 获取历史的记录
     * @param hisPriceTimePointQueryBean
     * @return
     */
    List<ExchangePrice>  getHisTimePointPrice(HisPriceTimePointQueryBean hisPriceTimePointQueryBean);

    /**
     * 获取指数变更记录
     * @param symbol 币对
     * @param pageSize 每页数量
     * @param beginNum 从beginNum 开始
     * @return
     */
    List<SymbolChangeRecord> listSymbolChangeRecord(@Param("symbol")String symbol,@Param("pageSize")Integer pageSize,@Param("beginNum")Integer beginNum);

    /**
     * 获取指数变更记录总数
     * @param symbol
     * @return
     */
    Long getSymbolChangeRecordCount(@Param("symbol")String symbol);

    /**
     * 获取一段时间内，每次参与计算的每一个币对的（最高价-最低价）/最低价 的偏差平均值
     * @param startTime
     * @param endTime
     */
    List<SymbolPriceDiffScaleAvg> listSymbolMaxWithMinDeviationRateAvg(@Param("startTime") String startTime, @Param("endTime") String endTime);
}