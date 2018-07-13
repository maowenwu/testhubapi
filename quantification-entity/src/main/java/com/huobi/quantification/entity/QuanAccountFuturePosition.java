package com.huobi.quantification.entity;

import java.util.Date;

public class QuanAccountFuturePosition {
    /**
     * @mbg.generated 2018-07-13 17:56:26
     */
    private Long id;

    /**
     * 账号ID
     * @mbg.generated 2018-07-13 17:56:26
     */
    private Long accountSourceId;

    /**
     * @mbg.generated 2018-07-13 17:56:26
     */
    private Long queryId;

    /**
     * 币种
     * @mbg.generated 2018-07-13 17:56:26
     */
    private String respBody;

    /**
     * @mbg.generated 2018-07-13 17:56:26
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-07-13 17:56:26
     */
    private Date updateTime;

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

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public String getRespBody() {
        return respBody;
    }

    public void setRespBody(String respBody) {
        this.respBody = respBody;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}