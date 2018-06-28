package com.huobi.quantification.service.order.impl;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.service.account.AccountService;
import com.huobi.quantification.service.order.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;


    @Test
    public void getOkOrderInfo() {
        orderService.getOkOrderInfo();
    }
    @Test
    public void getOkOrdersInfo() {
        orderService.getOkOrdersInfo();
    }

    @Test
    public void getOkOrdersHistory() {
        orderService.getOkOrdersHistory();
    }


    @Test
    public void placeOkOrder() {
        orderService.placeOkOrder();
    }


    @Test
    public void placeOkOrders() {
        orderService.placeOkOrders();
    }


    @Test
    public void cancelOkOrder() {
        orderService.cancelOkOrder();
    }

    @Test
    public void cancelOkOrders() {
        orderService.cancelOkOrders();
    }




}