package com.huobi.contract.index.taskjob;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.huobi.contract.index.api.LastestTickerNotifyService;
import com.huobi.contract.index.dao.IndexInfoMapper;
import com.huobi.contract.index.entity.IndexInfo;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.huobi.contract.index.common.mail.EmailSend;
import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.okcoin.FutureClientFacade;

@Service("lastestTickerNotifyJob")
public class LastestTickerNotifyJob extends AbstractSimpleElasticJob {
    private static final Logger LOG = LoggerFactory.getLogger(LastestTickerNotifyJob.class);
    @Autowired
    @Qualifier("lastestTickerNotifyService")
    private LastestTickerNotifyService lastestTickerNotifyService;

    @Transactional
    @Override
    public void process(JobExecutionMultipleShardingContext arg0) {
        LOG.info("OKEx币币行情任务开始");
        lastestTickerNotifyService.grab();
        LOG.info("OKEx币币行情任务结束");
    }
}
