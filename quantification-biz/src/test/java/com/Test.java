package com;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import com.huobi.quantification.index.dto.HuobiMarketdepthDto;

public class Test {
	public static void main(String[] args) {
		
		String str="{\r\n" + 
				"	\"status\": \"ok\",\r\n" + 
				"	\"ch\": \"market.btcusdt.depth.step1\",\r\n" + 
				"	\"ts\": 1489472598812,\r\n" + 
				"	\"tick\": {\r\n" + 
				"		\"id\": 1489464585407,\r\n" + 
				"		\"ts\": 1489464585407,\r\n" + 
				"		\"bids\": [\r\n" + 
				"			[7964, 0.0678],\r\n" + 
				"			[7963, 0.9162],\r\n" + 
				"			[7961, 0.1],\r\n" + 
				"			[7960, 12.8898],\r\n" + 
				"			[7958, 1.2],\r\n" + 
				"			[7955, 2.1009],\r\n" + 
				"			[7954, 0.4708],\r\n" + 
				"			[7953, 0.0564],\r\n" + 
				"			[7951, 2.8031],\r\n" + 
				"			[7950, 13.7785],\r\n" + 
				"			[7949, 0.125],\r\n" + 
				"			[7948, 4],\r\n" + 
				"			[7942, 0.4337],\r\n" + 
				"			[7940, 6.1612],\r\n" + 
				"			[7936, 0.02],\r\n" + 
				"			[7935, 1.3575],\r\n" + 
				"			[7933, 2.002],\r\n" + 
				"			[7932, 1.3449],\r\n" + 
				"			[7930, 10.2974],\r\n" + 
				"			[7929, 3.2226]\r\n" + 
				"		],\r\n" + 
				"		\"asks\": [\r\n" + 
				"			[7979, 0.0736],\r\n" + 
				"			[7980, 1.0292],\r\n" + 
				"			[7981, 5.5652],\r\n" + 
				"			[7986, 0.2416],\r\n" + 
				"			[7990, 1.9970],\r\n" + 
				"			[7995, 0.88],\r\n" + 
				"			[7996, 0.0212],\r\n" + 
				"			[8000, 9.2609],\r\n" + 
				"			[8002, 0.02],\r\n" + 
				"			[8008, 1],\r\n" + 
				"			[8010, 0.8735],\r\n" + 
				"			[8011, 2.36],\r\n" + 
				"			[8012, 0.02],\r\n" + 
				"			[8014, 0.1067],\r\n" + 
				"			[8015, 12.9118],\r\n" + 
				"			[8016, 2.5206],\r\n" + 
				"			[8017, 0.0166],\r\n" + 
				"			[8018, 1.3218],\r\n" + 
				"			[8019, 0.01],\r\n" + 
				"			[8020, 13.6584]\r\n" + 
				"		]\r\n" + 
				"	}\r\n" + 
				"}";
		
			HuobiMarketdepthDto huobiMarketdepthDto = JSONObject.parseObject(str,
				HuobiMarketdepthDto.class);
		
			//System.out.print(str);
			/*Map jsonMap = new HashMap();
			parseJSON2Map(jsonMap,checkFormat(str),null);
	        printJsonMap(jsonMap);*/
		  JSONObject object = huobiMarketdepthDto.getTick();	
		  JSONArray jsarr=object.getJSONArray("asks");
		  System.out.println("初始jsonObject:\n"+jsarr+"\n");
		  
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
		  
		 //System.out.println(object.get("asks").toString());
		/* try {
			 JSONObject object1=new JSONObject((Map<String, Object>) object.get("asks"));
			 JSONArray hobbies = object1.getJSONArray("hobbies");			 
			  for (int i = 0; i < hobbies.size(); i++) {
		            String s = (String) hobbies.get(i);
		            System.out.println(s);
		        }
		  
		  }catch(Exception ex) {
			  
		  }*/
      /* Iterator iterator = object.entrySet().iterator();
          while (iterator.hasNext()) {
              Map.Entry entry = (Map.Entry) iterator.next();
            //  System.out.print(entry.getKey()+"-----------------");
              //System.out.print(entry.getValue());
              if(entry.getKey().equals("asks")) {
            	    System.out.print(entry.getValue());
              }
          }
		*/
	}
	public static String checkFormat(String str){
        String _str = str.trim();
        if(_str.startsWith("[") && _str.endsWith("]")){
            return  _str.substring(1,_str.length()-1);
        }
        return  _str;
    }
	 public static void parseJSON2Map(Map jsonMap,String jsonStr,String parentKey){
	        //字符串转换成JSON对象
	        JSONObject json = JSONObject.parseObject(jsonStr);
	        //最外层JSON解析
	        for(Object k : json.keySet()){
	            //JSONObject 实际上相当于一个Map集合，所以我们可以通过Key值获取Value
	            Object v = json.get(k);
	            //构造一个包含上层keyName的完整keyName
	            String fullKey = (null == parentKey || parentKey.trim().equals("") ? k.toString() : parentKey + "." + k);

	            if(v instanceof JSONArray){
	                 //如果内层还是数组的话，继续解析
	                Iterator it = ((JSONArray) v).iterator();
	                while(it.hasNext()){
	                    JSONObject json2 = (JSONObject)it.next();
	                    parseJSON2Map(jsonMap,json2.toString(),fullKey);
	                }
	            } else if(isNested(v)){
	                parseJSON2Map(jsonMap,v.toString(),fullKey);
	            }
	            else{
	                jsonMap.put(fullKey, v);
	            }
	        }
	    }
	 
	 public static boolean isNested(Object jsonObj){

	        return jsonObj.toString().contains("{");
	    }
	 public static void println(Object str){
	        System.out.println(str);
	    }
	 public static void printJsonMap(Map map){
	        Set entrySet = map.entrySet();
	        Iterator<Map.Entry<String, Object>> it = entrySet.iterator();
	        //最外层提取
	        while(it.hasNext()){
	            Map.Entry<String, Object> e = it.next();
	            System.out.println("Key 值："+e.getKey()+"     Value 值："+e.getValue());
	        }
	    }
	 
	 public static Map<String, Object> getKeyAndValue(Object obj) {
	        Map<String, Object> map = new HashMap<String, Object>();
	        // 得到类对象
	        Class userCla = (Class) obj.getClass();
	        /* 得到类中的所有属性集合 */
	        Field[] fs = userCla.getDeclaredFields();
	        for (int i = 0; i < fs.length; i++) {
	            Field f = fs[i];
	            f.setAccessible(true); // 设置些属性是可以访问的
	            Object val = new Object();
	            try {
	                val = f.get(obj);
	                // 得到此属性的值
	                map.put(f.getName(), val);// 设置键值
	            } catch (IllegalArgumentException e) {
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }

	            /*
	             * String type = f.getType().toString();//得到此属性的类型 if
	             * (type.endsWith("String")) {
	             * System.out.println(f.getType()+"\t是String"); f.set(obj,"12") ;
	             * //给属性设值 }else if(type.endsWith("int") ||
	             * type.endsWith("Integer")){
	             * System.out.println(f.getType()+"\t是int"); f.set(obj,12) ; //给属性设值
	             * }else{ System.out.println(f.getType()+"\t"); }
	             */

	        }
	        System.out.println("单个对象的所有键值==反射==" + map.toString());
	        return map;
	    }
}
