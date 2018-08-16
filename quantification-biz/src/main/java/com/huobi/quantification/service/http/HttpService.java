package com.huobi.quantification.service.http;

import com.huobi.quantification.common.exception.HttpRequestException;

import java.util.Map;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface HttpService {

    String doGet(String url) throws HttpRequestException;

    String doGet(String url, Map<String, String> params) throws HttpRequestException;

    String doPost(String url, Map<String, String> params) throws HttpRequestException;

    String doPostJson(String url, Map<String, String> params) throws HttpRequestException;

    String doHuobiFuturePostJson(Long accountId, String url, Map<String, String> params) throws HttpRequestException;

    String doOkFuturePost(Long accountId, String url, Map<String, String> params) throws HttpRequestException;

    String doHuobiSpotGet(Long accountId, String uri, Map<String, String> params) throws HttpRequestException;
    
    String doHuobiSpotPost(Long accountId, String uri , Object object) throws HttpRequestException;
}
