package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;

import java.util.List;

public interface FutureOrderService {

    ServiceResult updateOrderInfo(FutureUpdateOrderReqDto reqDto);

    ServiceResult<FuturePlaceOrderRespDto> placeOrder(FuturePlaceOrderReqDto futurePlaceOrderReqDto);

    ServiceResult<FuturePriceOrderRespDto> getActiveOrderMap(FuturePriceOrderReqDto reqDto);

    ServiceResult<Long> cancelSingleOrder(FutureCancelSingleOrderReqDto reqDto);

    ServiceResult cancelAllOrder(FutureCancelAllOrderReqDto reqDto);

    ServiceResult replenishOrder(FutureReplenishOrderReqDto reqDto);

}
