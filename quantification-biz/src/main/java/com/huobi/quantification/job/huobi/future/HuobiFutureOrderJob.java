package com.huobi.quantification.job.huobi.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.order.HuobiFutureOrderService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class HuobiFutureOrderJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        HuobiFutureOrderService huobiFutureOrderService = ApplicationContextHolder.getContext().getBean(HuobiFutureOrderService.class);
        //huobiFutureOrderService.updateHuobiOrderInfo(data.getAccountId());
    }
}
