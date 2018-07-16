package com.huobi.quantification.api.spot;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;

public interface SpotAccountService {

	/**
     * 获取账户余额(权益)
     *
     * @param balanceReqDto
     * @return
     */
    ServiceResult<SpotBalanceRespDto> getBalance(SpotBalanceReqDto balanceReqDto);
}
