package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.ContractCodeDto;

public interface FutureContractService {


    ServiceResult<ContractCodeDto> getContractCode(int exchangeId, String symbol, String contractType);


}
