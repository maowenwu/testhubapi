package cn.huobi.framework.enums;

/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public enum FreezeStatus {

	/**
	 * 正常
	 * 
	 * */
	NORMAL("0"),

	/**
	 * 风控冻结
	 * 
	 * */
	RISKFREEZE("1"),
	/**
	 * 活动冻结
	 */
	HUDONGFREEZE("2"),
	
	/**
	 * 财务冻结
	 */
	CAIWUFREEZE("3");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private FreezeStatus(String _code) {
        this.code = _code;
    } 
    public static FreezeStatus getEnum(String _code) {
        if (_code != null) {
          for (FreezeStatus b : FreezeStatus.values()) {
            if (_code.equalsIgnoreCase(b.code)) {
              return b;
            }
          }
        }
        return null;
    }
}
