package com.huobi.quantification.strategy;


import com.google.common.base.Throwables;
import com.huobi.quantification.api.email.EmailService;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dao.StrategyInstanceConfigMapper;
import com.huobi.quantification.dao.StrategyInstanceHistoryMapper;
import com.huobi.quantification.dto.SimpleMailReqDto;
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
    @Autowired
    private EmailService emailService;

    private AtomicBoolean instanceEnable = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            Thread thread = new Thread(() -> {
                while (true) {
                    StrategyInstanceConfig config = null;
                    try {
                        config = instanceConfiger.getInstanceConfig();
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
                        String subject = "策略实例警报-严重-实例异常退出";
                        String text;
                        if (instanceEnable.get()) {
                            text = String.format("策略实例启动异常，instanceConfigId：%s，contractCode：%s，异常原因：%s", config.getId(), config.getFutureContractCode(), Throwables.getStackTraceAsString(e));
                        } else {
                            text = String.format("策略实例停止异常，instanceConfigId：%s，contractCode：%s，异常原因：%s", config.getId(), config.getFutureContractCode(), Throwables.getStackTraceAsString(e));
                        }
                        errorMail(subject, text);
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


    private void errorMail(String subject, String text) {
        SimpleMailReqDto reqDto = new SimpleMailReqDto();
        reqDto.setSubject(subject);
        reqDto.setText(text);
        emailService.sendSimpleMail(reqDto);
    }

}
