package com.huobi.contract.index.taskjob;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.huobi.contract.index.api.ExchangeIsQualifiedGrabCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("exchangeWebsocketQualifiedCalcJob")
public class ExchangeWebsocketQualifiedCalcJob extends AbstractSimpleElasticJob {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeWebsocketQualifiedCalcJob.class);

    @Autowired
    @Qualifier("exchangeIsGrapCalcService")
    private ExchangeIsQualifiedGrabCalcService exchangeIsQualifiedGrabCalcService;

    @Override
    public void process(JobExecutionMultipleShardingContext shardingContext) {
        LOG.info("交易所-币对websocket可用状态计算任务开始");
        exchangeIsQualifiedGrabCalcService.wsQualifiedCalc();
        LOG.info("交易所-币对websocket可用状态计算任务结束");
    }
}
