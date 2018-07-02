package com.huobi.quantification.entity;

public class QuanAccountFutureSecret {
    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private Long id;

    /**
     * @mbg.generated 2018-07-02 14:32:37
     */
    private Long accountFutureId;

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

    public Long getAccountFutureId() {
        return accountFutureId;
    }

    public void setAccountFutureId(Long accountFutureId) {
        this.accountFutureId = accountFutureId;
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