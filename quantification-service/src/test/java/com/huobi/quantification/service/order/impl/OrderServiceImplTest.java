package com.huobi.quantification.service.order.impl;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.OkCancelOrderDto;
import com.huobi.quantification.dto.OkTradeOrderDto;
import com.huobi.quantification.facade.OkOrderServiceFacade;
import com.huobi.quantification.job.okcoin.*;
import com.huobi.quantification.service.order.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OkOrderServiceFacade okOrderServiceFacade;

    @Test
    public void cancelOkOrder(){
        OkCancelOrderDto okCancelOrderDto = new OkCancelOrderDto();
        okCancelOrderDto.setAccountId(1L);
        okCancelOrderDto.setSymbol("btc_usd");
        okCancelOrderDto.setContractType("this_week");
        okCancelOrderDto.setOrderId("1038730059004928");
        ServiceResult serviceResult =
                okOrderServiceFacade.cancelOkOrder(okCancelOrderDto);
        System.out.println(serviceResult);
    }


    @Test
    public void placeOkOrder(){
        OkTradeOrderDto orderDto = new OkTradeOrderDto();
        orderDto.setAccountId(1L);
        orderDto.setSymbol("btc_usd");
        orderDto.setContractType("this_week");
        orderDto.setPrice("6500");
        orderDto.setAmount("1");
        orderDto.setType(1);
        orderDto.setMatchPrice(0);
        orderDto.setLeverRate(10);
        ServiceResult serviceResult = okOrderServiceFacade.placeOkOrder(orderDto);
        System.out.println(serviceResult);
    }

}