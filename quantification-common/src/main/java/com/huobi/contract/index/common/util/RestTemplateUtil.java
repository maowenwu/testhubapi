package com.huobi.contract.index.common.util;

import com.huobi.contract.index.entity.ProxyConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class RestTemplateUtil {
	private static  RestTemplate restTemplate = null;

//	private static class SingletonRestTemplate {
//		/**
//		 * 单例对象实例
//		 */
//		static final RestTemplateUtil INSTANCE = new RestTemplateUtil(ProxyConfig proxyConfig);
//	}

	private RestTemplateUtil(ProxyConfig proxyConfig) {
		SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(Constant.RESTTEMPLATE_CONNECT_TIMEOUT);
		httpRequestFactory.setReadTimeout(Constant.RESTTEMPLATE_READ_TIMEOUT);
		if(proxyConfig!=null){
			SocketAddress address = new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort());
			Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
			httpRequestFactory.setProxy(proxy);
		}
	}

	/**
	 * 单例实例
	 */
	public static RestTemplateUtil getInstance(ProxyConfig proxyConfig) {
		return new RestTemplateUtil(proxyConfig);
	}

	public String get(String url) {
		return restTemplate.getForObject(url, String.class, new Object[] {});

	}

	public String getForHeader(String url){
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
		requestHeaders.add("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		return response.getBody();
	}

	/**
	 * post提交对象
	 */
	public String post(String url, String data) {
		return restTemplate.postForObject(url, null, String.class, data);
	}

	public static void main(String[] args) {
		//System.out.println(RestTemplateUtil.getInstance().get("http://hao123.com"));
	}
}
