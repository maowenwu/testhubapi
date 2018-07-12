package com.huobi.quantification.huobi.response;

/**
 * 
 * @author shaoxiaofeng
 * @since  2018/6/29
 * 
 */
public class Trade<T> {

    /**
     * id : 600848670
     * ts : 1489464451000
     * data : [{"id":600848670,"price":7962.62,"amount":0.0122,"direction":"buy","ts":1489464451000}]
     */

    private int id;
    private long ts;
    private T data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
