package com.huobi.quantification.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.huobi.quantification.dto.*;
import com.huobi.quantification.enums.ExchangeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.entity.QuanContractCode;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.enums.OrderStatusEnum;
import com.huobi.quantification.request.future.FutureOkCancelOrderRequest;
import com.huobi.quantification.request.future.FutureOkOrderRequest;
import com.huobi.quantification.service.contract.ContractService;
import com.huobi.quantification.service.order.OkOrderService;

@Service
public class FutureOrderServiceImpl implements FutureOrderService {

    @Autowired
    private OkOrderService okOrderService;

    @Autowired
    private QuanOrderFutureMapper quanOrderFutureMapper;

    @Autowired
    private ContractService contractService;

    @Override
    public ServiceResult<FuturePlaceOrderRespDto> placeOrder(FuturePlaceOrderReqDto reqDto) {
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
    public ServiceResult<FuturePlaceOrderRespDto> placeOkOrder(FuturePlaceOrderReqDto reqDto) {
        // 插入order表生成内部订单id
        QuanOrderFuture orderFuture = new QuanOrderFuture();
        orderFuture.setExchangeId(ExchangeEnum.OKEX.getExId());
        orderFuture.setAccountId(reqDto.getAccountId());
        orderFuture.setLinkOrderId(reqDto.getLinkOrderId());
        orderFuture.setStatus(OrderStatusEnum.PRE_SUBMITTED.getOrderStatus());
        orderFuture.setCreateDate(new Date());
        orderFuture.setUpdateDate(new Date());
        quanOrderFutureMapper.insert(orderFuture);

        FuturePlaceOrderRespDto respDto = new FuturePlaceOrderRespDto();
        respDto.setInnerOrderId(orderFuture.getInnerOrderId());
        respDto.setLinkOrderId(reqDto.getLinkOrderId());
        if (reqDto.isSync()) {
            Long okOrderId = doPlaceOkOrder(reqDto, orderFuture);
            respDto.setExOrderId(okOrderId);
        } else {
            // 异步执行
            AsyncUtils.runAsyncNoException(() -> {
                doPlaceOkOrder(reqDto, orderFuture);
            });
        }
        return ServiceResult.buildSuccessResult(respDto);
    }


    private Long doPlaceOkOrder(FuturePlaceOrderReqDto reqDto, QuanOrderFuture orderFuture) {
        FutureOkOrderRequest orderRequest = new FutureOkOrderRequest();
        orderRequest.setAccountId(reqDto.getAccountId());
        String symbol = null;
        String contractType = null;
        String contractCode = null;
        if (StringUtils.isNotEmpty(reqDto.getContractCode())) {
            QuanContractCode quanContractCode = contractService.getContractCode(reqDto.getExchangeId(), reqDto.getContractCode());
            symbol = quanContractCode.getSymbol();
            contractType = quanContractCode.getContractType();
            contractCode = quanContractCode.getContractCode();
        } else {
            symbol = getOkSymbol(reqDto.getBaseCoin(), reqDto.getQuoteCoin());
            contractType = reqDto.getContractType();
        }
        orderRequest.setSymbol(symbol);
        orderRequest.setContractType(contractType);
        orderRequest.setPrice(reqDto.getPrice().toString());
        // todo 下单数量，单位
        orderRequest.setAmount(reqDto.getQuantity().toString());
        int type = getOkOrderType(reqDto.getSide(), reqDto.getOffset());
        orderRequest.setType(type);
        // todo 订单类型默认给市价单
        orderRequest.setMatchPrice(0);
        orderRequest.setLeverRate(reqDto.getLever() + "");
        Long orderId = okOrderService.placeOkOrder(orderRequest);
        // 下单完成后更新交易所id到order表
        orderFuture.setExOrderId(orderId);
        orderFuture.setContractType(contractType);
        orderFuture.setContractCode(contractCode);
        orderFuture.setSourceStatus(0);
        quanOrderFutureMapper.updateByPrimaryKeySelective(orderFuture);
        return orderId;
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
    public ServiceResult<List<FutureBatchOrderRespDto>> placeBatchOrders(FutureBatchOrderReqDto reqDto) {
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
    private ServiceResult<List<FutureBatchOrderRespDto>> placeOkBatchOrder(FutureBatchOrderReqDto reqDto) {
        List<FutureBatchOrderRespDto> list = new ArrayList<>();
        for (FutureBatchOrder order : reqDto.getOrders()) {
            FuturePlaceOrderReqDto orderReqDto = new FuturePlaceOrderReqDto();
            orderReqDto.setExchangeId(reqDto.getExchangeId());
            orderReqDto.setAccountId(reqDto.getAccountId());
            orderReqDto.setSync(reqDto.isSync());

            orderReqDto.setBaseCoin(order.getBaseCoin());
            orderReqDto.setQuoteCoin(order.getQuoteCoin());
            orderReqDto.setContractType(order.getContractType());
            orderReqDto.setContractCode(order.getContractCode());
            orderReqDto.setSide(order.getSide());
            orderReqDto.setOffset(order.getOffset());
            orderReqDto.setOrderType(order.getOrderType());
            orderReqDto.setPrice(order.getPrice());
            orderReqDto.setQuantity(order.getQuantity());
            orderReqDto.setLever(order.getLever());

            ServiceResult<FuturePlaceOrderRespDto> serviceResult = placeOrder(orderReqDto);
            FutureBatchOrderRespDto orderRespDto = new FutureBatchOrderRespDto();
            BeanUtils.copyProperties(serviceResult.getData(), orderRespDto);
            list.add(orderRespDto);
            sleep(reqDto.getTimeInterval());
        }
        return ServiceResult.buildSuccessResult(list);
    }

    @Override
    public ServiceResult<FutureQueryOrderRespDto> getOrderByInnerOrderId(FutureQueryOrderInnerReqDto reqDto) {
        List<QuanOrderFuture> orderFutures = quanOrderFutureMapper.selectByInnerOrderIds(reqDto.getInnerOrderId());
        Map<Long, FutureQueryOrderRespDto.DataBean> data = new HashMap<>();
        orderFutures.forEach(e -> {
            FutureQueryOrderRespDto.DataBean dataBean = new FutureQueryOrderRespDto.DataBean();
            BeanUtils.copyProperties(e, dataBean);
            data.put(e.getInnerOrderId(), dataBean);
        });

        FutureQueryOrderRespDto respDto = new FutureQueryOrderRespDto();
        respDto.setData(data);
        return ServiceResult.buildSuccessResult(respDto);
    }

    @Override
    public ServiceResult<FutureQueryOrderRespDto> getOrderByExOrderId(FutureQueryOrderExOrderIdReqDto reqDto) {
        Map params = new HashMap();
        BeanUtils.copyProperties(reqDto, params);
        List<QuanOrderFuture> orderFutures = quanOrderFutureMapper.selectByExOrderIds(params);
        Map<Long, FutureQueryOrderRespDto.DataBean> data = new HashMap<>();
        orderFutures.forEach(e -> {
            FutureQueryOrderRespDto.DataBean dataBean = new FutureQueryOrderRespDto.DataBean();
            BeanUtils.copyProperties(e, dataBean);
            data.put(e.getInnerOrderId(), dataBean);
        });

        FutureQueryOrderRespDto respDto = new FutureQueryOrderRespDto();
        respDto.setData(data);
        return ServiceResult.buildSuccessResult(respDto);
    }

    @Override
    public ServiceResult<FutureQueryOrderRespDto> getOrderByLinkOrderId(FutureQueryOrderLinkReqDto reqDto) {
        Map params = new HashMap();
        BeanUtils.copyProperties(reqDto, params);
        List<QuanOrderFuture> orderFutures = quanOrderFutureMapper.selectByLinkOrderIds(params);
        Map<Long, FutureQueryOrderRespDto.DataBean> data = new HashMap<>();
        orderFutures.forEach(e -> {
            FutureQueryOrderRespDto.DataBean dataBean = new FutureQueryOrderRespDto.DataBean();
            BeanUtils.copyProperties(e, dataBean);
            data.put(e.getInnerOrderId(), dataBean);
        });

        FutureQueryOrderRespDto respDto = new FutureQueryOrderRespDto();
        respDto.setData(data);
        return ServiceResult.buildSuccessResult(respDto);
    }

    @Override
    public ServiceResult<FutureQueryOrderRespDto> getOrderByStatus(FutureQueryOrderStatusReqDto reqDto) {
        QuanOrderFuture orderFuture = new QuanOrderFuture();
        BeanUtils.copyProperties(reqDto, orderFuture);
        List<QuanOrderFuture> orderFutures = quanOrderFutureMapper.selectBySelective(orderFuture);
        Map<Long, FutureQueryOrderRespDto.DataBean> data = new HashMap<>();
        orderFutures.forEach(e -> {
            FutureQueryOrderRespDto.DataBean dataBean = new FutureQueryOrderRespDto.DataBean();
            BeanUtils.copyProperties(e, dataBean);
            data.put(e.getInnerOrderId(), dataBean);
        });

        FutureQueryOrderRespDto respDto = new FutureQueryOrderRespDto();
        respDto.setData(data);
        return ServiceResult.buildSuccessResult(respDto);
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
        BeanUtils.copyProperties(reqDto, orderFuture);
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

    @Override
    public ServiceResult<FuturePriceOrderRespDto> getActiveOrderMap(FuturePriceOrderReqDto reqDto) {
        // todo
        return null;
    }
}
