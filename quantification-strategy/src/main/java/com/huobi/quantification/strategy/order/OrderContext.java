package com.huobi.quantification.strategy.order;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.StrategyOrderConfigMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.strategy.order.entity.FutureBalance;
import com.huobi.quantification.strategy.order.entity.FuturePosition;
import com.huobi.quantification.strategy.order.entity.SpotBalance;
import com.huobi.quantification.strategy.order.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderContext {

    @Autowired
    private SpotMarketService spotMarketService;

    @Autowired
    private FutureContractService futureContractService;

    @Autowired
    private SpotAccountService spotAccountService;

    @Autowired
    private FutureAccountService futureAccountService;

    @Autowired
    private StrategyOrderConfigMapper strategyOrderConfigMapper;

    @Autowired
    private FutureOrderService futureOrderService;

    public DepthBook getDepth(String symbol) {
        String[] split = symbol.split("_");
        SpotDepthReqDto reqDto = new SpotDepthReqDto();
        reqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
        reqDto.setBaseCoin(split[0]);
        reqDto.setQuoteCoin(split[1]);
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(3000);
        ServiceResult<SpotDepthRespDto> spotDepth = spotMarketService.getSpotDepth(reqDto);
        SpotDepthRespDto.DataBean data = spotDepth.getData().getData();
        DepthBook depthBook = new DepthBook();
        data.getAsks().forEach(e -> {
            depthBook.getAsks().add(new DepthBook.Depth(e.getPrice(), e.getAmount()));
        });
        data.getBids().forEach(e -> {
            depthBook.getBids().add(new DepthBook.Depth(e.getPrice(), e.getAmount()));
        });
        return depthBook;
    }

    public BigDecimal getExchangeRateOfUSDT2USD() {
        ServiceResult<BigDecimal> exchangeRateOfUSDT2USD = futureContractService.getExchangeRateOfUSDT2USD();
        return exchangeRateOfUSDT2USD.getData();
    }


    public SpotBalance getSpotBalance(String coinType) {
        ServiceResult<SpotBalanceRespDto> balance = spotAccountService.getBalance(null);
        Map<String, SpotBalanceRespDto.DataBean> data = balance.getData().getData();
        SpotBalanceRespDto.DataBean dataBean = data.get(coinType);
        if (dataBean != null) {
            SpotBalance spotBalance = new SpotBalance();
            BeanUtils.copyProperties(dataBean, spotBalance);
            return spotBalance;
        } else {
            return null;
        }
    }


    public FutureBalance getFutureBalance(String coinType) {
        ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(null);
        Map<String, FutureBalanceRespDto.DataBean> data = balance.getData().getData();
        FutureBalanceRespDto.DataBean dataBean = data.get(coinType);
        if (dataBean != null) {
            FutureBalance futureBalance = new FutureBalance();
            BeanUtils.copyProperties(dataBean, futureBalance);
            return futureBalance;
        } else {
            return null;
        }
    }

    public FuturePosition getFuturePosition(String coinType) {
        ServiceResult<FuturePositionRespDto> position = futureAccountService.getPosition(null);
        Map<String, List<FuturePositionRespDto.DataBean>> data = position.getData().getData();
        List<FuturePositionRespDto.DataBean> beanList = data.get(coinType);
        FuturePosition futurePosition = new FuturePosition();
        beanList.forEach(e -> {
            if (e.getLongAmount() != null) {
                FuturePosition.LongPosi longPosi = new FuturePosition.LongPosi();
                BeanUtils.copyProperties(e, longPosi);
                futurePosition.setLongPosi(longPosi);
            }
            if (e.getShortAmount() != null) {
                FuturePosition.ShortPosi shortPosi = new FuturePosition.ShortPosi();
                BeanUtils.copyProperties(e, shortPosi);
                futurePosition.setShortPosi(shortPosi);
            }
        });
        return futurePosition;
    }

    public void init() {

    }

    public StrategyOrderConfig getStrategyOrderConfig() {
        StrategyOrderConfig orderConfig = strategyOrderConfigMapper.selectByPrimaryKey(1);
        return orderConfig;
    }

    public Map<BigDecimal, List<FutureOrder>> getActiveOrderMap() {
        ServiceResult<FuturePriceOrderRespDto> activeOrderMap = futureOrderService.getActiveOrderMap(null);
        Map<BigDecimal, List<FuturePriceOrderRespDto.DataBean>> priceOrderMap = activeOrderMap.getData().getPriceOrderMap();
        Map<BigDecimal, List<FutureOrder>> result = new HashMap<>();
        priceOrderMap.forEach((k, v) -> {
            List<FutureOrder> list = new ArrayList<>();
            v.forEach(e -> {
                FutureOrder futureOrder = new FutureOrder();
                BeanUtils.copyProperties(e, futureOrder);
                list.add(futureOrder);
            });
            result.put(k, list);
        });
        return result;
    }


    /**
     * 取消价格不在深度列表中的所有订单
     *
     * @param depthBook
     */
    private void cancelOrderNotInDepthBook(DepthBook depthBook) {
        List<DepthBook.Depth> allDepth = new ArrayList<>();
        allDepth.addAll(depthBook.getAsks());
        allDepth.addAll(depthBook.getBids());
        List<BigDecimal> allPrice = allDepth.stream().map(e -> e.getPrice()).collect(Collectors.toList());

        List<Long> preCancelOrderIds = new ArrayList<>();
        Map<BigDecimal, List<FutureOrder>> orderMap = getActiveOrderMap();
        orderMap.forEach((k, v) -> {
            if (!allPrice.contains(k)) {
                List<Long> list = v.stream().map(e -> e.getExOrderId()).collect(Collectors.toList());
                preCancelOrderIds.addAll(list);
            }
        });
    }
}
