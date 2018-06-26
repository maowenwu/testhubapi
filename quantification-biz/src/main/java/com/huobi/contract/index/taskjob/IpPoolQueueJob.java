package com.huobi.contract.index.taskjob;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.huobi.contract.index.contract.index.service.IpPoolQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @desc
 * @Author mingjianyong
 */
@Service("ipPoolQueueJob")
public class IpPoolQueueJob extends AbstractSimpleElasticJob {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeHttpQualifiedGrapCalcJob.class);

    @Autowired
    private IpPoolQueue ipPoolQueue;

    @Override
    public void process(JobExecutionMultipleShardingContext shardingContext) {
        LOG.info("IP队列计算任务开始");
        ipPoolQueue.holdIpPoolQueue();
        LOG.info("IP队列计算任务结束");
    }
}
