package com.huobi.quantification.job.huobi.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.HuobiFutureMarketService;
import com.huobi.quantification.service.market.OkFutureMarketService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class HuobiFutureIndexJob extends AbstractQuartzJob {


    @Override
    public void execute(JobParamDto data) {
        HuobiFutureMarketService futureMarketService = ApplicationContextHolder.getContext().getBean(HuobiFutureMarketService.class);
        futureMarketService.updateHuobiIndex(data.getSymbol());
    }
}