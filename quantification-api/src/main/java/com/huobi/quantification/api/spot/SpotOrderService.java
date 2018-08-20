package com.huobi.quantification.api.spot;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotCancleAllOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;

public interface SpotOrderService {


    ServiceResult<SpotPlaceOrderRespDto> placeOrder(SpotPlaceOrderReqDto reqDto);

    ServiceResult cancelAllOrder(SpotCancleAllOrderReqDto reqDto);

}
