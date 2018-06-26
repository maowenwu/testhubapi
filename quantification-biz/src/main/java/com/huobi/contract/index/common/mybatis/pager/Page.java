package com.huobi.contract.index.common.mybatis.pager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对分页的基本数据进行一个简单的封装
 */
public class Page<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6436330541983205290L;
	private int pageNo = 1;// 页码，默认是第一页
	private int pageSize = 10;// 每页显示的记录数，默认是15
	private int start = 0;//预留字段
	private int limit = 10;//预留字段
	private Integer page = 1;//新版datagrid用到的页码字段
	private Integer rows = 10;//新版datagrid用到的每页总数字段
	private int total;// 总记录数
	private int totalPage;// 总页数
	private List<T> datas;// 对应的当前页记录
	private Map<String, Object> params = new HashMap<String, Object>();// 其他的参数我们把它分装成一个Map对象
	private String sortName; // 新版datagrid用到的自定义排序字段
	private String order; // 新版datagrid用到的升序或降序字段
	private String sort;//预留字段
	private String dir;//预留字段
	private String assembleOrderBy;//组合排序字段,会加 order dir 后
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	public int getPageNo() {
		return null != page ? page : this.getStart() / this.getLimit() + 1;
	}

	public int getPageSize() {
		return null != rows ? rows : this.getLimit();
	}

	/*public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}*/

	public int getTotal() {
		return total;
	}

	public void setTotal(int totalRecord) {
		this.total = totalRecord;
		// 在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
		int totalPage = totalRecord % rows == 0 ? totalRecord / rows
				: totalRecord / rows + 1;
		this.setTotalPage(totalPage);
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public Map<String, Object> getParams() {
		return params;
	}
	
	public void addParam(String key, String value){
		params.put(key, value);
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
	
	public String getAssembleOrderBy() {
		return assembleOrderBy;
	}

	public void setAssembleOrderBy(String assembleOrderBy) {
		this.assembleOrderBy = assembleOrderBy;
	}

	@Override
	public String toString() {
		return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize + ", start="
				+ start + ", limit=" + limit + ", page=" + page + ", rows="
				+ rows + ", total=" + total + ", totalPage=" + totalPage
				+ ", datas=" + datas + ", params=" + params + ", sortName="
				+ sortName + ", order=" + order + ", sort=" + sort + ", dir="
				+ dir + ", assembleOrderBy=" + assembleOrderBy + "]";
	}
	
}