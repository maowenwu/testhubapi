package com.huobi.contract.index.facade.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果对象
 * @see #total
 * @see #data
 * @param <T>
 */
public class PageResult<T> implements Serializable {


    private static final long serialVersionUID = 1758582908670065095L;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 返回的结果对象
     */
    private List<T> data;

    public PageResult() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
