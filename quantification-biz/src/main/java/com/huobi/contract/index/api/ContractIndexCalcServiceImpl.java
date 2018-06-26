package com.huobi.contract.index.api;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.contract.index.common.CalcQualified;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.contract.index.service.IpPoolQueue;
import com.huobi.contract.index.contract.index.service.impl.IpPoolQueueImpl;
import com.huobi.contract.index.dao.*;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("contractIndexCalcService")
public class ContractIndexCalcServiceImpl extends BaseContractIndexService implements ContractIndexCalcService {
    /**
     * 与中位数的偏差比例isMedianDeviation
     */
    @Value("${is_median_deviation}")
    private BigDecimal isMedianDeviation;
    /**
     * 两家交易所的偏差比例
     * twoExchangeDeviation
     */
    @Value("${two_exchange_deviation}")
    private BigDecimal twoExchangeDeviation;
    /**
     * 与最新价格的偏差比例
     * isLatestIndexPriceDeviation
     */
    @Value("${is_latest_index_price_deviation}")
    private BigDecimal isLatestIndexPriceDeviation;
    /**
     * 权重总值
     */
    private static final BigDecimal WEIGHT_TOTAL = new BigDecimal(100);
    /**
     * 权重变更
     */
    private static final byte WEIGHT_IS_CHANGE = 1;
    /**
     * 权重未变更
     */
    private static final byte WEIGHT_IS_NOT_CHANGE = 0;

    private static final Logger LOG = LoggerFactory.getLogger(ContractIndexCalcServiceImpl.class);

    @Autowired
    private ContractPriceIndexMapper contractPriceIndexMapper;

    @Autowired
    private ContractPriceIndexCalcRecordMapper contractPriceIndexCalcRecordMapper;

    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;

    @Autowired
    private IndexInfoMapper indexInfoMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ExchangeGrabConfMapper exchangeGrabConfMapper;
    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;


    /**
     * 价格计算
     */
    @Override
    public void calc() {
        //获取所有的币对
        List<IndexInfo> indexInfoList = indexInfoMapper.listAvaidlIndexInfo();
        for (IndexInfo indexInfo : indexInfoList) {
            //获取指定交易币对可抓取的交易所信息
            //获取币对有效的交易所信息
            List<ExchangeIndex> isValidExchangeIndexList = exchangeInfoMapper.getValidExchangeIndexBySymbol(indexInfo.getIndexSymbol());
            if (CollectionUtils.isEmpty(isValidExchangeIndexList)) {
                continue;
            }
            //获取该币下所有交易所的最新历史价格信息
            List<ContractPriceIndexHis> contractPriceIndexHisList = getAllHisPriceFromRedis(isValidExchangeIndexList);
            if (CollectionUtils.isEmpty(contractPriceIndexHisList)) {
                continue;
            }
            //获取该币对下参与计算的交易所最新历史价格信息
            List<ContractPriceIndexHis> isGrabContractPriceIndexHisList = getCalcHisPriceFromRedis(isValidExchangeIndexList);
            //有效的价格为空，不计算
            if (CollectionUtils.isEmpty(isGrabContractPriceIndexHisList)) {
                continue;
            }
            if (isGrabContractPriceIndexHisList.size() > 2) {
                //获取中位数
                BigDecimal middlePrice = getMiddlePrice(isGrabContractPriceIndexHisList);
                //计算后的价格信息
                List<ContractPriceIndexHis> newContractPriceIndexHisList = handleContractPrice(isGrabContractPriceIndexHisList, middlePrice);
                //插入到合约指数价格表，指数计算记录表
                handleMoreThanTwoExchange(newContractPriceIndexHisList, contractPriceIndexHisList, indexInfo.getIndexSymbol(), middlePrice, isValidExchangeIndexList);
            } else if (isGrabContractPriceIndexHisList.size() == 2) {
                //比较两家价格偏差
                handleTwoExchange(isGrabContractPriceIndexHisList, contractPriceIndexHisList, indexInfo.getIndexSymbol(), isValidExchangeIndexList);
            } else {
                handleOneExchange(isGrabContractPriceIndexHisList, contractPriceIndexHisList, indexInfo.getIndexSymbol(), isValidExchangeIndexList);
            }
        }
    }

    /**
     * 从redis中取出某一币种参与计算的交易所的最新价格信息
     *
     * @param exchangeIndexList 某一币种对应的交易所等信息
     * @return List<ContractPriceIndexHis> 币种对应的每一个交易所的价格信息
     */
    public List<ContractPriceIndexHis> getCalcHisPriceFromRedis(List<ExchangeIndex> exchangeIndexList) {
        List<ContractPriceIndexHis> calcPriceIndexHisList = Lists.newArrayList();
        for (ExchangeIndex index : exchangeIndexList) {
            ContractPriceIndexHis his = null;
            int fromSource = getIndexPriceHisFromSource(index);
            if (fromSource == CalcQualified.HTTP.value()) {
                his = redisService.getHttpHisIndexPrice(index.getShortName(), index.getIndexSymbol());
                if (his == null) {
                    his = contractPriceIndexHisMapper.getLastContractPriceIndexHis(index.getExchangeId(), index.getIndexSymbol(), Origin.HTTP.value());
                }
            } else if (fromSource == CalcQualified.WEBSOCKET.value()) {
                his = redisService.getWsHisIndexPrice(index.getShortName(), index.getIndexSymbol());
                if (his == null) {
                    his = contractPriceIndexHisMapper.getLastContractPriceIndexHis(index.getExchangeId(), index.getIndexSymbol(), Origin.WEBSOCKET.value());
                }
            } else {
                continue;
            }
            if(his!=null){
                calcPriceIndexHisList.add(his);
            }
        }
        return calcPriceIndexHisList;
    }

    /**
     * 从redis中取出某一币对所有交易所的最新价格信息
     *
     * @param exchangeIndexList 某一币种对应的交易所等信息
     * @return List<ContractPriceIndexHis> 币种对应的每一个交易所的价格信息
     */
    public List<ContractPriceIndexHis> getAllHisPriceFromRedis(List<ExchangeIndex> exchangeIndexList) {
        List<ContractPriceIndexHis> allPriceIndexHisList = Lists.newArrayList();
        for (ExchangeIndex index : exchangeIndexList) {
            ContractPriceIndexHis his = null;
            int fromSource = getIndexPriceHisFromSource(index);
            if (fromSource == CalcQualified.HTTP.value() || fromSource == CalcQualified.INVALID.value()) {
                his = redisService.getHttpHisIndexPrice(index.getShortName(), index.getIndexSymbol());
                if (his == null) {
                    his = contractPriceIndexHisMapper.getLastContractPriceIndexHis(index.getExchangeId(), index.getIndexSymbol(), Origin.HTTP.value());
                }
            } else if (fromSource == CalcQualified.WEBSOCKET.value()) {
                his = redisService.getWsHisIndexPrice(index.getShortName(), index.getIndexSymbol());
                if (his == null) {
                    his = contractPriceIndexHisMapper.getLastContractPriceIndexHis(index.getExchangeId(), index.getIndexSymbol(), Origin.WEBSOCKET.value());
                }
            }
            allPriceIndexHisList.add(his);
        }
        return allPriceIndexHisList;
    }

    /**
     * 判断从哪获取历史数据
     *
     * @param exchangeIndex
     * @return
     */
    private int getIndexPriceHisFromSource(ExchangeIndex exchangeIndex) {
        //判断当前交易所是否走代理
        ExchangeGrabConf conf = exchangeGrabConfMapper.getExchangeGrabConfByExchangeId(exchangeIndex.getExchangeId());
        if (conf == null) {
            //不走代理的情况
            if (exchangeIndex.getHttpQualified() == ValidEnum.SUCC.getStatus()) {
                return CalcQualified.HTTP.value();
            }
            if (exchangeIndex.getWsQualified() == ValidEnum.SUCC.getStatus()) {
                return CalcQualified.WEBSOCKET.value();
            }
            return CalcQualified.INVALID.value();
        } else {
            long ipPoolQueuelength = redisService.getDQueueLength(exchangeIndex.getShortName());
            if (exchangeIndex.getHttpQualified() == ValidEnum.SUCC.getStatus() && ipPoolQueuelength != 0) {
                return CalcQualified.HTTP.value();
            }
            if (exchangeIndex.getWsQualified() == ValidEnum.SUCC.getStatus()) {
                return CalcQualified.WEBSOCKET.value();
            }
            if (exchangeIndex.getHttpQualified() == ValidEnum.SUCC.getStatus()) {
                return CalcQualified.HTTP.value();
            }
            return CalcQualified.INVALID.value();
        }
    }

    /**
     * 获取价格中位数
     *
     * @param contractPriceIndexHisList
     * @return
     */
    public BigDecimal getMiddlePrice(List<ContractPriceIndexHis> contractPriceIndexHisList) {
        List<BigDecimal> pricelist = contractPriceIndexHisList.stream()
                .map(contractPriceIndexHis -> contractPriceIndexHis.getTargetPrice())
                .collect(Collectors.toList());
        Collections.sort(pricelist);
        if (pricelist.size() % 2 == 0) {
            //偶数个
            return (pricelist.get(pricelist.size() / 2 - 1)).add(pricelist.get(pricelist.size() / 2)).divide(new BigDecimal(2), 4, BigDecimal.ROUND_DOWN);
        } else {
            return pricelist.get(pricelist.size() / 2);
        }
    }

    /**
     * 对每个交易所获取到的价格进行处理：
     * 1.当交易所的价格与中位数偏差±10%时，设定交易所的价格为 中位数 *（1+10%）或  中位数 *（1-10%）作为当前交易所的正常价格
     *
     * @param contractPriceIndexHisList 原始币对在交易所的价格信息
     * @param middlePrice               中位数
     * @return List<ContractPriceIndexHis> 新的币对价格信息
     */
    public List<ContractPriceIndexHis> handleContractPrice(List<ContractPriceIndexHis> contractPriceIndexHisList, BigDecimal middlePrice) {
        BigDecimal andMedianOffset = isMedianDeviation;
        List<ContractPriceIndexHis> newContractPriceIndexHisList = new ArrayList<ContractPriceIndexHis>();
        for (ContractPriceIndexHis cih : contractPriceIndexHisList) {
            //价格与中位数的偏差
            BigDecimal deviatedIndex = (cih.getTargetPrice().subtract(middlePrice)).divide(middlePrice, Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
            if (BigDecimalUtil.moreThan(deviatedIndex.abs(), andMedianOffset)) {
                //偏差指数在=<10%以上
                if (BigDecimalUtil.lessThanOrEquals(cih.getTargetPrice(), middlePrice)) {
                    //价格<中位数
                    BigDecimal newPrice = middlePrice.multiply(BigDecimal.ONE.subtract(andMedianOffset));
                    cih.setTargetPrice(newPrice);
                } else if (BigDecimalUtil.moreThan(cih.getTargetPrice(), middlePrice)) {
                    //价格>中位数
                    BigDecimal newPrice = middlePrice.multiply(BigDecimal.ONE.add(andMedianOffset));
                    cih.setTargetPrice(newPrice);
                }
            }
            newContractPriceIndexHisList.add(cih);
        }
        return newContractPriceIndexHisList;
    }

    /**
     * 处理交易所数量大于2家的情况  （插入指数计算记录表，插入合约价格表）
     *
     * @param newContractPriceIndexHisList 参与计算的交易所 并与中位数比对计算后新的指数历史价格信息
     * @param allContractPriceIndexHisList 该币对所有的交易所历史价格信息
     * @param symbol                       币对
     * @param middlePrice                  中位数
     * @param isValidExchangeIndexList     币对下所有的交易所信息
     */
    @Transactional
    public void handleMoreThanTwoExchange(List<ContractPriceIndexHis> newContractPriceIndexHisList,
                                          List<ContractPriceIndexHis> allContractPriceIndexHisList,
                                          String symbol, BigDecimal middlePrice, List<ExchangeIndex> isValidExchangeIndexList) {
        //权重变更
        byte isWeightChange = getWeightWhetherChange(newContractPriceIndexHisList, isValidExchangeIndexList);
        //TODO
        handleSetExchangeNewWeight(newContractPriceIndexHisList, isValidExchangeIndexList);

        //获取加权平均价格
        BigDecimal indexPrice = getWeightedAvg(newContractPriceIndexHisList, isValidExchangeIndexList);
        //插入到合约指数价格表
        Long contractIndexId = insertContractPriceIndex(indexPrice, symbol, isWeightChange);
        //分装caclRecord对象
        List<ContractPriceIndexCalcRecord> contractPriceIndexCalcRecordList = handlePackContractPriceIndexCalcRecord(contractIndexId, allContractPriceIndexHisList, isValidExchangeIndexList);
        //插入指数计算记录表
        insertContractCalRecord(contractPriceIndexCalcRecordList);
        //插入中位数记录
        insertMedian(middlePrice, contractIndexId, symbol);
    }

    /**
     * 处理两家交易所的情况
     * 1.两家价格偏差<=25% 使用handleMoreThanTwoExchange处理
     * 2.两家价格偏差>25% 取与最新指数价格偏差小的一方使用
     *
     * @param isGrabContractPriceIndexHisList 参与计算的交易所指数历史价格信息
     * @param allContractPriceIndexHisList    该币对所有的交易所历史价格信息
     * @param symbol                          交易币对
     * @param allExchangeIndexList            币对下所有的交易所信息
     */
    @Transactional
    public void handleTwoExchange(List<ContractPriceIndexHis> isGrabContractPriceIndexHisList, List<ContractPriceIndexHis> allContractPriceIndexHisList, String symbol, List<ExchangeIndex> allExchangeIndexList) {
        ContractPriceIndexHis cpi1 = isGrabContractPriceIndexHisList.get(0);
        ContractPriceIndexHis cpi2 = isGrabContractPriceIndexHisList.get(1);
        if (cpi1 == null || cpi2 == null) {
            LOG.error("[" + symbol + "] 交易所价格信息为空");
            return;
        }
        //取出币对最新的价格信息
        ContractPriceIndex contractPriceIndex = getLastContractIndex(symbol);
        //判断哪家的价格低
        ContractPriceIndexHis min = BigDecimalUtil.lessThan(cpi1.getTargetPrice(), cpi2.getTargetPrice()) ? cpi1 : cpi2;
        //计算两家价格偏差
        BigDecimal offset = cpi1.getTargetPrice().subtract(cpi2.getTargetPrice()).abs().divide(min.getTargetPrice(), Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
        //两家交易所价格偏差小于等于25%
        if (BigDecimalUtil.lessThanOrEquals(offset, twoExchangeDeviation) || contractPriceIndex == null) {
            //两家交易所取平均值作为中位数
            BigDecimal indexPrice = cpi1.getTargetPrice().add(cpi2.getTargetPrice()).divide(new BigDecimal(2), Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
            handleMoreThanTwoExchange(isGrabContractPriceIndexHisList, allContractPriceIndexHisList, symbol, indexPrice, allExchangeIndexList);
        } else {
            //两家交易所价格偏差>25%,取与最新价格偏差较小的作为最新价格
            //取得最新价格
            BigDecimal lastIndexPrice = contractPriceIndex.getIndexPrice();
            BigDecimal offset1 = cpi1.getTargetPrice().subtract(lastIndexPrice);
            BigDecimal offset2 = cpi2.getTargetPrice().subtract(lastIndexPrice);
            ContractPriceIndexHis lastCp = cpi1; //设置采用哪一个价格
            //取与最新价格偏差小的作为指数价格
            if (BigDecimalUtil.lessThan(offset1.abs(), offset2.abs())) {
                lastCp = cpi1;//取cpi1
            } else {
                lastCp = cpi2;//取cpi2
            }
            List<ContractPriceIndexHis> newContractPriceIndexHisList = Lists.newArrayList();
            newContractPriceIndexHisList.add(lastCp);
            byte isWeightChange = getWeightWhetherChange(newContractPriceIndexHisList, allExchangeIndexList);
            //插入合约价格指数表
            Long contractPriceIndexId = insertContractPriceIndex(lastCp.getTargetPrice(), symbol, isWeightChange);
            //设置 allExchangeIndexList 中ExchangeIndex对象新的权重值
            handleSetExchangeNewWeight(newContractPriceIndexHisList, allExchangeIndexList);
            List<ContractPriceIndexCalcRecord> contractPriceIndexCalcRecordList = handlePackContractPriceIndexCalcRecord(contractPriceIndexId,
                    allContractPriceIndexHisList, allExchangeIndexList);
            //插入计算记录表
            insertContractCalRecord(contractPriceIndexCalcRecordList);
            //插入中位数记录到指数计算记录表中
            insertMedian(lastCp.getTargetPrice(), contractPriceIndexId, symbol);
        }
    }

    /**
     * 处理只有一家交易所的情况
     * 1.判断交易所价格与最新指数价格偏差
     * 2.偏差>=25%使用最新指数价格偏差
     * 3.偏差<25% 使用本次价格
     *
     * @param isGrabContractPriceIndexHisList 参与计算的交易所指数历史价格信息
     * @param allContractPriceIndexHisList    该币对所有的交易所历史价格信息
     * @param symbol                          币对
     * @param allExchangeIndexList            币对下所有的交易所信息
     */
    @Transactional
    public void handleOneExchange(List<ContractPriceIndexHis> isGrabContractPriceIndexHisList, List<ContractPriceIndexHis> allContractPriceIndexHisList, String symbol, List<ExchangeIndex> allExchangeIndexList) {
        //获取最新的价格信息
        ContractPriceIndex contractPriceIndex = getLastContractIndex(symbol);
        ContractPriceIndexHis contractPriceIndexHis = isGrabContractPriceIndexHisList.get(0);
        if (contractPriceIndexHis == null) {
            LOG.error("[" + symbol + "] 交易所价格为空");
            return;
        }
        BigDecimal currPrice = contractPriceIndexHis.getTargetPrice();
        if (contractPriceIndex != null) {
            //最新价格
            BigDecimal indexPrice = contractPriceIndex.getIndexPrice();
            BigDecimal offset = (contractPriceIndexHis.getTargetPrice().subtract(indexPrice).abs()).divide(indexPrice, Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
            //交易所价格与最新的价格偏差>=25% 使用 上次的价格//
            if (BigDecimalUtil.moreThan(offset, isLatestIndexPriceDeviation)) {
                currPrice = indexPrice;
            }
        }
        byte isWeightChange = getWeightWhetherChange(isGrabContractPriceIndexHisList, allExchangeIndexList);
        //插入合约价格指数表
        Long contractPriceIndexId = insertContractPriceIndex(currPrice, symbol, isWeightChange);
        //设置 allExchangeIndexList 中ExchangeIndex对象新的权重值
        handleSetExchangeNewWeight(isGrabContractPriceIndexHisList, allExchangeIndexList);
        //封装一个ContractPriceIndexCalcRecord集合
        List<ContractPriceIndexCalcRecord> contractPriceIndexCalcRecordList = handlePackContractPriceIndexCalcRecord(contractPriceIndexId,
                allContractPriceIndexHisList, allExchangeIndexList);
        //插入指数记录表
        insertContractCalRecord(contractPriceIndexCalcRecordList);
        //插入中位数
        insertMedian(currPrice, contractPriceIndexId, symbol);
    }

    /**
     * 插入中位数记录
     *
     * @param midPrice
     */
    public void insertMedian(BigDecimal midPrice, Long priceIndexId, String indexSymbol) {
        Date date = new Date();
        ExchangeInfo exchangeInfo = exchangeInfoMapper.getExchangeByShortName(Constant.MEDIAN_SHORT_NAME);
        ContractPriceIndexCalcRecord record = new ContractPriceIndexCalcRecord();
        record.setExchangeId(exchangeInfo.getExchangeId());
        record.setGrabTime(date);
        record.setTargetSymbol(indexSymbol);
        record.setTargetPrice(midPrice);
        record.setWeight(BigDecimal.ZERO);
        record.setOriginalWeight(BigDecimal.ZERO);
        record.setGrabTime(date);
        record.setInputTime(date);
        record.setPriceIndexId(priceIndexId);
        contractPriceIndexCalcRecordMapper.insertSelective(record);
    }

    /**
     * 插入指数计算记录表
     *
     * @param contractPriceIndexCalcRecordList
     */
    public void insertContractCalRecord(List<ContractPriceIndexCalcRecord> contractPriceIndexCalcRecordList) {
        for (ContractPriceIndexCalcRecord record : contractPriceIndexCalcRecordList) {
            contractPriceIndexCalcRecordMapper.insertSelective(record);
        }
    }

    /**
     * 插入合约指数价格表
     *
     * @param indexPrice     价格
     * @param indexSymbol    币对
     * @param isWeightChange 权重是否变更
     * @return
     */
    public Long insertContractPriceIndex(BigDecimal indexPrice, String indexSymbol, byte isWeightChange) {
        Date date = new Date();
        ContractPriceIndex contractPriceIndex = new ContractPriceIndex();
        contractPriceIndex.setIndexPrice(indexPrice);
        contractPriceIndex.setIsWeightChange(isWeightChange);
        contractPriceIndex.setIndexSymbol(indexSymbol);
        contractPriceIndex.setInputTime(date);
        contractPriceIndex.setLastTime(date);
        //插入数据库
        contractPriceIndexMapper.insertSelective(contractPriceIndex);
        //插入redis最新价格
        redisService.updateLatestIndexPrice(indexSymbol, contractPriceIndex);
        //插入redis 最新汇率
        redisService.updateIndexRate(indexSymbol, indexPrice);
        return contractPriceIndex.getId();
    }

    /**
     * 获取加权平均值
     *
     * @return
     */
    public BigDecimal getWeightedAvg(List<ContractPriceIndexHis> contractPriceIndexHisList, List<ExchangeIndex> exchangeIndexList) {
        BigDecimal weightedSum = BigDecimal.ZERO;
        BigDecimal currWeightPriceSum = BigDecimal.ZERO;

        for (ContractPriceIndexHis his : contractPriceIndexHisList) {
            for (ExchangeIndex ei : exchangeIndexList) {
                if (his.getExchangeId().equals(ei.getExchangeId())) {
                    weightedSum = weightedSum.add(ei.getWeight());
                    BigDecimal currWeightPrice = his.getTargetPrice().multiply(ei.getWeight());
                    currWeightPriceSum = currWeightPriceSum.add(currWeightPrice);
                }
            }
        }
        return currWeightPriceSum.divide(weightedSum, Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
    }

    /**
     * 判断权重是否更改
     *
     * @param isGrabContractPriceIndexHisList 参与计算的历史价格信息
     * @param allExchangeIndexList            该币对所有的交易所信息
     * @return
     */
    private byte getWeightWhetherChange(List<ContractPriceIndexHis> isGrabContractPriceIndexHisList, List<ExchangeIndex> allExchangeIndexList) {
        byte isWeightChange = WEIGHT_IS_NOT_CHANGE;
        if (isGrabContractPriceIndexHisList.size() != allExchangeIndexList.size()) {
            isWeightChange = WEIGHT_IS_CHANGE;
        }
        return isWeightChange;
    }

    /**
     * 设置交易所的新权重
     *
     * @param newContractPriceIndexHisList 参与计算的指数历史价格信息
     * @param exchangeIndexList
     */
    private void handleSetExchangeNewWeight(List<ContractPriceIndexHis> newContractPriceIndexHisList, final List<ExchangeIndex> exchangeIndexList) {
        List<Long> exchangeIdList = newContractPriceIndexHisList.stream().map(his -> his.getExchangeId()).collect(Collectors.toList());
        //计算有效交易所的权重总值
        BigDecimal newTotalWeight = exchangeIndexList.stream().filter(ei -> exchangeIdList.contains(ei.getExchangeId())).map(ei -> ei.getOriginalWeight()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //根据各家交易所的权重占比，计算新的权重值
        for (ExchangeIndex ei : exchangeIndexList) {
            if (exchangeIdList.contains(ei.getExchangeId())) {
                BigDecimal wieght = ei.getOriginalWeight().divide(newTotalWeight, 6, BigDecimal.ROUND_DOWN).multiply(WEIGHT_TOTAL).setScale(4, BigDecimal.ROUND_DOWN);
                ei.setWeight(wieght);
            } else {
                ei.setWeight(BigDecimal.ZERO);
            }
        }
    }

    /**
     * 将币对所有的历史价格分装为ContractPriceIndexCalcRecord对象
     *
     * @param allContractPriceIndexHisList
     * @param exchangeIndexList
     */
    private List<ContractPriceIndexCalcRecord> handlePackContractPriceIndexCalcRecord(Long contractPriceIndexId, List<ContractPriceIndexHis> allContractPriceIndexHisList, List<ExchangeIndex> exchangeIndexList) {
        List<ContractPriceIndexCalcRecord> contractPriceIndexCalcRecordList = Lists.newArrayList();
        ContractPriceIndexCalcRecord record = null;
        Date date = new Date();
        for (ExchangeIndex ei : exchangeIndexList) {
            record = new ContractPriceIndexCalcRecord();
            record.setTargetSymbol(ei.getIndexSymbol());
            record.setExchangeFullName(ei.getFullName());
            record.setPriceIndexId(contractPriceIndexId);
            record.setExchangeId(ei.getExchangeId());
            record.setExchangeShortName(ei.getShortName());
            //原始权重
            record.setOriginalWeight(ei.getOriginalWeight());
            record.setTargetPrice(BigDecimal.ZERO);
            //新权重
            record.setWeight(ei.getWeight());
            record.setGrabTime(date);
            for (ContractPriceIndexHis his : allContractPriceIndexHisList) {
                if (ei.getExchangeId().equals(his.getExchangeId())) {
                    record.setTargetPrice(his.getTargetPrice());
                    record.setGrabTime(his.getTradeTime());
                    record.setContractPriceIndexHisId(his.getId());
                }
            }
            record.setInputTime(date);
            contractPriceIndexCalcRecordList.add(record);
        }
        return contractPriceIndexCalcRecordList;
    }
}
