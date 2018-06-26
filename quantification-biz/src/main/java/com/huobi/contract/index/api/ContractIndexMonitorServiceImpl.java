package com.huobi.contract.index.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.dao.*;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.entity.*;
import com.huobi.contract.index.facade.entity.*;
import com.huobi.contract.index.facade.service.ContractIndexMonitorService;
import com.okcoin.rest.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(interfaceName = "com.huobi.contract.index.facade.service.ContractIndexMonitorService")
public class ContractIndexMonitorServiceImpl extends BaseContractIndexService implements ContractIndexMonitorService {

    private static final Logger LOG = LoggerFactory.getLogger(ContractIndexMonitorServiceImpl.class);

    private static final String INDEX_NAME = "指数价格";

    private static final Integer ONE_HOUR_SERCOND = 60*60;
    @Value("${index.his.price.hour.intrrval}")
    private Integer IndexhisPriceInterval;
    @Autowired
    private ContractPriceIndexMapper contractPriceIndexMapper;

    @Autowired
    private ContractPriceIndexCalcRecordMapper contractPriceIndexCalcRecordMapper;

    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;

    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    @Autowired
    private IndexInfoMapper indexInfoMapper;

    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public ContractIndexListResult listContrctIndexBySymbol(String symbol) {
        if(StringUtil.isEmpty(symbol)){
            return null;
        }
        ContractIndexListResult contractIndexListResult = new ContractIndexListResult();
        //获取币对最新的合约指数价格信息
        ContractPriceIndex lastContractPriceIndex = getLastContractPriceIndex(symbol);
        //最新的合约指数价格信息无数据
        if(lastContractPriceIndex == null){
            return contractIndexListResult;
        }
        //获取最新价格计算记录
        try {
            ContractPriceIndexCalcRecord record = new ContractPriceIndexCalcRecord();
            record.setTargetSymbol(symbol);
            record.setPriceIndexId(lastContractPriceIndex.getId());
            //获取最新价格的计算记录信息
            List<ContractPriceIndexCalcRecord> calcRecordList =  contractPriceIndexCalcRecordMapper.listPriceCalcRecordBySymbolAndPriceIndexId(record);
            //获取中位数exchangeId
            ExchangeInfo exchangeInfo = exchangeInfoMapper.getExchangeByShortName(Constant.MEDIAN_SHORT_NAME);
            record.setExchangeId(exchangeInfo.getExchangeId());
            ContractPriceIndexCalcRecord midRecord = contractPriceIndexCalcRecordMapper.getMidNumByContractIndexIdAndSymbol(record);
            //设置中位数价格
            contractIndexListResult.setMidNumber(midRecord.getTargetPrice());
            //设置最新的价格
            contractIndexListResult.setIndexPrice(lastContractPriceIndex.getIndexPrice());
            List<ContractIndexInfo> contractIndexInfos= new ArrayList<ContractIndexInfo>();
            ContractIndexInfo contractIndexInfo = null;
            for(ContractPriceIndexCalcRecord rd : calcRecordList){
                contractIndexInfo = new ContractIndexInfo();
                contractIndexInfo.setExchangeId(rd.getExchangeId());
                contractIndexInfo.setExchangeName(rd.getExchangeFullName());
                contractIndexInfo.setExchangeShortName(rd.getExchangeShortName());
                //当前权重
                contractIndexInfo.setWeight(rd.getWeight());
                //初始权重
                contractIndexInfo.setOriginalWeight(rd.getOriginalWeight());
                //更新时间
                contractIndexInfo.setUpdateTime(DateUtil.parseDateToStr(rd.getInputTime(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
                //获取该币种该交易所前100次的成功数
                contractIndexInfo.setSuccCount(contractPriceIndexHisMapper.getBefore100SuccCount(rd));
                //设置品种在该交易所的价格
                contractIndexInfo.setLastPrice(rd.getTargetPrice());
                //偏离中位数比例
                BigDecimal offertMidPriceRate = (contractIndexInfo.getLastPrice().subtract(contractIndexListResult.getMidNumber())).divide(contractIndexListResult.getMidNumber(),Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
                //偏离指数比例
                BigDecimal offertIndexPriceRate = (contractIndexInfo.getLastPrice().subtract(contractIndexListResult.getIndexPrice())).divide(contractIndexListResult.getIndexPrice(),Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
                contractIndexInfo.setOffsetCentorRate(offertMidPriceRate);
                contractIndexInfo.setOffsetIndexRate(offertIndexPriceRate);
                contractIndexInfos.add(contractIndexInfo);
            }
            contractIndexListResult.setList(contractIndexInfos);
        } catch (Exception e) {
            LOG.error("ContractIndexMonitorService-->listContrctIndexBySymbol 监控图表->指数列表获取失败");
        }
        return contractIndexListResult;
    }

    @Override
    public List<TargetHisPriceListResult> listContractIndexHistoryPrice(String symbol, Integer timeInterval) {
        if(StringUtil.isEmpty(symbol) || timeInterval == null){
            return null;
        }
        List<TargetHisPriceListResult> list = new ArrayList<TargetHisPriceListResult>();
        //获取各个交易所的历史价格
        getExchangeHisPirce(list,symbol,timeInterval);
        //获取中位数历史价格
        getMiddleNumberHisPrice(list, symbol, timeInterval);
        //获取指数历史价格
        getHisIndexPrice(list, symbol, timeInterval);
        return list;
    }

    @Override
    public PageResult<SymbolChangeRecord> listContractIndexChangeRecord(String symbol, Integer pageSize, Integer currentPage) {
        if(pageSize == null){
            pageSize = 5;
        }
        if(currentPage == null){
            currentPage = 1;
        }
        Integer beginNum = (currentPage-1) * pageSize;
        Long total = contractPriceIndexCalcRecordMapper.getSymbolChangeRecordCount(symbol);
        List<SymbolChangeRecord> list = contractPriceIndexCalcRecordMapper.listSymbolChangeRecord(symbol,pageSize,beginNum);
        PageResult<SymbolChangeRecord> result= new PageResult<SymbolChangeRecord>();
        result.setTotal(total);
        result.setData(list);
        return result;
    }

    /**
     * 获取当前所有的指数价格
     * @return
     */
    @Override
    public List<com.huobi.contract.index.facade.entity.ContractPriceIndex> getLastIndexPrice() {
        List<IndexInfo> indexInfoList = indexInfoMapper.listAvaidlIndexInfo();
        List<com.huobi.contract.index.facade.entity.ContractPriceIndex> contractPriceIndexList = new ArrayList<com.huobi.contract.index.facade.entity.ContractPriceIndex>();
        com.huobi.contract.index.facade.entity.ContractPriceIndex fcpi = null;
        for(IndexInfo ii : indexInfoList){
            ContractPriceIndex cpi = getLastContractIndex(ii.getIndexSymbol());
            fcpi = new com.huobi.contract.index.facade.entity.ContractPriceIndex();
            fcpi.setId(cpi.getId());
            fcpi.setIndexPrice(cpi.getIndexPrice());
            fcpi.setIndexSymbol(cpi.getIndexSymbol());
            fcpi.setInputTime(cpi.getInputTime());
            fcpi.setIsWeightChange(cpi.getIsWeightChange());
            fcpi.setLastTime(cpi.getLastTime());
            fcpi.setRemark(cpi.getRemark());
            contractPriceIndexList.add(fcpi);
        }
        return contractPriceIndexList;
    }

    /**
     * 汇率接口
     * @param indexSymbol 币对（eg:BTC-USD,CNY-USD）
     * @return
     */
    @Override
    public ExchangeRateResult getExchangeRate(String indexSymbol) {
        ExchangeRate rate = exchangeRateMapper.getLastExchangeRateBySymbol(indexSymbol);
        ExchangeRateResult result = new ExchangeRateResult();
        if(rate == null){
            ContractPriceIndex price = contractPriceIndexMapper.getLastIndex(indexSymbol);
            result.setExchangeSymbol(price.getIndexSymbol());
            result.setExchangeRate(price.getIndexPrice());
        }else {
            result.setExchangeRate(rate.getExchangeRate());
            result.setExchangeSymbol(rate.getExchangeSymbol());
            result.setInputTime(rate.getInputTime());
        }
        return result;
    }

    /**
     * 获取最新指数信息
     * @param indexSymbol
     * @return
     */
    public ContractPriceIndex getLastContractPriceIndex(String indexSymbol){
        ContractPriceIndex redisContractPriceIndex = redisService.getLatestIndexPirce(indexSymbol);
        if(redisContractPriceIndex == null){
            ContractPriceIndex cpi = contractPriceIndexMapper.getLastIndex(indexSymbol);
            return cpi;
        }else{
            return redisContractPriceIndex;
        }
    }

    /**
     * 获取币对的交易所历史价格信息
     * @param resultList
     * @param symbol
     * @param interval
     */
    public void getExchangeHisPirce(final List<TargetHisPriceListResult> resultList, String symbol, Integer interval){
        List<ExchangeIndex> exchangeIndexList = exchangeInfoMapper.getValidExchangeIndexBySymbol(symbol);
        TargetHisPriceListResult targetResult = null;
        for(ExchangeIndex ei : exchangeIndexList){
            targetResult = new TargetHisPriceListResult();
            HisPriceTimePointQueryBean bean = getHisPriceTimePointQueryBean(ei.getIndexSymbol(),interval);
            List<ExchangePrice> exchangePriceList = contractPriceIndexCalcRecordMapper.getHisTimePointPrice(bean);
            targetResult.setExchangeName(ei.getFullName());
            targetResult.setExchangeShortName(ei.getShortName());
            handleIsNullPrice(exchangePriceList);
            targetResult.setList(exchangePriceList);
            resultList.add(targetResult);
        }
    }

    /**
     * 获取币对中位数历史价格
     * @param resultList
     * @param symbol
     * @param interval
     */
    public void getMiddleNumberHisPrice(final List<TargetHisPriceListResult> resultList, String symbol, Integer interval){
        //获取中位数exchangeId
        ExchangeInfo exchangeInfo = exchangeInfoMapper.getExchangeByShortName(Constant.MEDIAN_SHORT_NAME);
        HisPriceTimePointQueryBean bean = getHisPriceTimePointQueryBean(symbol,interval);
        List<ExchangePrice> exchangePriceList = contractPriceIndexCalcRecordMapper.getHisTimePointPrice(bean);
        handleIsNullPrice(exchangePriceList);
        TargetHisPriceListResult targetResult = new TargetHisPriceListResult();
        targetResult.setExchangeShortName(exchangeInfo.getFullName());
        targetResult.setExchangeName(exchangeInfo.getFullName());
        targetResult.setList(exchangePriceList);
        resultList.add(targetResult);
    }
    /**
     * 获取指数的历史价格
     */
    public void getHisIndexPrice(final List<TargetHisPriceListResult> resultList, String symbol, Integer interval){
        HisPriceTimePointQueryBean bean = getHisPriceTimePointQueryBean(symbol,interval);
        List<ExchangePrice> exchangePriceList = contractPriceIndexMapper.getHisTimePointPrice(bean);
        handleIsNullPrice(exchangePriceList);
        TargetHisPriceListResult targetResult = new TargetHisPriceListResult();
        targetResult.setExchangeShortName(INDEX_NAME);
        targetResult.setExchangeName(INDEX_NAME);
        targetResult.setList(exchangePriceList);
        resultList.add(targetResult);
    }

    /**
     * 封装请求bean
     * @param symbol 币对（eg:BTC-USD）
     * @param interval 点数间隔,单位秒（如：1分钟一个点,5分钟一个点）
     * @return
     */
    private HisPriceTimePointQueryBean getHisPriceTimePointQueryBean(String symbol, Integer interval){
        HisPriceTimePointQueryBean bean  = new HisPriceTimePointQueryBean();
        bean.setSymbol(symbol);
        bean.setPointMount(interval);
        bean.setInterval(interval);
        if(Constant.HISTORY_TIME_INTERVAL_OME_MINUTE  == interval){
            bean.setPointMount(ONE_HOUR_SERCOND/Constant.HISTORY_TIME_INTERVAL_OME_MINUTE*IndexhisPriceInterval);
            bean.setBeginTime(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
        }else if(Constant.HISTORY_TIME_INTERVAL_FIVE_MINUTE  == interval){
            bean.setPointMount(ONE_HOUR_SERCOND/Constant.HISTORY_TIME_INTERVAL_FIVE_MINUTE*IndexhisPriceInterval);
            bean.setBeginTime(DateUtil.parseDateToStr(DateUtil.getRecentFiveMinuteTimeInterval(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
        }else if(Constant.HISTORY_TIME_INTERVAL_FIFTEEN_MINUTE  == interval){
            bean.setPointMount(ONE_HOUR_SERCOND/Constant.HISTORY_TIME_INTERVAL_FIFTEEN_MINUTE*IndexhisPriceInterval);
            bean.setBeginTime(DateUtil.parseDateToStr(DateUtil.getRecentTenMinuteTimeInterval(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
        }else{
            return null;
        }
        return bean;
    }
    /**
     * 处理缺失时刻点的价格，取之前最新的值作为缺失点的值
     * @param exchangePriceList
     * @return
     */
    public void handleIsNullPrice(final List<ExchangePrice> exchangePriceList){
        BigDecimal flagPrice = BigDecimal.ZERO;
        for (ExchangePrice exchangePrice : exchangePriceList){
            if(exchangePrice == null){
                continue;
            }
            if(exchangePrice.getValue() !=null){
                flagPrice = exchangePrice.getValue();
            }else {
                exchangePrice.setValue(flagPrice);
            }
        }
    }

    /**
     * 获取PageHelper中的结果对象
     * @param list
     * @param <T>
     * @return
     */
    public <T> PageResult<T> getPageResult(List<T> list){
        PageResult<T> pageResult = new PageResult();
        pageResult.setData(list);
        System.out.println(list.getClass());
        if (list instanceof Page) {
            Page page = (Page) list;
            pageResult.setTotal(page.getTotal());
        }else{
            pageResult.setTotal(0L);
        }
        return pageResult;
    }
}
