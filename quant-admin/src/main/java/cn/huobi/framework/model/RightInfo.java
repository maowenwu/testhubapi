package cn.huobi.framework.model;

public class RightInfo {

	private Integer id;

	private Integer menuId;
	
    private String rightCode;

    private String rightName;

    private String rightComment;

    private Integer rightType;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
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
