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

    String okSignedPost(String url, Map<String, String> params) throws HttpRequestException;
}
