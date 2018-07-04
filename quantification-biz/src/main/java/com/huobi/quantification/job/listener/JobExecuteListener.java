package com.huobi.quantification.job.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import org.springframework.stereotype.Component;

@Component
public class JobExecuteListener implements ElasticJobListener {

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {

        System.out.println("==>" + this);
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {

    }
}
