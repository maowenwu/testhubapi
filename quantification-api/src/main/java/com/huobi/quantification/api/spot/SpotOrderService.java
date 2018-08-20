package com.huobi.quantification.api.spot;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotActiveOrderCancelReqDto;
import com.huobi.quantification.dto.SpotCancleAllOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;

public interface SpotOrderService {


    /**
     * 下单接口
     *
     * @return
     */
    ServiceResult<SpotPlaceOrderRespDto> placeOrder(SpotPlaceOrderReqDto reqDto);


    /**
     * 批量取消符合条件的订单
     *
     * @param reqDto
     * @return
     */
    ServiceResult cancelAllOrder(SpotCancleAllOrderReqDto reqDto);

}
