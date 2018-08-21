package com.huobi.quantification.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.useCluster}")
    private Boolean useCluster;


    @Bean
    public RedissonClient redissonClient() {
        logger.info("redis连接字符串：redis://{}:{}，是否启用集群：", host, port, useCluster);
        Config config = new Config();
        if (useCluster) {
            config.useClusterServers()
                    // 集群状态扫描间隔时间，单位是毫秒
                    .setScanInterval(2000)
                    //可以用"rediss://"来启用SSL连接
                    .addNodeAddress(String.format("redis://%s:%s", host, port));
        } else {
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setAddress(String.format("redis://%s:%s", host, port));
            if (StringUtils.isNotEmpty(password)) {
                singleServerConfig.setPassword(password);
            }
        }
        RedissonClient redissonClient = Redisson.create(config);
        if (redissonClient == null) {
            throw new RuntimeException("redisson 连接redis失败");
        }
        return redissonClient;
    }

}
