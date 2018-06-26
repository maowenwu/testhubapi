package com.huobi.contract.index.common.util;

import com.huobi.contract.index.entity.HttpResponseEntity;
import com.huobi.contract.index.entity.ProxyConfig;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @desc
 * @Author mingjianyong
 */
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private CloseableHttpClient httpClient;
    private  HttpClientUtil(ProxyConfig proxyConfig){
        if(proxyConfig == null){
            //不需要代理
            httpClient = HttpClients.createDefault();
        } else if(proxyConfig.getUsername()==null){
            HttpHost proxyHost = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort());
            httpClient = HttpClients.custom()
                    .setProxy(proxyHost).build();
        } else{
            HttpHost proxyHost = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort());
            HttpClientBuilder build = HttpClients.custom().setDefaultRequestConfig(setRequest(proxyHost));
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            AuthScope authscope = new AuthScope(proxyHost);
            Credentials credentials = new UsernamePasswordCredentials(proxyConfig.getUsername(),
                    proxyConfig.getPassword());
            credentialsProvider.setCredentials(authscope, credentials);
            httpClient = build
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setProxy(proxyHost)
                    .build();
        }
    }

    public static HttpClientUtil getInstance(ProxyConfig proxyConfig){
        return new HttpClientUtil(proxyConfig);
    }
    private static RequestConfig setRequest(HttpHost proxy){
        return RequestConfig.custom().setConnectTimeout(Constant.RESTTEMPLATE_CONNECT_TIMEOUT)
                .setProxy(proxy)
                .setSocketTimeout(Constant.RESTTEMPLATE_READ_TIMEOUT).build();

    }
    public HttpResponseEntity get(String url) throws IOException {
        logger.debug("get start " +System.currentTimeMillis());
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
        httpGet.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        HttpResponseEntity responseEntity = null;
        try {
            long st = System.currentTimeMillis();
            logger.debug("get start sssss" +System.currentTimeMillis());

            CloseableHttpResponse response = httpClient.execute(httpGet);
            logger.debug("get start eeeee"+(System.currentTimeMillis()-st));
            responseEntity = new HttpResponseEntity();
           // responseEntity.setStatus(ValidEnum.FAIL.getStatus());
            if (response != null) {
                responseEntity.setStatusCode(response.getStatusLine().getStatusCode());
                responseEntity.setResponse(EntityUtils.toString(response.getEntity()));
              //  responseEntity.setStatus(ValidEnum.SUCC.getStatus());
            }
        } catch (Exception e) {
           // throw e;
        }finally {
            httpClient.close();
        }
        return responseEntity;
    }
}
