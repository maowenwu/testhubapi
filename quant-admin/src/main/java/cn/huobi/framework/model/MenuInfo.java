package cn.huobi.framework.model;

import java.io.Serializable;
import java.util.List;

public class MenuInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	 private Integer id;
	 private Integer parentId;
	 private String menuCode;
	 private String menuName;
	 private String menuUrl;
	 private String rightCode;
	 private Integer menuLevel;
	 private String menuType;
	 private Integer orderNo;
	 private String parentName;
	 private List<MenuInfo> children;
	 
	 private Integer logFlag;
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	
	public String getRightCode() {
		return rightCode;
	}
	public void setRightCode(String rightCode) {
		this.rightCode = rightCode;
	}
	public Integer getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public List<MenuInfo> getChildren() {
		return children;
	}
	public void setChildren(List<MenuInfo> children) {
		this.children = children;
	}
	public Integer getLogFlag() {
		return logFlag;
	}
	public void setLogFlag(Integer logFlag) {
		this.logFlag = logFlag;
	}
	
	
}
