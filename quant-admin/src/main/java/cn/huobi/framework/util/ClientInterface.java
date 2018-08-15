package cn.huobi.framework.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;

public class ClientInterface {
	private String host;
	private Map<String,String> params;
	private Map<String,String> headers;
	private CloseableHttpClient client;
	private static final DateFormat fmt=new SimpleDateFormat("YYYY-MM-dd");
	private static final DecimalFormat format = new java.text.DecimalFormat("0.00");
	private static final Logger log = LoggerFactory.getLogger(ClientInterface.class);
	public ClientInterface(String host,Map<String,String> headers,Map<String,String> params){
		this.host=host;
		this.params=params;
		this.headers=headers;
		this.client=HttpClients.createDefault();
	}
	
	public ClientInterface(String host,Map<String,String> params){
		this(host,null,params);
	}
	public String postRequest(){
		try {
			HttpPost post=new HttpPost(host);
			if(headers!=null)
				post.setHeaders(setHeaders());
			post.setEntity(setParams());
			HttpResponse res=client.execute(post);
			if(res.getStatusLine().getStatusCode()==200){
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

//    private HttpEntity setParams1() throws UnsupportedEncodingException {
//        if(params!=null){
//            StringBuilder sb = new StringBuilder();
//            for(String key:params.keySet()){
//                sb.append("key=" + URLEncoder.encode(params.get(key), "utf8") + "&");
//            }
//            if (sb.length() > 0){
//                sb.deleteCharAt(sb.length() - 1);
//            }
//            return new StringEntity(sb.toString());
//        }
//        return null;
//    }

    public String postRequestBody(String param){
		try {
			log.info("请求参数："+param);
			HttpPost post=new HttpPost(host);
			if(headers!=null)
				post.setHeaders(setHeaders());
			post.setEntity(new StringEntity(param, "utf-8"));
			HttpResponse res=client.execute(post);
			if(res.getStatusLine().getStatusCode()==200){
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public String getRequest(){
		try {
			HttpGet get=new HttpGet(host+"?"+EntityUtils.toString(setParams(),"ISO-8859-1"));
			if(headers!=null)
				get.setHeaders(setHeaders());
			HttpResponse res=client.execute(get);
			if(res.getStatusLine().getStatusCode()==200){
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public UrlEncodedFormEntity setParams() throws UnsupportedEncodingException{
		if(params!=null){
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			for(String key:params.keySet()){
				list.add(new BasicNameValuePair(key,params.get(key)));
			}
			return new UrlEncodedFormEntity(list);
		}
		return null;
	}
	
	public Header[] setHeaders() throws UnsupportedEncodingException{
		if(headers!=null){
			Header[] headerArray=new Header[headers.size()];
			int i=0;
			for(String key:headers.keySet()){
				headerArray[i]=new BasicHeader(key,params.get(key));
				i++;
			}
			return headerArray;
		}
		return null;
	}
	
	public static void main(String[] args) {
//		checkOrderProfit("100");
//		String batchNo = "1000";
//		String url = "http://192.168.8.49:2111/superbank-mobile/order/checkOrderProfit";
		String url = "http://192.168.0.200:8020/repay/oemCreate";
//		String sign = Md5.md5Str(batchNo + "&" + Constants.SUPER_BANK_SECRET);
		Map<String,String> map = new HashMap<String,String>();
		map.put("oem_name", "123");
//		map.put("sign", sign);
		String str = httpPost(url,map);
		System.out.println(str);
//		Map<String,String> params=new HashMap<>();
//		params.put("baseInfo", "{'serviceName':'','serviceType':'','rateCard':'','rateHolidays':'','quotaHolidays':'','quotaCard':'','fixedRate':'','fixedQuota':'','rateCheckStatus':'','rateLockStatus':'','quotaCheckStatus':'','quotaLockStatus':''}");
//		params.put("pageNo", "1");
//		params.put("pageSize", "10");
//		System.out.println(new ClientInterface("http://192.168.1.160:8088/boss2/service/queryServiceList", params).postRequest());

	}

	public static String baseClient(String host,Map<String,Object> map){
		final long iat = System.currentTimeMillis() / 1000l; 
		map.put("exp", iat+300L);
		map.put("iat", iat);
		map.put("jti",UUID.randomUUID().toString());
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner("123456").sign(map));
		log.info("请求路径：{},参数：{}",host, JSONObject.toJSONString(map));
		String returnStr = new ClientInterface(host, params).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	public static String baseClientBySecret(String host,Map<String,Object> map, String secret){
		final long iat = System.currentTimeMillis() / 1000l; 
		map.put("exp", iat+300L);
		map.put("iat", iat);
		map.put("jti",UUID.randomUUID().toString());
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(secret).sign(map));
		log.info("请求路径：{},参数：{}",host, JSONObject.toJSONString(map));
		String returnStr = new ClientInterface(host, params).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	public static String baseNoClient(String host,Map<String,String> map){
		return new ClientInterface(host, map).postRequest();
	}
	
	public static String postRequest(String url){
		Map<String,Object> params=new HashMap<>();
		return baseClient(url, params);
	}

	/**公共httpPost请求方法*/
	@SuppressWarnings("rawtypes")
	private static String httpPost(String url,Map<String,String> map){
		String jsonStr = "";
		CloseableHttpClient httpclient = null;
		HttpPost httppost = null;
		try{
			httpclient = HttpClientBuilder.create().build();
			httppost = new HttpPost(url);
			//配置请求的超时设置
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(6000)
					.setConnectTimeout(6000)
					.setSocketTimeout(6000).build();
			httppost.setConfig(requestConfig);
			if(null != map){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					params.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
				}
				httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}
			CloseableHttpResponse response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity rs = response.getEntity();
				jsonStr = EntityUtils.toString(rs);
			}
			httppost.releaseConnection();
			httpclient.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(null != httppost){
					httppost.releaseConnection();
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
			try{
				if(null != httpclient){
					httpclient.close();
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		return jsonStr;
	}

	/**公共httpPost请求方法*/
	private static String httpPost2(String url,Map<String, String> map){
		String jsonStr = "";
		CloseableHttpClient httpclient = null;
		HttpPost httppost = null;
		try{
			httpclient = HttpClientBuilder.create().build();
			httppost = new HttpPost(url);
			//配置请求的超时设置
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(2000)
					.setConnectTimeout(2000)
					.setSocketTimeout(2000).build();
			httppost.setConfig(requestConfig);
			httppost.setHeader("Content-Type","application/json");

			StringEntity s = new StringEntity(JSONObject.toJSONString(map));
			s.setContentEncoding("UTF-8");
			httppost.setEntity(s);

			CloseableHttpResponse response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity rs = response.getEntity();
				jsonStr = EntityUtils.toString(rs);
			}
			httppost.releaseConnection();
			httpclient.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(null != httppost){
					httppost.releaseConnection();
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
			try{
				if(null != httpclient){
					httpclient.close();
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		return jsonStr;
	}

}
