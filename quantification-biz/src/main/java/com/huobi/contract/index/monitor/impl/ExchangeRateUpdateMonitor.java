package com.huobi.contract.index.monitor.impl;

import com.google.common.collect.Maps;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.entity.ExchangeRate;
import com.huobi.contract.index.monitor.common.MonitorConfig;
import com.huobi.contract.index.monitor.service.IndexMonitorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @desc 每周五汇率更新失败报警
 * @Author mingjianyong
 */
@Service("exchangeRateUpdateMonitor")
public class ExchangeRateUpdateMonitor extends BaseIndexMonitorService implements IndexMonitorService {

    @Value("${exchange.rate.noupdate.day.level}")
    private String alertLevel;
    private final static  String VARIETIES = "varieties";
    private final static  String DATE = "date";

    @Override
    public void monitor() {
        List<ExchangeRate> exchangeRateList = listLastExchangeRate();
        Date previousFriday = DateUtil.getPreviousGFriday();
        Map<String,Object> model = Maps.newHashMap();
        model.put(DATE, DateUtil.parseDateToStr(previousFriday, DateUtil.DATE_FORMAT_YYYY_MM_DD));
        for(ExchangeRate rate:exchangeRateList){
            if(rate.getInputTime()==null){
                model.put(VARIETIES,rate.getExchangeSymbol());
                handleAlarmByLevel(model);
            }else{
                if(!DateUtil.parseDateToStr(rate.getInputTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD)
                        .equals(DateUtil.parseDateToStr(previousFriday, DateUtil.DATE_FORMAT_YYYY_MM_DD))){
                    model.put(VARIETIES,rate.getExchangeSymbol());
                    handleAlarmByLevel(model);
                }
            }
        }
    }

    @Override
    protected String getSubject() {
        return MonitorConfig.exchangeRateUpdateMonitorSubject;
    }

    @Override
    protected String getTemplateContent() {
        return MonitorConfig.exchangeRateUpdateMonitor;
    }

    @Override
    protected List<String> getAlarmLevel() {
        return Arrays.asList(alertLevel.split(","));
    }
}
