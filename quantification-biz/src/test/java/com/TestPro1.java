package com;

import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.dto.CurrencylayerExchangeRateResp;
import com.huobi.quantification.index.dto.HuobiMarketdepthDto;
import com.okcoin.rest.HttpUtilManager;

public class TestPro1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HttpUtilManager httpUtil = HttpUtilManager.getInstance();
		try {
			String result = httpUtil.requestHttpGet("https://api.huobipro.com/","market/depth", "symbol=btcusdt&type=step5");
			
			HuobiMarketdepthDto huobiMarketdepthDto = JSONObject.parseObject(result,
					HuobiMarketdepthDto.class);
			
			System.out.print(result);
			
			
			  JSONObject object = huobiMarketdepthDto.getTick();
              Iterator iterator = object.entrySet().iterator();
              while (iterator.hasNext()) {
                  Map.Entry entry = (Map.Entry) iterator.next();
              }
			
		}catch(Exception ex) {
			
		}
		
	}

}
