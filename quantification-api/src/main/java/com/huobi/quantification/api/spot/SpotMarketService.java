package com.huobi.quantification.api.spot;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotCurrentPriceReqDto;
import com.huobi.quantification.dto.SpotCurrentPriceRespDto;

public interface SpotMarketService {
	
	ServiceResult<SpotCurrentPriceRespDto> getCurrentPrice(SpotCurrentPriceReqDto currentPriceReqDto);


}
