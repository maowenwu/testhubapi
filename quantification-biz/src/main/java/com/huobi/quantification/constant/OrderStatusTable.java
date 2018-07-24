package com.huobi.quantification.constant;

import com.huobi.quantification.enums.OrderStatusEnum;

import java.util.HashMap;
import java.util.Map;

public class OrderStatusTable {


    public static class OkOrderStatus {

        //0等待成交 1部分成交 2全部成交 -1撤单 4撤单处理中 5部分成交已撤单（5这个状态由我们自己计算得出）
        private static Map<Integer, OrderStatusEnum> map = new HashMap<>();

        static {
            map.put(0, OrderStatusEnum.SUBMITTED);
            map.put(1, OrderStatusEnum.PARTIAL_FILLED);
            map.put(2, OrderStatusEnum.FILLED);
            map.put(-1, OrderStatusEnum.CANCELED);
            map.put(4, OrderStatusEnum.CANCELING);
            map.put(5, OrderStatusEnum.PARTIAL_CANCELED);
        }

        public static OrderStatusEnum getOrderStatus(Integer orderStatus) {
            return map.get(orderStatus);
        }
    }
}
