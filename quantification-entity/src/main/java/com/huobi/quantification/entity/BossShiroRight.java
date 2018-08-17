package com.huobi.quantification.entity;

public class BossShiroRight {
    /**
     * 主键ID
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Integer id;

    /**
     * 权限编码
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String rightCode;

    /**
     * 权限名称
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String rightName;

    /**
     * 权限说明
     * @mbg.generated 2018-08-17 10:33:01
     */
    private String rightComment;

    /**
     * 权限类型
     * @mbg.generated 2018-08-17 10:33:01
     */
    private Integer rightType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRightCode() {
        return rightCode;
    }

    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public String getRightComment() {
        return rightComment;
    }

    public void setRightComment(String rightComment) {
        this.rightComment = rightComment;
    }

    public Integer getRightType() {
        return rightType;
    }

    public void setRightType(Integer rightType) {
        this.rightType = rightType;
    }
}