package com.huobi.quantification.strategy.order;

import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.strategy.order.entity.FutureOrder;
import com.huobi.quantification.strategy.order.entity.FuturePosition;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderReader {

    private List<FutureOrder> orders = new ArrayList<>();

    private Map<BigDecimal, List<FutureOrder>> buyOrderMap;

    private Map<BigDecimal, List<FutureOrder>> sellOrderMap;

    private Map<BigDecimal, Map<OffsetEnum, List<FutureOrder>>> priceOffsetBuyOrderMap;

    private Map<BigDecimal, Map<OffsetEnum, List<FutureOrder>>> priceOffsetSellOrderMap;

    private FuturePosition position;

    public OrderReader(List<FutureOrder> orders, FuturePosition position) {
        this.position = position;
        this.orders.addAll(orders);
        init();
    }

    private void init() {
        List<FutureOrder> buyOrderList = new ArrayList<>();
        List<FutureOrder> sellOrderList = new ArrayList<>();
        this.orders.stream().forEach(e -> {
            if ((e.getSide().equals(SideEnum.BUY.getSideType()))) {
                buyOrderList.add(e);
            } else {
                sellOrderList.add(e);
            }
        });
        buyOrderMap = buyOrderList.stream().collect(Collectors.groupingBy(e -> e.getOrderPrice()));
        sellOrderMap = sellOrderList.stream().collect(Collectors.groupingBy(e -> e.getOrderPrice()));

        priceOffsetBuyOrderMap = new HashMap<>();
        buyOrderMap.forEach((k, v) -> {
            Map<OffsetEnum, List<FutureOrder>> map = v.stream().collect(Collectors.groupingBy(e -> OffsetEnum.valueOf(e.getOffset())));
            priceOffsetBuyOrderMap.put(k, map);
        });

        priceOffsetSellOrderMap = new HashMap<>();
        sellOrderMap.forEach((k, v) -> {
            Map<OffsetEnum, List<FutureOrder>> map = v.stream().collect(Collectors.groupingBy(e -> OffsetEnum.valueOf(e.getOffset())));
            priceOffsetSellOrderMap.put(k, map);
        });
    }

    public void addOrder(FutureOrder order) {
        this.orders.add(order);
        init();
    }

    public BigDecimal getBidAmountTotalByPrice(BigDecimal price) {
        List<FutureOrder> orders = buyOrderMap.get(price);
        if (CollectionUtils.isEmpty(orders)) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal sum = BigDecimal.ZERO;
            for (FutureOrder order : orders) {
                sum = sum.add(order.getOrderQty().subtract(order.getDealQty()));
            }
            return sum;
        }
    }

    public BigDecimal getAskAmountTotalByPrice(BigDecimal price) {
        List<FutureOrder> orders = sellOrderMap.get(price);
        if (CollectionUtils.isEmpty(orders)) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal sum = BigDecimal.ZERO;
            for (FutureOrder order : orders) {
                sum = sum.add(order.getOrderQty().subtract(order.getDealQty()));
            }
            return sum;
        }
    }

    public List<Long> getBidExOrderIdByPrice(BigDecimal price) {
        List<FutureOrder> orders = buyOrderMap.get(price);
        if (CollectionUtils.isEmpty(orders)) {
            return new ArrayList<>();
        } else {
            return orders.stream().map(e -> e.getExOrderId()).collect(Collectors.toList());
        }
    }

    public List<Long> getAskExOrderIdByPrice(BigDecimal price) {
        List<FutureOrder> orders = sellOrderMap.get(price);
        if (CollectionUtils.isEmpty(orders)) {
            return new ArrayList<>();
        } else {
            return orders.stream().map(e -> e.getExOrderId()).collect(Collectors.toList());
        }
    }


    /**
     * 返回多仓仓位上限（包括持仓和未成交的开仓单）
     *
     * @return
     */
    public BigDecimal getBidPositionTotal() {
        List<FutureOrder> orders = new ArrayList<>();
        priceOffsetBuyOrderMap.forEach((k, v) -> {
            v.forEach((k2, v2) -> {
                if (k2 == OffsetEnum.LONG) {
                    orders.addAll(v2);
                }
            });
        });
        BigDecimal sum = BigDecimal.ZERO;
        FuturePosition.LongPosi longPosi = position.getLongPosi();
        if (longPosi != null) {
            sum = sum.add(longPosi.getLongAvailable());
        }
        for (FutureOrder order : orders) {
            sum = sum.add(order.getOrderQty().subtract(order.getDealQty()));
        }
        return sum;
    }


    /**
     * 返回空仓仓位上限（包括持仓和未成交的开仓单）
     *
     * @return
     */
    public BigDecimal getAskPositionTotal() {
        List<FutureOrder> orders = new ArrayList<>();
        priceOffsetSellOrderMap.forEach((k, v) -> {
            v.forEach((k2, v2) -> {
                if (k2 == OffsetEnum.LONG) {
                    orders.addAll(v2);
                }
            });
        });

        BigDecimal sum = BigDecimal.ZERO;
        FuturePosition.ShortPosi shortPosi = position.getShortPosi();
        if (shortPosi != null) {
            sum = sum.add(shortPosi.getShortAvailable());
        }
        for (FutureOrder order : orders) {
            sum = sum.add(order.getOrderQty().subtract(order.getDealQty()));
        }
        return sum;
    }


}
