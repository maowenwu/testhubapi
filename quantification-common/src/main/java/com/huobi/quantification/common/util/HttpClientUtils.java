package com.huobi.quantification.common.util;

import com.huobi.quantification.common.constant.HttpConstant;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClientUtils {

    private CloseableHttpClient httpClient;

    public static HttpClientUtils getInstance(ProxyConfig proxyConfig){
        return new HttpClientUtils(proxyConfig);
    }

    private HttpClientUtils(ProxyConfig proxyConfig){
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



    private static RequestConfig setRequest(HttpHost proxy){
        return RequestConfig.custom()
                .setConnectTimeout(HttpConstant.RESTTEMPLATE_CONNECT_TIMEOUT)
                .setProxy(proxy)
                .setSocketTimeout(HttpConstant.RESTTEMPLATE_READ_TIMEOUT).build();

    }

}
