package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;

public interface JobManageService {

    /******************Huobi 期货 **********************/
    ServiceResult addHuobiFuturePositionJob(Long accountId, String cron, boolean enable);

    ServiceResult addHuobiFutureAccountJob(Long accountId, String cron, boolean enable);

    ServiceResult addHuobiFutureContractCodeJob(String cron, boolean enable);

    ServiceResult addHuobiFutureDepthJob(String symbol, String contractType, String type, String cron, boolean enable);

    /******************Huobi 现货 **********************/
    ServiceResult addHuobiSpotAccountJob(Long accountId, String cron, boolean enable);

    ServiceResult addHuobiSpotCurrentPriceJob(String symbol, String cron, boolean enable);

    ServiceResult addHuobiSpotDepthJob(String symbol, String depthType, String cron, boolean enable);

    ServiceResult addHuobiSpotOrderJob(String cron, boolean enable);

}
