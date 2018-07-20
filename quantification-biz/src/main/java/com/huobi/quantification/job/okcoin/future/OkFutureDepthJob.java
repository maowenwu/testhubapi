package com.huobi.quantification.job.okcoin.future;

import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.OkFutureMarketService;

@DisallowConcurrentExecution
public class OkFutureDepthJob extends AbstractQuartzJob {


    @Override
    public void execute(JobParamDto data) {
        OkFutureMarketService okFutureMarketService = ApplicationContextHolder.getContext().getBean(OkFutureMarketService.class);
        okFutureMarketService.updateOkDepth(data.getSymbol(), data.getContractType());
    }
}
