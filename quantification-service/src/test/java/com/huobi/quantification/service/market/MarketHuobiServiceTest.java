package com.huobi.quantification.service.market;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.dao.QuanDepthDetailMapper;
import com.huobi.quantification.dao.QuanDepthMapper;
import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanDepthDetail;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class MarketHuobiServiceTest {
	 @Autowired
	private QuanDepthDetailMapper  quanDepthDetailMapper;
	 @Autowired
	 private QuanDepthMapper quanDepthMapper;

    @Test
    public void getDepth(){
       
    	String str="{\r\n" + 
    			"  \"status\": \"ok\",\r\n" + 
    			"  \"ch\": \"market.btcusdt.depth.step1\",\r\n" + 
    			"  \"ts\": 1489472598812,\r\n" + 
    			"  \"tick\": {\r\n" + 
    			"    \"id\": 1489464585407,\r\n" + 
    			"    \"ts\": 1489464585407,\r\n" + 
    			"    \"bids\": [\r\n" + 
    			"      [7964, 0.0678], // [price, amount]\r\n" + 
    			"      [7963, 0.9162],\r\n" + 
    			"      [7961, 0.1],\r\n" + 
    			"      [7960, 12.8898],\r\n" + 
    			"      [7958, 1.2],\r\n" + 
    			"      [7955, 2.1009],\r\n" + 
    			"      [7954, 0.4708],\r\n" + 
    			"      [7953, 0.0564],\r\n" + 
    			"      [7951, 2.8031],\r\n" + 
    			"      [7950, 13.7785],\r\n" + 
    			"      [7949, 0.125],\r\n" + 
    			"      [7948, 4],\r\n" + 
    			"      [7942, 0.4337],\r\n" + 
    			"      [7940, 6.1612],\r\n" + 
    			"      [7936, 0.02],\r\n" + 
    			"      [7935, 1.3575],\r\n" + 
    			"      [7933, 2.002],\r\n" + 
    			"      [7932, 1.3449],\r\n" + 
    			"      [7930, 10.2974],\r\n" + 
    			"      [7929, 3.2226]\r\n" + 
    			"    ],\r\n" + 
    			"    \"asks\": [\r\n" + 
    			"      [7979, 0.0736],\r\n" + 
    			"      [7980, 1.0292],\r\n" + 
    			"      [7981, 5.5652],\r\n" + 
    			"      [7986, 0.2416],\r\n" + 
    			"      [7990, 1.9970],\r\n" + 
    			"      [7995, 0.88],\r\n" + 
    			"      [7996, 0.0212],\r\n" + 
    			"      [8000, 9.2609],\r\n" + 
    			"      [8002, 0.02],\r\n" + 
    			"      [8008, 1],\r\n" + 
    			"      [8010, 0.8735],\r\n" + 
    			"      [8011, 2.36],\r\n" + 
    			"      [8012, 0.02],\r\n" + 
    			"      [8014, 0.1067],\r\n" + 
    			"      [8015, 12.9118],\r\n" + 
    			"      [8016, 2.5206],\r\n" + 
    			"      [8017, 0.0166],\r\n" + 
    			"      [8018, 1.3218],\r\n" + 
    			"      [8019, 0.01],\r\n" + 
    			"      [8020, 13.6584]\r\n" + 
    			"    ]\r\n" + 
    			"  }\r\n" + 
    			"}";
     JSONObject jsonObject = JSON.parseObject(str);
   	 String data=jsonObject.getString("tick");	    
   	 QuanDepth quanDepth=new QuanDepth();
   	 quanDepthMapper.insert(quanDepth);
		 JSONObject temp=JSON.parseObject(data);
		 JSONArray jsarr=temp.getJSONArray("asks");
		  for (int i = 0; i < jsarr.size(); i++) {
	            JSONArray item = jsarr.getJSONArray(i);
	            QuanDepthDetail depthDetail=new QuanDepthDetail();
	            depthDetail.setDetailPrice(item.getBigDecimal(0));
	           // depthDetail.setDetailAmount(item.getDouble(1));
	            depthDetail.setDepthId(quanDepth.getId());
	            depthDetail.setDetailType(new Byte((byte) 1));
	           quanDepthDetailMapper.insert(depthDetail);
		  }
    	
    }
}
