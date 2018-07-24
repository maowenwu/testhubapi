package com.huobi.quantification.job.huobi.spot;

import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.order.HuobiOrderService;

@DisallowConcurrentExecution
public class HuobiOrderJob extends AbstractQuartzJob{

	@Override
	public void execute(JobParamDto data) {
		HuobiOrderService bean = ApplicationContextHolder.getContext().getBean(HuobiOrderService.class);
		bean.updateHuobiOrder();
	}

}
