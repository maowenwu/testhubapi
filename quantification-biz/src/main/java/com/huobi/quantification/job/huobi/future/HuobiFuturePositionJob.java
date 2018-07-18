package com.huobi.quantification.job.huobi.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.HuobiFutureAccountService;
import com.huobi.quantification.service.account.OkFutureAccountService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class HuobiFuturePositionJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        HuobiFutureAccountService futureAccountService = ApplicationContextHolder.getContext().getBean(HuobiFutureAccountService.class);
        futureAccountService.updateHuobiPosition(data.getAccountId());
    }
}
