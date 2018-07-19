package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.request.future.FutureOkCancelOrderRequest;
import com.huobi.quantification.request.future.FutureOkOrderRequest;
import com.huobi.quantification.service.order.OkOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
     * ok 单笔下单
     *
     * @param reqDto
     * @return
     */
    public ServiceResult<FutureOrderRespDto> placeOkOrder(FutureOrderReqDto reqDto) {
        FutureOkOrderRequest orderRequest = new FutureOkOrderRequest();
        orderRequest.setAccountId(reqDto.getAccountId());
        String symbol = getOkSymbol(reqDto.getBaseCoin(), reqDto.getQuoteCoin());
        orderRequest.setSymbol(symbol);
        orderRequest.setContractType(reqDto.getContractType());
        orderRequest.setPrice(reqDto.getPrice().toString());
        // todo 下单数量，单位
        orderRequest.setAmount(reqDto.getQuantity().toString());
        int type = getOkOrderType(reqDto.getSide(), reqDto.getOffset());
        orderRequest.setType(type);
        // todo 订单类型默认给市价单
        orderRequest.setMatchPrice(0);
        orderRequest.setLeverRate(reqDto.getLever() + "");
        Long orderId = okOrderService.placeOkOrder(orderRequest);

        QuanOrderFuture orderFuture = new QuanOrderFuture();
        orderFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
        orderFuture.setAccountId(reqDto.getAccountId());
        orderFuture.setExOrderId(orderId);
        orderFuture.setUpdateDate(new Date());
        quanOrderFutureMapper.insert(orderFuture);
        return null;
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
    public ServiceResult<FutureBatchOrderRespDto> placeBatchOrders(FutureBatchOrderReqDto reqDto) {
        if (reqDto.getExchangeId() == ExchangeEnum.OKEX.getExId()) {
            return placeOkBatchOrder(reqDto);
        }
        return null;
    }

    /**
     * 因为接口定义为可以下不同symbol的单，那么不能使用ok的批量接口，ok仅支持同一symbol批量下单
     * 所以先实现为循环调用
     *
     * @param reqDto
     * @return
     */
    private ServiceResult<FutureBatchOrderRespDto> placeOkBatchOrder(FutureBatchOrderReqDto reqDto) {
        for (FutureBatchOrder order : reqDto.getOrders()) {
            FutureOkOrderRequest orderRequest = new FutureOkOrderRequest();
            orderRequest.setAccountId(reqDto.getAccountId());
            String symbol = getOkSymbol(order.getBaseCoin(), order.getQuoteCoin());
            orderRequest.setSymbol(symbol);
            orderRequest.setContractType(order.getContractType());
            orderRequest.setPrice(order.getPrice());
            // todo 下单数量，单位
            orderRequest.setAmount(order.getQuantity());
            int type = getOkOrderType(order.getSide(), order.getOffset());
            orderRequest.setType(type);
            // todo 订单类型默认给市价单
            orderRequest.setMatchPrice(0);
            orderRequest.setLeverRate(order.getLever());
            Long orderId = okOrderService.placeOkOrder(orderRequest);

            QuanOrderFuture orderFuture = new QuanOrderFuture();
            orderFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
            orderFuture.setAccountId(reqDto.getAccountId());
            orderFuture.setExOrderId(orderId);
            orderFuture.setUpdateDate(new Date());
            quanOrderFutureMapper.insert(orderFuture);
        }
        return null;
    }

    @Override
    public ServiceResult getOrderByInnerOrderId() {
        return null;
    }

    @Override
    public ServiceResult getOrderByExOrderId() {
        return null;
    }

    @Override
    public ServiceResult getOrderByLinkOrderId() {
        return null;
    }

    @Override
    public ServiceResult getOrderByStatus() {
        return null;
    }

    @Override
    public ServiceResult cancelOrder(FutureCancelOrderReqDto reqDto) {
        // 查询待撤销订单
        List<FutureCancelOrder> cancelOrders = reqDto.getOrders();
        List<QuanOrderFuture> uncompleteOrders = new ArrayList<>();
        for (FutureCancelOrder cancelOrder : cancelOrders) {
            cancelOrder.setExchangeId(reqDto.getExchangeId());
            cancelOrder.setAccountId(reqDto.getAccountId());
            uncompleteOrders.addAll(queryOrderFuture(cancelOrder));
        }
        // 开始撤销订单
        CancelOrderContext orderContext = new CancelOrderContext(reqDto.getExchangeId(),
                reqDto.getAccountId(), uncompleteOrders, reqDto.isParallel(), reqDto.getTimeInterval());
        doCancelOrder(orderContext);
        return null;
    }

    private void doCancelOrder(CancelOrderContext orderContext) {
        if (orderContext.getExchangeId() == ExchangeEnum.OKEX.getExId()) {
            cancelOkOrder(orderContext);
        }
    }

    /**
     * 循环方式取消???
     *
     * @param orderContext
     */
    private void cancelOkOrder(CancelOrderContext orderContext) {
        // 调用批量接口进行撤单
        if (orderContext.isParallel()) {
            List<QuanOrderFuture> orders = orderContext.getOrders();
            String orderIds = orders.stream().map(e -> e.getExOrderId().toString()).collect(Collectors.joining(","));
            QuanOrderFuture order = orders.get(0);
            FutureOkCancelOrderRequest orderRequest = new FutureOkCancelOrderRequest();
            orderRequest.setAccountId(orderContext.getAccountId());
            orderRequest.setSymbol(getOkSymbol(order.getBaseCoin(), order.getQuoteCoin()));
            orderRequest.setContractType(order.getContractType());
            orderRequest.setOrderId(orderIds);
            okOrderService.cancelOkOrder(orderRequest);
        } else {
            // 串行撤单
            for (QuanOrderFuture order : orderContext.getOrders()) {
                FutureOkCancelOrderRequest orderRequest = new FutureOkCancelOrderRequest();
                orderRequest.setAccountId(order.getAccountId());
                orderRequest.setSymbol(getOkSymbol(order.getBaseCoin(), order.getQuoteCoin()));
                orderRequest.setContractType(order.getContractType());
                orderRequest.setOrderId(order.getExOrderId() + "");
                okOrderService.cancelOkOrder(orderRequest);
                // 暂停一个时间间隔
                sleep(orderContext.getTimeInterval());
            }
        }

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<QuanOrderFuture> queryOrderFuture(FutureCancelOrder cancelOrder) {
        QuanOrderFuture orderFuture = new QuanOrderFuture();
        BeanUtils.copyProperties(cancelOrder, orderFuture);
        List<QuanOrderFuture> orderFutures = quanOrderFutureMapper.selectBySelective(orderFuture);
        return orderFutures;
    }

    @Override
    public ServiceResult cancelActiveOrder(FutureActiveOrderReqDto reqDto) {
        // 查询待撤销订单
        QuanOrderFuture orderFuture = new QuanOrderFuture();
        List<QuanOrderFuture> uncompleteOrders = quanOrderFutureMapper.selectBySelective(orderFuture);
        // 开始撤销订单
        CancelOrderContext orderContext = new CancelOrderContext(reqDto.getExchangeId(),
                reqDto.getAccountId(), uncompleteOrders, reqDto.isParallel(), reqDto.getTimeInterval());
        doCancelOrder(orderContext);
        return null;
    }

    static class CancelOrderContext {
        private int exchangeId;
        private long accountId;
        private List<QuanOrderFuture> orders;
        private boolean parallel;
        private int timeInterval;

        public CancelOrderContext(int exchangeId, long accountId, List<QuanOrderFuture> orders, boolean parallel, int timeInterval) {
            this.exchangeId = exchangeId;
            this.accountId = accountId;
            this.orders = orders;
            this.parallel = parallel;
            this.timeInterval = timeInterval;
        }

        public int getExchangeId() {
            return exchangeId;
        }

        public long getAccountId() {
            return accountId;
        }

        public List<QuanOrderFuture> getOrders() {
            return orders;
        }

        public boolean isParallel() {
            return parallel;
        }

        public int getTimeInterval() {
            return timeInterval;
        }
    }
}
