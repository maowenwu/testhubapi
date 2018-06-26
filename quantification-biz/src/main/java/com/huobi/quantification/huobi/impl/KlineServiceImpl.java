package com.huobi.quantification.huobi.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.dao.QuantificationKlineMapper;
import com.huobi.contract.index.dto.HuobKlineDto;
import com.huobi.quantification.huobi.KlineService;
import com.huobi.quantification.index.entity.QuantificationKline;
import com.huobi.rest.HuobiAplUtil;
@Service("klineService")
public class KlineServiceImpl  implements KlineService{

	@Autowired
	private QuantificationKlineMapper quantificationKlineMapper;
	
	public void insertKline() {
		try {
		  String klineResult=HuobiAplUtil.kline("btcusdt", "1day", "200");	
		  JSONObject jso=JSON.parseObject(klineResult);
			 JSONArray data=jso.getJSONArray("data");			 
			 JSONObject klineobject=data.getJSONObject(0);
			 try {
				 HuobKlineDto stu2=(HuobKlineDto)JSONObject.parseObject(klineobject.toJSONString(), HuobKlineDto.class);	
				 if(stu2!=null) {
						QuantificationKline kline=new QuantificationKline();
						kline.setKlineAmount(Double.valueOf(stu2.getAmount()));
						kline.setKlineClose(new BigDecimal(stu2.getClose()) );
						kline.setKlineCount(Double.valueOf(stu2.getCount()));
						kline.setKlineHigh(new BigDecimal(stu2.getHigh()) );
						kline.setKlineLow(new BigDecimal(stu2.getLow()) );
						kline.setKlineOpen(new BigDecimal(stu2.getOpen()) );
						quantificationKlineMapper.insert(kline); 
				 }

			 }catch(Exception ex) {
				 
			 }
			 
			
		}catch(Exception ex) {
			
		}

	}
}
