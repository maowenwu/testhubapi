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

    @Autowired
    private OkFutureDepthJob okFutureDepthJob;
    @Autowired
    private OkFutureKlineJob okFutureKlineJob;
    @Autowired
    private OkFutureOrderJob okFutureOrderJob;
    @Autowired
    private OkFuturePositionJob okFuturePositionJob;
    @Autowired
    private OkFutureTickerJob okFutureTickerJob;
    @Autowired
    private OkFutureUserInfoJob okFutureUserInfoJob;
    @Test
    public void jobTest()  {
        while (true){
            CompletableFuture[] futures=new CompletableFuture[6];
            futures[0]=CompletableFuture.runAsync(()->{
                okFutureDepthJob.execute();
            });
            futures[1]=CompletableFuture.runAsync(()->{
                okFutureKlineJob.execute();
            });
            futures[2]=CompletableFuture.runAsync(()->{
                okFutureOrderJob.execute();
            });
            futures[3]=CompletableFuture.runAsync(()->{
                okFuturePositionJob.execute();
            });
            futures[4]=CompletableFuture.runAsync(()->{
                okFutureTickerJob.execute();
            });
            futures[5]=CompletableFuture.runAsync(()->{
                okFutureUserInfoJob.execute();
            });
            CompletableFuture.allOf(futures).join();
            System.out.println("done");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

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