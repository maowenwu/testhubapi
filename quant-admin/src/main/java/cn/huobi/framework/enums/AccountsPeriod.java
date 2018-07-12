package cn.huobi.framework.enums;

/**
 * 结算周期
 * 
 * @author junhu
 *
 */
public enum AccountsPeriod {

	ONE(1, "Ｔ+１"), TWO(2, "T+0");

	private int value;
	private String text;

	private AccountsPeriod(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
