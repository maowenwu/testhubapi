package com.huobi.quantification.job.okcoin.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.OkFutureMarketService;
import org.quartz.*;

@DisallowConcurrentExecution
public class OkFutureDepthJob extends AbstractQuartzJob {


    @Override
    public void execute(JobParamDto data) {
        OkFutureMarketService okFutureMarketService = ApplicationContextHolder.getContext().getBean(OkFutureMarketService.class);
        okFutureMarketService.updateOkDepth(data.getSymbol(), data.getContractType());
    }
}
