package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FutureBalanceRespDto;
import com.huobi.quantification.dto.FuturePositionReqDto;
import com.huobi.quantification.dto.FuturePositionRespDto;

public interface FutureAccountService {


    /**
     * 获取账户余额(权益)
     *
     * @param balanceReqDto
     * @return
     */
    ServiceResult<FutureBalanceRespDto> getBalance(FutureBalanceReqDto balanceReqDto);

    /**
     * 获取账户持仓
     *
     * @param positionReqDto
     * @return
     */
    ServiceResult<FuturePositionRespDto> getPosition(FuturePositionReqDto positionReqDto);
    
    void saveAccountsInfo(Long accountId, String contractCode);
    
    ServiceResult<FutureBalanceRespDto> getAccountInfo(Long accountId, String contractCode);
    
    ServiceResult<FuturePositionRespDto> getAccountPosition(Long accountId, String contractCode);

}
