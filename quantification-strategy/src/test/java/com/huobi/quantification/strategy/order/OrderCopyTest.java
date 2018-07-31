package com.huobi.quantification.strategy.order;

import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.strategy.order.entity.DepthBook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class OrderCopyTest {

    @Autowired
    private OrderCopy orderCopy;


    @Test
    public void copyOrder() {
        orderCopy.copyOrder();
    }


    @Test
    public void init() {
    }

}