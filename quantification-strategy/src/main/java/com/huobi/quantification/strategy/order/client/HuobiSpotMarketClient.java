package com.huobi.quantification.strategy.order.client;

import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotDepthRespDto;
import com.huobi.quantification.enums.ExchangeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HuobiSpotMarketClient {

    @Autowired
    private SpotMarketService spotMarketService;

    public ServiceResult<SpotDepthRespDto> getDepth(String symbol) {
        String[] split = symbol.split("_");
        SpotDepthReqDto reqDto = new SpotDepthReqDto();
        reqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
        reqDto.setBaseCoin(split[0]);
        reqDto.setQuoteCoin(split[1]);
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(1000);
        return spotMarketService.getSpotDepth(reqDto);
    }
}
