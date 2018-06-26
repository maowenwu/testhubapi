package com.huobi.contract.index.monitor.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.dao.ContractPriceIndexCalcRecordMapper;
import com.huobi.contract.index.dao.IndexInfoMapper;
import com.huobi.contract.index.dto.SymbolPriceDiffScaleAvg;
import com.huobi.contract.index.entity.IndexInfo;
import com.huobi.contract.index.monitor.common.MonitorConfig;
import com.huobi.contract.index.monitor.service.IndexMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

/**
 * @desc 单交易所某交易对价格长时间大幅偏离
 * @Author mingjianyong
 */
@Service("indexHisPriceLargeRangeOffMonitor")
public class IndexHisPriceLargeRangeOffMonitor extends BaseIndexMonitorService implements IndexMonitorService {
    @Autowired
    private ContractPriceIndexCalcRecordMapper contractPriceIndexCalcRecordMapper;
    @Autowired
    private IndexInfoMapper indexInfoMapper;
    @Value("${symbol.his.price.time.hour}")
    private int symbolHisPriceTimeHour;
    @Value("${symbol.his.price.level}")
    private String symbolHisPriceLevel;

    private final static String TRANSACTIONPAIR = "transactionPair";
    private final static String PERCENTAGE = "percentage";
    private final static String TIME = "time";

    @Override
    public void monitor() {
        String startTime = DateUtil.parseDateToStr(DateUtil.getSpecifiedHourBefore(symbolHisPriceTimeHour),
                DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        String endTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        List<SymbolPriceDiffScaleAvg> list = contractPriceIndexCalcRecordMapper.listSymbolMaxWithMinDeviationRateAvg(startTime, endTime);
        List<IndexInfo> indexInfoList = indexInfoMapper.listAvaidlIndexInfo();
        handleMonitor(list,indexInfoList);
    }
    void handleMonitor(List<SymbolPriceDiffScaleAvg> list,List<IndexInfo> indexInfoList){
        Map<String,Object> model = Maps.newHashMap();
        for(SymbolPriceDiffScaleAvg scale:list){
            if(CollectionUtils.isEmpty(indexInfoList)){
                return;
            }
            BigDecimal alarmThreshold =null;
            Optional<BigDecimal> optional = indexInfoList.stream().filter(index->index.getIndexSymbol().equalsIgnoreCase(scale.getSymbol()))
                    .map(index->index.getAlarmThreshold()).findFirst();
            if(optional.isPresent()){
                alarmThreshold = optional.get();
            }
            if(alarmThreshold!=null && BigDecimalUtil.moreThanOrEquals(scale.getScale(), alarmThreshold)){
                //报警
                model.put(TRANSACTIONPAIR,scale.getSymbol());
                model.put(PERCENTAGE,alarmThreshold);
                model.put(TIME,new Date());
                handleAlarmByLevel(model);
            }
        }
    }

    @Override
    protected String getSubject() {
        return MonitorConfig.indexHisPriceLargeRangeOffMonitorSubject;
    }

    @Override
    protected String getTemplateContent() {
        return MonitorConfig.indexHisPriceLargeRangeOffMonitor;
    }
    @Override
    protected List<String> getAlarmLevel() {
        return Arrays.asList(symbolHisPriceLevel.split(","));
    }
}
