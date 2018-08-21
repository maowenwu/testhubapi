package com.huobi.quantification.strategy;


import com.huobi.quantification.dao.StrategyInstanceConfigMapper;
import com.huobi.quantification.dao.StrategyInstanceHistoryMapper;
import com.huobi.quantification.entity.StrategyInstanceConfig;
import com.huobi.quantification.entity.StrategyInstanceHistory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class InstanceConfiger {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StrategyInstanceConfigMapper instanceConfigMapper;
    @Autowired
    private StrategyInstanceHistoryMapper instanceHistoryMapper;

    private Integer configId;

    public InstanceConfiger() {
        String configId = System.getProperty("instanceConfigId");
        if (StringUtils.isEmpty(configId)) {
            throw new RuntimeException("未指定系统属性[instanceConfigId]，系统退出");
        }
        this.configId = Integer.valueOf(configId);
    }


    public StrategyInstanceConfig getInstanceConfig() {
        StrategyInstanceConfig config = instanceConfigMapper.selectByPrimaryKey(Integer.valueOf(configId));
        if (config == null) {
            throw new RuntimeException(String.format("未找到指定配置instanceConfigId=%s，系统退出", configId));
        }
        return config;
    }

    public void saveInstanceStatus(StrategyInstanceConfig config) {
        StrategyInstanceHistory history = new StrategyInstanceHistory();
        history.setStrategyName(config.getStrategyName());
        history.setInstanceConfigId(config.getId());
        history.setInstanceId(config.getInstanceId());
        history.setBaseCoin(config.getFutureBaseCoin());
        history.setContractCode(config.getFutureContractCode());
        Date nowDate = new Date();
        history.setInstanceStartupTime(nowDate);
        history.setCreateTime(nowDate);
        history.setUpdateTime(nowDate);
        instanceHistoryMapper.insert(history);
    }

    public void updateInstanceStatus(StrategyInstanceConfig config) {
        StrategyInstanceHistory history = new StrategyInstanceHistory();
        history.setInstanceId(config.getInstanceId());
        Date nowDate = new Date();
        history.setInstanceStopTime(new Date());
        history.setUpdateTime(nowDate);
        instanceHistoryMapper.updateByInstanceId(history);
    }

    public void updateInstanceHeartbeat() {
        StrategyInstanceConfig config = new StrategyInstanceConfig();
        config.setId(configId);
        config.setInstanceHeartbeat(new Date());
        instanceConfigMapper.updateByPrimaryKeySelective(config);
    }

    public boolean getOrderThreadEnable() {
        StrategyInstanceConfig config = instanceConfigMapper.selectByPrimaryKey(configId);
        return Integer.valueOf(1).equals(config.getOrderThreadEnable());
    }

    public void updateOrderThreadHeartbeat() {
        StrategyInstanceConfig config = new StrategyInstanceConfig();
        config.setId(configId);
        config.setOrderThreadHeartbeat(new Date());
        instanceConfigMapper.updateByPrimaryKeySelective(config);
    }

    public boolean getRiskThreadEnable() {
        StrategyInstanceConfig config = instanceConfigMapper.selectByPrimaryKey(configId);
        return Integer.valueOf(1).equals(config.getRiskThreadEnable());
    }

    public void updateRiskThreadHeartbeat() {
        StrategyInstanceConfig config = new StrategyInstanceConfig();
        config.setId(configId);
        config.setRiskThreadHeartbeat(new Date());
        instanceConfigMapper.updateByPrimaryKeySelective(config);
    }

    public boolean getHedgeThreadEnable() {
        StrategyInstanceConfig config = instanceConfigMapper.selectByPrimaryKey(configId);
        return Integer.valueOf(1).equals(config.getHedgeThreadEnable());
    }

    public void updateHedgeThreadHeartbeat() {
        StrategyInstanceConfig config = new StrategyInstanceConfig();
        config.setId(configId);
        config.setHedgeThreadHeartbeat(new Date());
        instanceConfigMapper.updateByPrimaryKeySelective(config);
    }
}
