package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureJobReqDto;
import com.huobi.quantification.dto.JobReqDto;

public interface JobManageService {


    ServiceResult startFutureJob(FutureJobReqDto jobReqDto);

    ServiceResult stopFutureJob(FutureJobReqDto jobReqDto);
    
    ServiceResult startSpotJob(JobReqDto jobReqDto);

    ServiceResult stopSpotJob(JobReqDto jobReqDto);

}
