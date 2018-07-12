package cn.huobi.framework.enums;

/**
 * 商户服务类型
 * 
 * @author junhu
 *
 */
public enum ServiceType {

	ONE(1, "POS刷卡-航空、加油、超市"), TWO(2, "POS刷卡-保险、公共事业"), THREE(3, "POS刷卡-房车类"), FOUR(4, "POS刷卡-一般类"), 
	FIVE(5, "POS刷卡-餐娱类"), SIX(6, "POS刷卡-民生类"), SEVEN(7, "POS刷卡-批发类"), EIGHT(8, "POS刷卡-其他"), 
	NINE(9, "支付宝扫码支付"), TEN(10, "微信扫码支付"), ELEVEN(11, "快捷支付"), TWELVE(12, "账户提现");

	private int value;
	private String text;

	private ServiceType(int value, String text) {
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

