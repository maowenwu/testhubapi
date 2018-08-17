package com.huobi.quantification.entity;

public class SysDict {
    /**
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Long id;

    /**
     * 标识字典类型
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String sysKey;

    /**
     * 名称
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String sysName;

    /**
     * 值
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String sysValue;

    /**
     * 上级ID
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String parentId;

    /**
     * 排序
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Long orderNo;

    /**
     * 状态
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Integer status;

    /**
     * 描述信息
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysValue() {
        return sysValue;
    }

    public void setSysValue(String sysValue) {
        this.sysValue = sysValue;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}