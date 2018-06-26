package com.huobi.contract.index.facade.service;

import com.huobi.contract.index.facade.entity.*;

import java.util.List;

/**
 * <p>合约指数对外接口</p>
 * @author mingjianyong
 * @see ContractIndexMonitorService
 * @see #listContrctIndexBySymbol(String)
 * @see #listContractIndexChangeRecord(String, Integer, Integer)
 * @see #listContractIndexHistoryPrice(String, Integer)
 * @see #getLastIndexPrice()
 *
 */
public interface ContractIndexMonitorService {
    /**
     * <p>获取当前品种指数列表，中位数，当前价格信息</p>
     * @param symbol 品种(eg:BTC-USD)(所有品种以美元计价) 不能为空
     * @return ContractIndexListResult
     */
    ContractIndexListResult listContrctIndexBySymbol(String symbol);

    /**
     * <p>获取某个品种各个交易所，中位数，指数价格，标记价格的历史价格信息</p>
     * @param symbol 品种信息(eg:BTC-USD)(所有品种以美元计价) 不能为空
     * @param timeInterval  时间间隔(秒为单位) 不能为空
     * @return List<TargetHisPriceListResult>
     */
    List<TargetHisPriceListResult> listContractIndexHistoryPrice(String symbol, Integer timeInterval);

    /**
     *<p>获取指数变更记录</p>
     * @param symbol 品种(eg:BTC-USD)(所有品种以美元计价) 可为空
     * @param pageSize  每页数量 (不能为空)
     * @param currentPage   当前页 (不能为空)
     * @return PageResult
     */
    PageResult<SymbolChangeRecord> listContractIndexChangeRecord(String symbol, Integer pageSize, Integer currentPage);

    /**
     * <p>获取当前的指数价格</p>
     * @return List<ContractPriceIndex>
     */
    List<ContractPriceIndex> getLastIndexPrice();

    /**
     * <p>获取币对的汇率价格</p>
     * @param indexSymbol 币对（eg:BTC-USD,CNY-USD,USD-CNY） 不能为空
     * @return
     */
    ExchangeRateResult getExchangeRate(String indexSymbol);

}
