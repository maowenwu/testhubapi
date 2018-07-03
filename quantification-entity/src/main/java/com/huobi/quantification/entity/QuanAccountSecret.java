package com.huobi.quantification.entity;

public class QuanAccountSecret {
    /**
     * @mbg.generated 2018-07-03 10:29:58
     */
    private Long id;

    /**
     * @mbg.generated 2018-07-03 10:29:58
     */
    private Long accountSourceId;

    /**
     * @mbg.generated 2018-07-03 10:29:58
     */
    private String accessKey;

    /**
     * @mbg.generated 2018-07-03 10:29:58
     */
    private String secretKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountSourceId() {
        return accountSourceId;
    }

    public void setAccountSourceId(Long accountSourceId) {
        this.accountSourceId = accountSourceId;
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