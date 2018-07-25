package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.ContractCodeDto;

import java.math.BigDecimal;

public interface FutureContractService {


    ServiceResult<ContractCodeDto> getContractCode(int exchangeId, String symbol, String contractType);


    ServiceResult<BigDecimal> getExchangeRateOfUSDT2USD();
}
