package com.huobi.contract.index.taskjob.alert.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.huobi.contract.index.contract.index.service.IndexMesssageAlertService;

/**
 * 
 * @author beckli
 *
 */
@Service("emailSendJob")
public class EmailSendJob extends AbstractSimpleElasticJob {
	private static final Logger LOG = LoggerFactory.getLogger(EmailSendJob.class);
	@Autowired
	private IndexMesssageAlertService indexMesssageAlertService;
	
	@Transactional
	@Override
	public void process(JobExecutionMultipleShardingContext arg0) {
		LOG.info("EmailSendJob任务开始");
		indexMesssageAlertService.emailTask();
		LOG.info("EmailSendJob任务结束");
	}

}

