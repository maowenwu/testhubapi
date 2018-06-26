package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.contract.index.dto.HuobKlineDto;

public class HuobiapiTest {
	public static void main(String[] args) {
		//TestHuobiAccounts();
		//Testbalance();
		//detail();
		kline();
	}
	//用户资产APIGET /v1/account/accounts
	public static void TestHuobiAccounts() {
		String s="{\r\n" + 
				"  \"status\": \"ok\",\r\n" + 
				"  \"data\": [\r\n" + 
				"    {\r\n" + 
				"      \"id\": 100009,\r\n" + 
				"      \"type\": \"spot\",\r\n" + 
				"      \"state\": \"working\",\r\n" + 
				"      \"user-id\": 1000\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
		 JSONObject jso=JSON.parseObject(s);
		 JSONArray jsarr=jso.getJSONArray("data");
		 JSONObject ao=jsarr.getJSONObject(0);
		 String vString=ao.getString("type");
		 System.out.println(vString);
	}
	///v1/account/accounts/{account-id}/balance 查询Pro站指定账户的余额
	public static void Testbalance() {
		String s="{\r\n" + 
				"  \"status\": \"ok\",\r\n" + 
				"  \"data\": {\r\n" + 
				"    \"id\": 100009,\r\n" + 
				"    \"type\": \"spot\",\r\n" + 
				"    \"state\": \"working\",\r\n" + 
				"    \"list\": [\r\n" + 
				"      {\r\n" + 
				"        \"currency1\": \"usdt\",\r\n" + 
				"        \"type\": \"trade\",\r\n" + 
				"        \"balance\": \"500009195917.4362872650\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"currency1\": \"usdt\",\r\n" + 
				"        \"type\": \"frozen\",\r\n" + 
				"        \"balance\": \"328048.1199920000\"\r\n" + 
				"      },\r\n" + 
				"     {\r\n" + 
				"        \"currency1\": \"etc\",\r\n" + 
				"        \"type\": \"trade\",\r\n" + 
				"        \"balance\": \"499999894616.1302471000\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"currency1\": \"etc\",\r\n" + 
				"        \"type\": \"frozen\",\r\n" + 
				"        \"balance\": \"9786.6783000000\"\r\n" + 
				"      }\r\n" + 
				"     {\r\n" + 
				"        \"currency\": \"eth\",\r\n" + 
				"        \"type\": \"trade\",\r\n" + 
				"        \"balance\": \"499999894616.1302471000\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"currency1\": \"eth”,\r\n" + 
				"        \"type\": \"frozen\",\r\n" + 
				"        \"balance\": \"9786.6783000000\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"user-id\": 1000\r\n" + 
				"  }\r\n" + 
				"}";	
		 JSONObject jso=JSON.parseObject(s);
		 String data=jso.getString("data");
		 
		 JSONObject temp=JSON.parseObject(data);
		 JSONArray jsarr=temp.getJSONArray("list");
		 for(int i=0;i<jsarr.size();i++){  
			 JSONObject list = jsarr.getJSONObject(i);   
			 System.out.println(list.get("currency1")+"=") ;    
		 }
	}
	public static void detail() {
		String s="{\r\n" + 
				"\"status\":\"ok\",\r\n" + 
				"\"ch\":\"market.ethusdt.detail.merged\",\r\n" + 
				"\"ts\":1499225276950,\r\n" + 
				"\"tick\":{\r\n" + 
				"  \"id\":1499225271,\r\n" + 
				"  \"ts\":1499225271000,\r\n" + 
				"  \"close\":1885.0000,\r\n" + 
				"  \"open\":1960.0000,\r\n" + 
				"  \"high\":1985.0000,\r\n" + 
				"  \"low\":1856.0000,\r\n" + 
				"  \"amount\":81486.2926,\r\n" + 
				"  \"count\":42122,\r\n" + 
				"  \"vol\":157052744.85708200,\r\n" + 
				"  \"ask\":[1885.0000,21.8804],\r\n" + 
				"  \"bid\":[1884.0000,1.6702]\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"";
		 JSONObject jso=JSON.parseObject(s);
		 String data=jso.getString("tick");
		 JSONObject temp=JSON.parseObject(data);
		 JSONArray jsarr=temp.getJSONArray("ask");
		 for(int index=0;index<jsarr.size();index++){				  
			  System.out.println("-----------"+index);		
			  Object jo = jsarr.get(index);  
			  //System.out.println(jo.toString());
			String[]  arr= jo.toString().replace("[", "").replace("]", "").split(",");
			for(int i=0;i<arr.length;i++) {
				System.out.println(arr[i]);
			}
			 // getKeyAndValue(jo);
		  }
		 JSONArray jsarr2=temp.getJSONArray("bid");
		 for(int index=0;index<jsarr2.size();index++){				  
			  System.out.println("-----------"+index);		
			  Object jo = jsarr.get(index);  
			  //System.out.println(jo.toString());
			String[]  arr= jo.toString().replace("[", "").replace("]", "").split(",");
			for(int i=0;i<arr.length;i++) {
				System.out.println(arr[i]);
			}
			 // getKeyAndValue(jo);
		  }
		  
	}
	//查询某个订单v1/order/orders/{order-id}
	public static void Testorder() {
		
	}
	public static void kline() {
		String str="{\r\n" + 
				"  \"status\": \"ok\",\r\n" + 
				"  \"ch\": \"market.btcusdt.kline.1day\",\r\n" + 
				"  \"ts\": 1499223904680,\r\n" + 
				"  \"data\": [\r\n" + 
				"{\r\n" + 
				"    \"id\": 1499184000,\r\n" + 
				"    \"amount\": 37593.0266,\r\n" + 
				"    \"count\": 0,\r\n" + 
				"    \"open\": 1935.2000,\r\n" + 
				"    \"close\": 1879.0000,\r\n" + 
				"    \"low\": 1856.0000,\r\n" + 
				"    \"high\": 1940.0000,\r\n" + 
				"    \"vol\": 71031537.97866500\r\n" + 
				"  },\r\n" + 
				"// more data here\r\n" + 
				"]\r\n" + 
				"}";
		 JSONObject jso=JSON.parseObject(str);
		 JSONArray jsarr=jso.getJSONArray("data");
		 JSONObject ao=jsarr.getJSONObject(0);
		 try {
			 HuobKlineDto stu2=(HuobKlineDto)JSONObject.parseObject(ao.toJSONString(), HuobKlineDto.class);
			 System.out.println(stu2.getAmount());
		 }catch(Exception ex) {
			 
		 }
		 
		 String vString=ao.getString("id");
		 System.out.println(vString);
	}
}
