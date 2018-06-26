package com.huobi.contract.index.monitor.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.dao.ExchangeRateHisMapper;
import com.huobi.contract.index.dto.ExchangeRateHisSymbol;
import com.huobi.contract.index.entity.ExchangeRateHis;
import com.huobi.contract.index.entity.ValidEnum;
import com.huobi.contract.index.monitor.common.MonitorConfig;
import com.huobi.contract.index.monitor.service.IndexMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @desc 汇率历史表长期缺失报警
 * @Author mingjianyong
 */
@Service("exchangeRateHisMonitor")
public class ExchangeRateHisMonitor extends BaseIndexMonitorService implements IndexMonitorService {
    @Value("${exchange.rate.his.noupdate.time}")
    private Integer exchangeRateHisNoUpdateTime;
    @Value("${exchange.rate.his.noupdate.unit}")
    private String exchangeRateHisNoUpdateUnit;
    @Value("${exchange.rate.his.noupdate.day.level}")
    private String exchangeRateHisNoUpdateLevel;
    @Autowired
    private ExchangeRateHisMapper exchangeRateHisMapper;

    private final static  String VARIETIES = "varieties";
    private static String INTERVAL = "interval";

    @Override
    public void monitor() {
        //获取前n天的时刻
        Date beforeDay = DateUtil.getSpecifiedDayBefore(exchangeRateHisNoUpdateTime);
        List<ExchangeRateHisSymbol>  exchangeRateHisSymbols = exchangeRateHisMapper.listExchangeRateHisGroupSymbol(
                DateUtil.parseDateToStr(beforeDay, DateUtil.DATE_FORMAT_YYYY_MM_DD));
        handleMonitor(exchangeRateHisSymbols);
    }

    /**
     * 处理告警逻辑
     * @param exchangeRateHisSymbols
     */
    protected void handleMonitor(List<ExchangeRateHisSymbol> exchangeRateHisSymbols){
        Map<String,Object> model = Maps.newHashMap();
        model.put(INTERVAL,exchangeRateHisNoUpdateTime+exchangeRateHisNoUpdateUnit);
        for(ExchangeRateHisSymbol symbol:exchangeRateHisSymbols){
            List<ExchangeRateHis> hisList = symbol.getList();
            if(CollectionUtils.isEmpty(hisList)){
                //汇率历史表无数据报警TODO
                model.put(VARIETIES, symbol.getSymbol());
                handleAlarmByLevel(model);
                continue;
            }
            long succCount = hisList.stream().filter(his->his.getStatus()== ValidEnum.SUCC.getStatus()).count();
            if(succCount==0){
                //连续失败告警TODO
                model.put(VARIETIES, symbol.getSymbol());
                handleAlarmByLevel(model);
            }

        }
    }

    @Override
    protected String getSubject() {
        return MonitorConfig.exchangeRateHisMonitorSubject;
    }

    @Override
    protected String getTemplateContent() {
        return MonitorConfig.exchangeRateHisMonitor;
    }

    @Override
    protected List<String> getAlarmLevel() {
        return Arrays.asList(exchangeRateHisNoUpdateLevel.split(","));
    }
}
