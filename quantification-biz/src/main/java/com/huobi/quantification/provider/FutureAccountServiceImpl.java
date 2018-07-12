package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureBalanceDto;
import com.huobi.quantification.dto.FuturePositionDto;
import org.springframework.stereotype.Service;

@Service
public class FutureAccountServiceImpl implements FutureAccountService {


    @Override
    public ServiceResult<FutureBalanceDto> getBalance(int exchangeId, long accountId, String coinType, long timeout, long maxDelay) {
        return null;
    }

    @Override
    public ServiceResult<FuturePositionDto> getPosition(int exchangeId, long accountId, String coinType, long timeout, long maxDelay) {
        return null;
    }
}
