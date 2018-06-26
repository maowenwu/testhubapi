package com.huobi.contract.index.monitor.impl;

import com.google.common.collect.Maps;
import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.monitor.common.MonitorConfig;
import com.huobi.contract.index.monitor.service.IndexMonitorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 指数价格，长时间未更新
 */
@Service("indexPriceMonitor")
public class IndexPriceMonitor extends BaseIndexMonitorService implements IndexMonitorService {

    @Value("${index.price.noupdate.time}")
    private long noUpdateTime;
    @Value("${index.price.noupdate.level}")
    private String alertLevel;
    @Value("${index.price.noupdate.time.unit}")
    private String unit;

    private final static  String VARIETIES = "varieties";
    private final static  String INTERVAL = "interval";

    @Override
    public void monitor() {
        //最新指数指数价格信息
        List<ContractPriceIndex> priceIndexList = listLastIndexPrice();
        handMonitor(priceIndexList);
    }
    public void handMonitor(List<ContractPriceIndex> priceIndexList){
        Date currDate =new Date();
        Map<String,Object> model = Maps.newHashMap();
        model.put(INTERVAL,noUpdateTime+unit);
        for(ContractPriceIndex index:priceIndexList){
            if(index == null){
                continue;
            }
            if(index.getInputTime()==null){
                model.put(VARIETIES, index.getIndexSymbol());
                handleAlarmByLevel(model);
                continue;
            }
            //计算指数最新的值得时间与当前时间相差的秒数
            long diffTime = (currDate.getTime() - index.getInputTime().getTime())/1000;
            if(diffTime>=noUpdateTime){
                model.put(VARIETIES, index.getIndexSymbol());
                handleAlarmByLevel(model);
            }
        }
    }

    @Override
    protected String getSubject() {
        return MonitorConfig.indexPriceMonitorSubject;
    }

    @Override
    protected String getTemplateContent() {
        return MonitorConfig.indexPriceMonitor;
    }

    @Override
    protected List<String> getAlarmLevel() {
        return Arrays.asList(alertLevel.split(","));
    }
}
