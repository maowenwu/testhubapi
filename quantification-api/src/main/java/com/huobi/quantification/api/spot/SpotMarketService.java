package com.huobi.quantification.api.spot;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotCurrentPriceReqDto;
import com.huobi.quantification.dto.SpotCurrentPriceRespDto;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotDepthRespDto;
import com.huobi.quantification.dto.SpotKlineReqDto;
import com.huobi.quantification.dto.SpotKlineRespDto;

public interface SpotMarketService {
	
	ServiceResult<SpotCurrentPriceRespDto> getCurrentPrice(SpotCurrentPriceReqDto currentPriceReqDto);

	ServiceResult<SpotDepthRespDto> getSpotDepth(SpotDepthReqDto depthReqDto);
	
	ServiceResult<SpotKlineRespDto> getSpotKline(SpotKlineReqDto depthReqDto);
	
}
