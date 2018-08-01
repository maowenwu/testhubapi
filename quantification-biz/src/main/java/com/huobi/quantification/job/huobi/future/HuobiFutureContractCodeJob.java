package com.huobi.quantification.job.huobi.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.contract.ContractService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class HuobiFutureContractCodeJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        ContractService contractService = ApplicationContextHolder.getContext().getBean(ContractService.class);
        contractService.updateHuobiContractCode();
    }
}
