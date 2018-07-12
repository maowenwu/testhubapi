package cn.huobi.framework.db.pagination;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.session.RowBounds;

/**
 * Mybatis分页参数及查询结果封装. 注意所有序号从1开始.
 * 
 * @param <T>
 *            Page中记录的类型.
 * @author 沙
 * @param <T>
 */
public class Page<T> extends RowBounds {
    // --分页参数 --//
    /**
     * 页编号 : 第几页
     */
    public int pageNo = 1;
    /**
     * 页大小 : 每页的数量
     */
    public int pageSize = 10;

   
    /**
     * 查询结果
     */
    protected List<T> result = new ArrayList<T>();

    /**
     * 总条数
     */
    protected int totalCount;

    /**
     * 总页数
     */
    protected int totalPages;

    // --计算 数据库 查询的参数 : LIMIT 3, 3; LIMIT offset, limit; --//
    /**
     * 计算偏移量
     */
    
    // -- 构造函数 --//
    public Page() {
    }

    public Page(int pageNo) {
    	if(pageNo>0)
    		this.pageNo = pageNo;
    }
    public Page(int pageNo, int pageSize) {
    	if(pageNo>0)
    		this.pageNo = pageNo;
    	if(pageSize>0)
    		this.pageSize = pageSize;
    }

    
    /**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(int pageNo) {
		if(pageNo>0)
			this.pageNo = pageNo;
	}

	// -- 访问查询参数函数 --//
    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     */
    public int getPageNo() {
        return pageNo;
    }
 
    /**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		if(pageNo>0)
			this.pageSize = pageSize;
	}

	/**
     * 获得每页的记录数量,默认为1.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
     */
    public int getFirst() {
        return ((pageNo - 1) * pageSize);
    }
 
    // -- 访问查询结果函数 --//
    /**
     * 取得页内的记录列表.
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 设置页内的记录列表.
     */
    public void setResult(final List<T> result) {
    	if(result!=null)
    		this.result = result;
    }

    /**
     * 取得总记录数, 默认值为-1.
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数.
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        this.totalPages = this.getTotalPages();
    }

    /**
     * 根据pageSize与totalCount计算总页数, 默认值为-1.
     */
    public int getTotalPages() {
    	if(pageSize<1){
    		pageSize=10;
    	}
        if (totalCount < 0) {
            return -1;
        }
        int pages = totalCount / pageSize;
        return totalCount % pageSize > 0 ? ++pages : pages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
