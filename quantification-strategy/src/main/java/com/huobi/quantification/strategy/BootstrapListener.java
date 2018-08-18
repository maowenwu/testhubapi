package com.huobi.quantification.strategy;


import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dao.StrategyInstanceConfigMapper;
import com.huobi.quantification.dao.StrategyInstanceHistoryMapper;
import com.huobi.quantification.entity.StrategyInstanceConfig;
import com.huobi.quantification.entity.StrategyInstanceHistory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class BootstrapListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StrategyBootstrap bootstrap;
    @Autowired
    private StrategyInstanceConfigMapper instanceConfigMapper;
    @Autowired
    private StrategyInstanceHistoryMapper instanceHistoryMapper;

    private AtomicBoolean instanceEnable = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        String id = System.getProperty("instanceConfigId");
                        if (StringUtils.isEmpty(id)) {
                            logger.error("未指定系统属性[instanceConfigId]，系统退出");
                            System.exit(1);
                        }
                        StrategyInstanceConfig config = instanceConfigMapper.selectByPrimaryKey(Integer.valueOf(id));
                        if (config == null) {
                            logger.error("未找到指定配置instanceConfigId={}，系统退出", id);
                            System.exit(1);
                        }
                        if (config.getInstanceEnable().equals(1) && !instanceEnable.get()) {
                            // 启动
                            instanceEnable.set(true);
                            bootstrap.startInstance(config);
                            saveInstanceStatus(config);
                        } else if (config.getInstanceEnable().equals(0) && instanceEnable.get()) {
                            // 停止
                            instanceEnable.set(false);
                            bootstrap.stopInstance();
                            updateInstanceStatus(config);
                        }
                        ThreadUtils.sleep(1000);
                    } catch (Throwable e) {
                        logger.error("实例控制线程异常，系统退出", e);
                        System.exit(1);
                    }
                }
            });
            thread.setDaemon(true);
            thread.setName("Instance-Lifecycle线程");
            thread.start();
        }
    }

    private void saveInstanceStatus(StrategyInstanceConfig config) {
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

    private void updateInstanceStatus(StrategyInstanceConfig config) {
        StrategyInstanceHistory history = new StrategyInstanceHistory();
        history.setInstanceId(config.getInstanceId());
        Date nowDate = new Date();
        history.setInstanceStopTime(new Date());
        history.setUpdateTime(nowDate);
        instanceHistoryMapper.updateByInstanceId(history);
    }

}
