package com.huobi.contract.index.taskjob;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.huobi.contract.index.api.ExchangeRateCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("exchangeRateCalcJob")
public class ExchangeRateCalcJob extends AbstractSimpleElasticJob {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeHttpQualifiedGrapCalcJob.class);

    @Autowired
    @Qualifier("exchangeRateCalcService")
    private ExchangeRateCalcService exchangeRateCalcService;

    @Override
    public void process(JobExecutionMultipleShardingContext shardingContext) {
        LOG.info("汇率计算任务开始");
        exchangeRateCalcService.calc();
        LOG.info("汇率计算任务结束");
    }
}
