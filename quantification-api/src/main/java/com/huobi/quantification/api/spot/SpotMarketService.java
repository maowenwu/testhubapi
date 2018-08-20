package com.huobi.quantification.api.spot;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotCurrentPriceReqDto;
import com.huobi.quantification.dto.SpotCurrentPriceRespDto;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotDepthRespDto;

public interface SpotMarketService {
	
	ServiceResult<SpotCurrentPriceRespDto> getCurrentPrice(SpotCurrentPriceReqDto currentPriceReqDto);

	ServiceResult<SpotDepthRespDto> getDepth(SpotDepthReqDto depthReqDto);

	
}
