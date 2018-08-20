package com.huobi.quantification.api.spot;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;

public interface SpotAccountService {


    ServiceResult<SpotBalanceRespDto> getBalance(SpotBalanceReqDto balanceReqDto);

}
