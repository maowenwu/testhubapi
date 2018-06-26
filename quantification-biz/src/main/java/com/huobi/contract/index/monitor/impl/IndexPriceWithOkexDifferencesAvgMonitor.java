package com.huobi.contract.index.monitor.impl;
import com.google.common.collect.Maps;
import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.dao.ContractPriceIndexMapper;
import com.huobi.contract.index.dto.SymbolPriceDiffScaleAvg;
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
 * @desc 每日指数价格，与Okex指数价格偏差过大
 * @Author mingjianyong
 */
public class IndexPriceWithOkexDifferencesAvgMonitor extends BaseIndexMonitorService implements IndexMonitorService {

    @Autowired
    private ContractPriceIndexMapper contractPriceIndexMapper;
    @Value("${index.price.with.okex.time.hour}")
    private Integer indexPriceWithOkexTimeHour;
    @Value("${index.price.with.okex.difference.scale.avg}")
    private BigDecimal indexPriceWithOkexDifferenceScaleAvg;
    @Value("${index.price.with.okex.difference.level}")
    private String indexPriceWithOkexDifferenceLevel;

    private final static String TRANSACTIONPAIR = "transactionPair";
    private final static String PERCENTAGE = "percentage";

    @Override
    public void monitor() {
         String startTime = DateUtil.parseDateToStr(DateUtil.getSpecifiedHourBefore(indexPriceWithOkexTimeHour),
                DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
         String endTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
         List<SymbolPriceDiffScaleAvg> diffScaleList = contractPriceIndexMapper.listIndexPriceWithOkexDiffScale(startTime,endTime);
         handleMonitor(diffScaleList);

    }
    public void handleMonitor(List<SymbolPriceDiffScaleAvg> diffScaleList){
        Map<String,Object> model = Maps.newHashMap();
        for(SymbolPriceDiffScaleAvg indexScale:diffScaleList){
            //indexScale.getSymbol();
            if(indexScale.getScale()==null || BigDecimalUtil.moreThanOrEquals(indexScale.getScale(),
                    indexPriceWithOkexDifferenceScaleAvg)){
                //报警
                model.put(TRANSACTIONPAIR,indexScale.getSymbol());
                model.put(PERCENTAGE,indexPriceWithOkexDifferenceScaleAvg);
                handleAlarmByLevel(model);
            }

        }
    }

    @Override
    protected String getSubject() {
        return MonitorConfig.indexPriceWithOkexDifferencesAvgMonitorSubject;
    }

    @Override
    protected String getTemplateContent() {
        return MonitorConfig.indexPriceWithOkexDifferencesAvgMonitor;
    }

    @Override
    protected List<String> getAlarmLevel() {
        return Arrays.asList(indexPriceWithOkexDifferenceLevel.split(","));
    }
}
