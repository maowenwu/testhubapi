package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FutureBalanceRespDto;
import com.huobi.quantification.dto.FuturePositionReqDto;
import com.huobi.quantification.dto.FuturePositionRespDto;

public interface FutureAccountService {


    /**
     * 获取账户余额(权益)
     */
    ServiceResult<FutureBalanceRespDto> getBalance(FutureBalanceReqDto balanceReqDto);

    /**
     * 获取账户持仓
     */
    ServiceResult<FuturePositionRespDto> getPosition(FuturePositionReqDto positionReqDto);


}
