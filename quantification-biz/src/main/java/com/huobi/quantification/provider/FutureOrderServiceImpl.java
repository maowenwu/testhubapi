package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.dto.FutureOrderReqDto;
import com.huobi.quantification.dto.FutureOrderRespDto;
import com.huobi.quantification.dto.OkTradeOrderDto;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.order.OkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class FutureOrderServiceImpl implements FutureOrderService {

    @Autowired
    private OkOrderService okOrderService;

    @Autowired
    private QuanOrderFutureMapper quanOrderFutureMapper;

    @Override
    public ServiceResult<FutureOrderRespDto> placeOrder(FutureOrderReqDto reqDto) {
        if (reqDto.getExchangeId() == ExchangeEnum.OKEX.getExId()) {
            return placeOkOrder(reqDto);
        }
        return null;
    }

    /**
     * ok 下单
     *
     * @param reqDto
     * @return
     */
    public ServiceResult<FutureOrderRespDto> placeOkOrder(FutureOrderReqDto reqDto) {
        OkTradeOrderDto orderDto = new OkTradeOrderDto();
        orderDto.setAccountId(reqDto.getAccountId());
        String symbol = getOkSymbol(reqDto.getBaseCoin(), reqDto.getQuoteCoin());
        orderDto.setSymbol(symbol);
        orderDto.setContractType(reqDto.getContractType());
        orderDto.setPrice(reqDto.getPrice().toString());
        // todo 下单数量，单位
        orderDto.setAmount(reqDto.getQuantity().toString());
        int type = getOkOrderType(reqDto.getSide(), reqDto.getOffset());
        orderDto.setType(type);
        // todo 订单类型默认给市价单
        orderDto.setMatchPrice(0);
        orderDto.setLeverRate(reqDto.getLever());
        Future<Long> orderIdFuture = AsyncUtils.submit(() -> okOrderService.placeOkOrder(orderDto));

        ServiceResult<FutureOrderRespDto> serviceResult = new ServiceResult<>();
        serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
        serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());

        FutureOrderRespDto respDto = new FutureOrderRespDto();


        QuanOrderFuture orderFuture = new QuanOrderFuture();
        //orderFuture.setStrategyName(order.getStrategyName());
        //orderFuture.setStrategyVersion(order.getStrategyVersion());
        orderFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
        orderFuture.setOrderAccountId(reqDto.getAccountId());
        if (reqDto.isSync()) {
            try {
                orderFuture.setOrderSourceId(orderIdFuture.get());
                respDto.setExOrderId(orderIdFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
                serviceResult.setMessage(ServiceErrorEnum.EXECUTION_ERROR.getMessage());
                serviceResult.setCode(ServiceErrorEnum.EXECUTION_ERROR.getCode());
            }
        }
        orderFuture.setUpdateDate(new Date());
        quanOrderFutureMapper.insert(orderFuture);

        respDto.setLinkOrderId(reqDto.getLinkOrderId());
        respDto.setInnerOrderId(orderFuture.getId());

        serviceResult.setData(respDto);
        return serviceResult;
    }


    private int getOkOrderType(int side, int offset) {
        if (side == 1) {
            // 买入开仓
            if (offset == 1) {
                return 1;
            } else {
                // 买入平仓
                return 2;
            }
        } else {
            // 卖出开仓
            if (offset == 1) {
                return 3;
            } else {
                // 卖出平仓
                return 4;
            }
        }
    }

    private String getOkSymbol(String baseCoin, String quoteCoin) {
        return baseCoin.toLowerCase() + "_" + quoteCoin.toLowerCase();
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
