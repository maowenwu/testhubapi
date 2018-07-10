package com.huobi.quantification.job.huobi.spot;

import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.order.HuobiOrderService;

@DisallowConcurrentExecution
public class HuobiOrderJob extends AbstractQuartzJob{

	@Override
	public void execute(Object data) {
		HuobiOrderService bean = ApplicationContextHolder.getContext().getBean(HuobiOrderService.class);
		if (data instanceof QuanJob) {
			QuanJob job = (QuanJob) data;
		}
		bean.updateHuobiOrder(123L);
	}

}
