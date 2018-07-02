package com.huobi.quantification.entity;

public class QuanAccountSecret {
    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private Long id;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private Long accountId;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private String accessKey;

    /**
     * @mbg.generated 2018-07-02 14:32:37
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