package com.huobi.quantification.service.account.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.huobi.quantification.ServiceApplication;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class AccountHuobiServiceImplTest {

	@Test
    public void getAccount(){
		String s="{\r\n" + 
				"  \"status\": \"ok\",\r\n" + 
				"  \"data\": {\r\n" + 
				"    \"id\": 100009,\r\n" + 
				"    \"type\": \"spot\",\r\n" + 
				"    \"state\": \"working\",\r\n" + 
				"    \"list\": [\r\n" + 
				"      {\r\n" + 
				"        \"currency\": \"usdt\",\r\n" + 
				"        \"type\": \"trade\",\r\n" + 
				"        \"balance\": \"500009195917.4362872650\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"currency\": \"usdt\",\r\n" + 
				"        \"type\": \"frozen\",\r\n" + 
				"        \"balance\": \"328048.1199920000\"\r\n" + 
				"      },\r\n" + 
				"     {\r\n" + 
				"        \"currency\": \"etc\",\r\n" + 
				"        \"type\": \"trade\",\r\n" + 
				"        \"balance\": \"499999894616.1302471000\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"currency\": \"etc\",\r\n" + 
				"        \"type\": \"frozen\",\r\n" + 
				"        \"balance\": \"9786.6783000000\"\r\n" + 
				"      }\r\n" + 
				"     {\r\n" + 
				"        \"currency\": \"eth\",\r\n" + 
				"        \"type\": \"trade\",\r\n" + 
				"        \"balance\": \"499999894616.1302471000\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"currency\": \"eth”,\r\n" + 
				"        \"type\": \"frozen\",\r\n" + 
				"        \"balance\": \"9786.6783000000\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"user-id\": 1000\r\n" + 
				"  }\r\n" + 
				"}";
		
	}
	
}
