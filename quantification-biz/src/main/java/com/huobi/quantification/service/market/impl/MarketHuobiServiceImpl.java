package com.huobi.quantification.service.market.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanDepthDetailMapper;
import com.huobi.quantification.dao.QuanDepthMapper;
import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanDepthDetail;
import com.huobi.quantification.enums.OkSymbolEnum;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.MarketHuobiService;
/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public class MarketHuobiServiceImpl implements MarketHuobiService{

	  @Autowired
	  private HttpService httpService;
	  @Autowired
	  private QuanDepthDetailMapper quanDepthDetailMapper;
	  @Autowired
	  private QuanDepthMapper quanDepthMapper;
	 public Object getTicker(String symbol) {
		        Map<String, String> params = new HashMap<>();
		        params.put("symbol", symbol);
		        String body = httpService.doGet(HttpConstant.HUOBI_TICKER, params);
		 return null;
	 }
	 
	 public  Object getDepth(String symbol,String type) {
		   Map<String, String> params = new HashMap<>();
	        params.put("symbol", symbol);
	        params.put("type", type);
	        String body = httpService.doGet(HttpConstant.HUOBI_DEPTH, params);
	        
	        
	        
		 return null;
	 }
	 public Object getKline(String symbol,String period,String size) {
		  Map<String, String> params = new HashMap<>();
	        params.put("symbol", symbol);
	        params.put("period", period);
	        params.put("size", size);
	        String body = httpService.doGet(HttpConstant.HUOBI_KLINE, params);
		 return null;
	 }
	 
	 private void parseAndSaveDepth(String res, OkSymbolEnum symbolEnum, String type) {
		    
			 JSONObject jsonObject = JSON.parseObject(res);
	    	 String data=jsonObject.getString("tick");	    
	    	 QuanDepth quanDepth=new QuanDepth();
	    	 quanDepthMapper.insert(quanDepth);
			 JSONObject temp=JSON.parseObject(data);
			 JSONArray jsarr=temp.getJSONArray("asks");
			  for (int i = 0; i < jsarr.size(); i++) {
		            JSONArray item = jsarr.getJSONArray(i);
		            QuanDepthDetail depthDetail=new QuanDepthDetail();
		            depthDetail.setDetailPrice(item.getBigDecimal(0));
		            //depthDetail.setDetailAmount(item.getDouble(1));
		            depthDetail.setDepthId(quanDepth.getId());
		           quanDepthDetailMapper.insert(depthDetail);
			  }
		 
	 }
}