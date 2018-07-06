package com.huobi.quantification.api;

import com.huobi.quantification.common.ServiceResult;

public interface JobManageService {


    ServiceResult startFutureJob(int exchangeId, int jobType, Long accountId, String symbol, String contractType, String cron);

    ServiceResult stopFutureJob(int exchangeId, int jobType, Long accountId, String symbol, String contractType, String cron);

}
