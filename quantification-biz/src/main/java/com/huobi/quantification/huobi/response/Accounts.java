package com.huobi.quantification.huobi.response;

/**
 * 
 * @author shaoxiaofeng
 * @since  2018/6/29
 * 
 */
public class Accounts {
    /**
     * id : 100009
     * type : spot
     * state : working
     * user-id : 1000
     */

    private int id;
    private String type;
    private String state;
    private int userid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
