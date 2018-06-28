package com.huobi.quantification.common.api;

import com.huobi.quantification.common.util.MD5;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OkSignature {

    private String apiKey = "d880067d-65b7-4bcb-b366-4cea186001a7";
    private String secretKey = "F6D5CF92E40A0FA178BDF05FB9801BED";

    public OkSignature() {
    }

    public OkSignature(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    public Map<String, String> sign(Map<String, String> params) {
        params.put("api_key", apiKey);
        params.put("sign", getSign(params));
        return params;
    }


    private String getSign(Map<String, String> params) {
        List<String> parameters = params.entrySet().stream()
                .map((e) -> e.getKey() + "=" + e.getValue())
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        parameters.add("secret_key=" + secretKey);
        String queryParams = String.join("&", parameters);
        return MD5.hash(queryParams);
    }
}
