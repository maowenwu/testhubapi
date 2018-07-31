package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;

public interface JobManageService {

    /******************OK 期货 **********************/
    ServiceResult addOkFutureCurrentPriceJob(String symbol, String contractType, String cron, boolean enable);

    ServiceResult addOkFutureDepthJob(String symbol, String contractType, String cron, boolean enable);

    ServiceResult addOkFutureIndexJob(String symbol, String cron, boolean enable);

    ServiceResult addOkFutureKlineJob(String symbol, String period, String contractType, String cron, boolean enable);

    ServiceResult addOkFutureOrderJob(Long accountId, String symbol, String contractType, String cron, boolean enable);

    ServiceResult addOkFuturePositionJob(Long accountId, String cron, boolean enable);

    ServiceResult addOkFutureUserInfoJob(Long accountId, String cron, boolean enable);

    /******************Huobi 期货 **********************/
    ServiceResult addHuobiFuturePositionJob(Long accountId, String cron, boolean enable);

    ServiceResult addHuobiFutureUserInfoJob(Long accountId, String cron, boolean enable);

    /******************Huobi 现货 **********************/
    ServiceResult addHuobiSpotAccountJob(Long accountId, String cron, boolean enable);

    ServiceResult addHuobiSpotCurrentPriceJob(String symbol, String cron, boolean enable);

    ServiceResult addHuobiSpotDepthJob(String symbol, String depthType, String cron, boolean enable);

    ServiceResult addHuobiSpotKlineJob(String symbol, String klineType, int size, String cron, boolean enable);

    ServiceResult addHuobiSpotOrderJob(String symbol, Long accountId, String cron, boolean enable);

}
