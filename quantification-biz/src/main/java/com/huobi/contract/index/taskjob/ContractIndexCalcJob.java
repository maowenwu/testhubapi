package com.huobi.contract.index.taskjob;

import com.huobi.contract.index.api.ContractIndexCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

@Service("contractIndexCalcJob")
public class ContractIndexCalcJob extends AbstractSimpleElasticJob {

	private static final Logger LOG = LoggerFactory.getLogger(ContractIndexCalcJob.class);
	@Autowired
	@Qualifier("contractIndexCalcService")
	private ContractIndexCalcService contractIndexCalcService;
	@Override
	public void process(JobExecutionMultipleShardingContext arg0) {
		LOG.info("合约指数计算任务开始");
		contractIndexCalcService.calc();
		LOG.info("合约指数计算任务结束");
	}

}
