package com.huobi.quantification.api.common;

import java.util.List;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.ExchangeConfigResqDto;

public interface ExchangeConfigService {
	
	ServiceResult<List<ExchangeConfigResqDto>> getAllExchangeConfig();

}
