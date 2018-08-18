package com.huobi.quantification.entity;

public class QuanAccountSecret {
    /**
     * @mbg.generated 2018-08-18 15:39:57
     */
    private Long id;

    /**
     * 用户id
     * @mbg.generated 2018-08-18 15:39:57
     */
    private Long accountId;

    /**
     * access_key
     * @mbg.generated 2018-08-18 15:39:57
     */
    private String accessKey;

    /**
     * secret_key
     * @mbg.generated 2018-08-18 15:39:57
     */
    private String secretKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}