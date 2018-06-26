package com.huobi.contract.index.contract.index.service.impl;


import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.service.ServiceApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class BaseIndexGrabServiceTest {

    @Autowired
    private BaseIndexGrabService baseIndexGrabService;

    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;


}
