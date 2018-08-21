package com.huobi.quantification;import com.google.common.base.Stopwatch;import org.mybatis.spring.annotation.MapperScan;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.boot.SpringApplication;import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.context.annotation.ImportResource;import org.springframework.transaction.annotation.EnableTransactionManagement;@EnableTransactionManagement@SpringBootApplication@MapperScan("com.huobi.quantification.dao")@ImportResource(locations = {"classpath:spring/spring-application.xml"})public class StrategyApplication {    private static final Logger logger = LoggerFactory.getLogger(StrategyApplication.class);    public static void main(String[] args) {        Stopwatch stopwatch = Stopwatch.createStarted();        logger.info("开始启动服务~~~~~~~~~~~~~~~~");        SpringApplication.run(StrategyApplication.class, args);        logger.info("服务启动完成,消耗时间：{}", stopwatch);        synchronized (StrategyApplication.class) {            try {                StrategyApplication.class.wait();            } catch (InterruptedException e) {                logger.error("", e);            }        }    }}