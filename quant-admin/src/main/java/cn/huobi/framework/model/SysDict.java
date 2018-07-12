package cn.huobi.framework.model;

/**
 * table sys_dict desc 数字字典
 * 
 * @author thj
 *
 */
public class SysDict {
	private Long id;

	private String sysKey;

	private String sysName;

	private String sysValue;

	private String parentId;

	private Long orderNo;

	private Integer status;

	private String remark;
	
	private String type;

	//liuks 用于公告选中标识传输(后台逻辑判断)
	private String checkedState;

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
		this.sysKey = sysKey == null ? null : sysKey.trim();
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName == null ? null : sysName.trim();
	}

	public String getSysValue() {
		return sysValue;
	}

	public void setSysValue(String sysValue) {
		this.sysValue = sysValue == null ? null : sysValue.trim();
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId == null ? null : parentId.trim();
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
		this.remark = remark == null ? null : remark.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCheckedState() {
		return checkedState;
	}

	public void setCheckedState(String checkedState) {
		this.checkedState = checkedState;
	}
}