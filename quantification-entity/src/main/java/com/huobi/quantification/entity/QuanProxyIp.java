package com.huobi.quantification.entity;

import java.util.Date;

public class QuanProxyIp {
    /**
     * @mbg.generated 2018-07-05 17:29:11
     */
    private Long id;

    /**
     * @mbg.generated 2018-07-05 17:29:11
     */
    private String host;

    /**
     * @mbg.generated 2018-07-05 17:29:11
     */
    private Integer port;

    /**
     * @mbg.generated 2018-07-05 17:29:11
     */
    private String userName;

    /**
     * @mbg.generated 2018-07-05 17:29:11
     */
    private String password;

    /**
     * @mbg.generated 2018-07-05 17:29:11
     */
    private Integer state;

    /**
     * @mbg.generated 2018-07-05 17:29:11
     */
    private Date updateTime;

    /**
     * @mbg.generated 2018-07-05 17:29:11
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}