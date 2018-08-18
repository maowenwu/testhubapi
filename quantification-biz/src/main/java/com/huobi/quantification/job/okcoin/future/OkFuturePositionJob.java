package com.huobi.quantification.job.okcoin.future;

import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class OkFuturePositionJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        /*OkFutureAccountService okFutureAccountService = ApplicationContextHolder.getContext().getBean(OkFutureAccountService.class);
        okFutureAccountService.updateOkPosition(data.getAccountId());*/
    }
}
