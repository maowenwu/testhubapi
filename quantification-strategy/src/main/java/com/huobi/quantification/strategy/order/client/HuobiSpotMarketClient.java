package com.huobi.quantification.strategy.order.client;

import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotDepthRespDto;
import com.huobi.quantification.enums.ExchangeEnum;
import org.springframework.stereotype.Component;

@Component
public class HuobiSpotMarketClient {

    private SpotMarketService spotMarketService;

    public void te(){
        SpotDepthReqDto reqDto = new SpotDepthReqDto();
        reqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
        ServiceResult<SpotDepthRespDto> spotDepth = spotMarketService.getSpotDepth(reqDto);
    }
}
