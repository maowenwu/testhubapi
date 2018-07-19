package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureJobReqDto;

public interface JobManageService {


    ServiceResult startFutureJob(FutureJobReqDto jobReqDto);

    ServiceResult stopFutureJob(FutureJobReqDto jobReqDto);
    
    ServiceResult startSpotJob(FutureJobReqDto jobReqDto);

    ServiceResult stopSpotJob(FutureJobReqDto jobReqDto);

}
