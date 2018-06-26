package com.huobi.contract.index.monitor.impl;

import com.google.common.collect.Maps;
import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.dao.ContractPriceIndexHisMapper;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dto.ContractPriceIndexHisStatusCount;
import com.huobi.contract.index.entity.ExchangeInfo;
import com.huobi.contract.index.monitor.common.MonitorConfig;
import com.huobi.contract.index.monitor.service.IndexMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @desc 	单交易所某交易对价格长时间无法获取
 * @Author mingjianyong
 */
public class IndexHisPriceLongTimeNotGetMonitor extends BaseIndexMonitorService implements IndexMonitorService {

    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;
    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;
    @Value("${index.his.price.not.get.time.hour}")
    private Integer indexHisPriceNotGetTimeHour;
    @Value("${index.his.price.get.succ.scale}")
    private BigDecimal indexHisPriceGetSuccScale;
    @Value("${index.his.price.get.level}")
    private String indexHisPriceGetLevel;


    private final static String EXCHANGE = "exchange";
    private final static String TRANSACTIONPAIR = "transactionPair";
    private final static String PERCENTAGE = "percentage";

    @Override
    public void monitor() {
        String startTime = DateUtil.parseDateToStr(DateUtil.getSpecifiedDayBefore(indexHisPriceNotGetTimeHour),
                DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        String endTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        List<ContractPriceIndexHisStatusCount> indexHisStatusCounts = contractPriceIndexHisMapper.listIndexHisStatusCount(startTime, endTime);
        handleMonitor(indexHisStatusCounts);

    }
    protected void handleMonitor(List<ContractPriceIndexHisStatusCount> indexHisStatusCounts){
        Map<String,Object> model = Maps.newHashMap();
        for(ContractPriceIndexHisStatusCount hisStatus : indexHisStatusCounts){
            if(hisStatus.getSuccCount()== null && hisStatus.getFailCount()== null){
                //交易所-币对没有抓取到数据，报警
                ExchangeInfo exchangeInfo = exchangeInfoMapper.selectByPrimaryKey(hisStatus.getExchangeId());
                model.put(EXCHANGE,exchangeInfo.getShortName());
                model.put(TRANSACTIONPAIR,hisStatus.getTargetSymbol());
                model.put(PERCENTAGE,indexHisPriceGetSuccScale);
                handleAlarmByLevel(model);
            }else{
                long succCount = hisStatus.getSuccCount();
                long failCount = hisStatus.getFailCount();
                BigDecimal succScale = new BigDecimal(succCount).divide(new BigDecimal(succCount+failCount),
                        Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
                if(BigDecimalUtil.lessThanOrEquals(succScale, indexHisPriceGetSuccScale)){
                    //报警
                    ExchangeInfo exchangeInfo = exchangeInfoMapper.selectByPrimaryKey(hisStatus.getExchangeId());
                    model.put(EXCHANGE,exchangeInfo.getShortName());
                    model.put(TRANSACTIONPAIR,hisStatus.getTargetSymbol());
                    model.put(PERCENTAGE,indexHisPriceGetSuccScale);
                    handleAlarmByLevel(model);
                }
            }
        }
    }
    @Override
    protected String getSubject() {
        return MonitorConfig.indexHisPriceLongTimeNotGetMonitorSubject;
    }

    @Override
    protected String getTemplateContent() {
        return MonitorConfig.indexHisPriceLongTimeNotGetMonitor;
    }

    @Override
    protected List<String> getAlarmLevel() {
        return Arrays.asList(indexHisPriceGetLevel.split(","));
    }
}
