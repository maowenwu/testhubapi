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
    private InstanceConfiger instanceConfiger;

    private AtomicBoolean instanceEnable = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        StrategyInstanceConfig config = instanceConfiger.getInstanceConfig();
                        if (config.getInstanceEnable().equals(1) && !instanceEnable.get()) {
                            // 启动
                            instanceEnable.set(true);
                            bootstrap.startInstance(config);
                            instanceConfiger.saveInstanceStatus(config);
                        } else if (config.getInstanceEnable().equals(0) && instanceEnable.get()) {
                            // 停止
                            instanceEnable.set(false);
                            bootstrap.stopInstance();
                            instanceConfiger.updateInstanceStatus(config);
                        }
                        instanceConfiger.updateInstanceHeartbeat();
                        ThreadUtils.sleep(1000);
                    } catch (Throwable e) {
                        logger.error("实例控制线程异常，系统退出", e);
                        System.exit(1);
                    }
                }
            });
            thread.setDaemon(true);
            thread.setName("实例控制线程");
            thread.start();
            logger.info("实例控制线程启动");
        }
    }



}
