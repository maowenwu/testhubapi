package com.huobi.contract.index.service;import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.boot.SpringApplication;import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.context.annotation.ComponentScan;@SpringBootApplication(exclude = MybatisAutoConfiguration.class)@ComponentScan("com.huobi.*")public class ServiceApplication {    private static final Logger LOG = LoggerFactory            .getLogger(ServiceApplication.class);    public static void main(String[] args) {        long statTime = System.currentTimeMillis();        LOG.info("开始启动服务~~~~~~~~~~~~~~~~");        SpringApplication.run(ServiceApplication.class, args);        long time = System.currentTimeMillis() - statTime;        LOG.info("服务启动完成,消耗时间：" + time + "毫秒");    }}