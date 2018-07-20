package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureJobReqDto;

public interface JobManageService {

    ServiceResult addOkFutureCurrentPriceJob(String symbol, String contractType, String cron, boolean enable);

    ServiceResult addOkFutureDepthJob(String symbol, String contractType, String cron, boolean enable);

    ServiceResult addOkFutureIndexJob(String symbol, String cron, boolean enable);

    ServiceResult addOkFutureKlineJob(String symbol, String period, String contractType, String cron, boolean enable);

    ServiceResult addOkFutureOrderJob(Long accountId, String symbol, String contractType, String cron, boolean enable);

    ServiceResult addOkFuturePositionJob(Long accountId, String cron, boolean enable);

    ServiceResult addOkFutureUserInfoJob(Long accountId, String cron, boolean enable);



    ServiceResult startSpotJob(FutureJobReqDto jobReqDto);

    ServiceResult stopSpotJob(FutureJobReqDto jobReqDto);

}
