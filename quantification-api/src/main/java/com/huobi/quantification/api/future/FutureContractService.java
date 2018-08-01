package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.ContractCodeDto;
import com.huobi.quantification.entity.QuanContractCode;

import java.math.BigDecimal;

public interface FutureContractService {


    ServiceResult<ContractCodeDto> getContractCode(int exchangeId, String symbol, String contractType);

    ServiceResult<ContractCodeDto> getContractCode(int exchangeId, String contractCode);

    ServiceResult<BigDecimal> getExchangeRateOfUSDT2USD();
}
