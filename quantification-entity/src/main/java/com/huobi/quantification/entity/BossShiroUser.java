package com.huobi.quantification.entity;

import java.util.Date;

public class BossShiroUser {
    /**
     * 主键ID
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Integer id;

    /**
     * 用户唯一标识
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String userName;

    /**
     * 用户编码
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String password;

    /**
     * 真实姓名
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String realName;

    /**
     * 电话
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String telNo;

    /**
     * 邮箱
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String email;

    /**
     * 用户状态，0-无效，1-有效
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Integer status;

    /**
     * 用户样式主题
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String theme;

    /**
     * 创建人
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String createOperator;

    /**
     * 创建时间
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Date createTime;

    /**
     * 修改密码时间
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Date updatePwdTime;

    /**
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Integer deptId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdatePwdTime() {
        return updatePwdTime;
    }

    public void setUpdatePwdTime(Date updatePwdTime) {
        this.updatePwdTime = updatePwdTime;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}