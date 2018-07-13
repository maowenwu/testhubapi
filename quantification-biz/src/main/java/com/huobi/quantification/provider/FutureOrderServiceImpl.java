package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureOrderReqDto;
import com.huobi.quantification.dto.FutureOrderRespDto;
import org.springframework.stereotype.Service;

@Service
public class FutureOrderServiceImpl implements FutureOrderService {

    @Override
    public ServiceResult<FutureOrderRespDto> placeOrder(FutureOrderReqDto futureOrderReqDto) {
        return null;
    }

    @Override
    public ServiceResult placeBatchOrders(int exchangeId, long accountID, Object orders, boolean parallel, int timeInterval, boolean sync) {
        return null;
    }

    @Override
    public ServiceResult getOrderByInnerOrderID() {
        return null;
    }

    @Override
    public ServiceResult getOrderByExOrderID() {
        return null;
    }

    @Override
    public ServiceResult getOrderByLinkOrderID() {
        return null;
    }

    @Override
    public ServiceResult getOrderByStatus() {
        return null;
    }

    @Override
    public ServiceResult cancelOrder() {
        return null;
    }

    @Override
    public ServiceResult cancelOrder(Object id) {
        return null;
    }
}
