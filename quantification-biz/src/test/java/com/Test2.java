package com;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test2 {

	public static void main(String[] args) {    
	    String json="{\"name\":\"刘德华\",\"age\":35,\"some\":[{\"k1\":\"v1\",\"k2\":\"v2\"},{\"k3\":\"v3\",\"k4\":\"v4\"}]}";
	    JSONObject jso=JSON.parseObject(json);//json字符串转换成jsonobject对象
	    System.out.println("初始jsonObject:\n"+jso+"\n");
	    JSONArray jsarr=jso.getJSONArray("some");//jsonobject对象取得some对应的jsonarray数组
	    System.out.println("jsonObject里面的jsonarray:\n"+jsarr+"\n");
	   JSONObject ao=jsarr.getJSONObject(0);//jsonarray对象通过getjsonobjext(index)方法取得数组里面的jsonobject对象
	   System.out.println("jsonObject里面的jsonarray里面的第一个jsonobject：\n"+ao+"\n");
	   String vString=ao.getString("k1");//jsonobject对象通过key直接取得String的值
	   System.out.println("jsonObject里面的jsonarray里面的第一个jsonobject里的键值对对k1取值：\n"+vString+"\n");
	}
}
