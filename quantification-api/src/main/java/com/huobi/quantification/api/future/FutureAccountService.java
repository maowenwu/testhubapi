package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureBalanceDto;
import com.huobi.quantification.dto.FuturePositionDto;

public interface FutureAccountService {


    /**
     * 获取账户余额(权益)
     *
     * @param exchangeId
     * @param accountId
     * @param coinType
     * @param timeout
     * @param maxDelay
     * @return
     */
    ServiceResult<FutureBalanceDto> getBalance(int exchangeId, long accountId, String coinType, long timeout, long maxDelay);

    /**
     * 获取账户持仓
     *
     * @param exchangeId
     * @param accountId
     * @param coinType
     * @param timeout
     * @param maxDelay
     * @return
     */
    ServiceResult<FuturePositionDto> getPosition(int exchangeId, long accountId, String coinType, long timeout, long maxDelay);
}
