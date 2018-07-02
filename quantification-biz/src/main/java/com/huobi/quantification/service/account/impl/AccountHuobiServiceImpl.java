package com.huobi.quantification.service.account.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.service.account.AccountHuobiService;
import com.huobi.quantification.service.http.HttpService;
/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public class AccountHuobiServiceImpl implements AccountHuobiService {
	
		@Autowired
	    private HttpService httpService;
		@Autowired
		private QuanAccountMapper quanAccountMapper;
		@Autowired
		private QuanAccountAssetMapper quanAccountAssetMapper;
		
	 public Object accounts(String accountId) {
		 	Map<String, String> params = new HashMap<>();
	        String body = httpService.doGet(HttpConstant.HUOBI_ACCOUNT, params);
	        parseAndaccounts(body);
	        return null;
	 }
	 private void parseAndaccounts(String accountres) {
		    long queryId = System.currentTimeMillis();
	        JSONObject jsonObject = JSON.parseObject(accountres);
	        String data=jsonObject.getString("data");
	        JSONObject temp=JSON.parseObject(data);
			 JSONArray jsarr=temp.getJSONArray("list");
			 QuanAccount account=new QuanAccount();
			 quanAccountMapper.insert(account);
			 for(int i=0;i<jsarr.size();i++){  
				 JSONObject list = jsarr.getJSONObject(i);  
				 QuanAccountAsset quanAccountAsset=new QuanAccountAsset();
				 quanAccountAsset.setAccountId(account.getId());
				 quanAccountAssetMapper.insert(quanAccountAsset);
			 }
	 }
}
