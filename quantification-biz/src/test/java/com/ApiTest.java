package com;

import java.io.IOException;

import org.apache.http.HttpException;

import com.huobi.rest.HuobiAplUtil;

public class ApiTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String klineResult=HuobiAplUtil.kline("btcusdt", "1day", "200");
			System.out.print(klineResult);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
