package cn.huobi.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */

public enum MenuType {
	/**
	 * 菜单类型
	 */
	MENU("menu"),
	/**
	 * 页面类型
	 */
	PAGE("page");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private MenuType(String _code) {
        this.code = _code;
    }
}
