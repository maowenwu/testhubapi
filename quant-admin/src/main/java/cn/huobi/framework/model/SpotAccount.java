package cn.huobi.framework.model;

public class SpotAccount {
	private Long id;
	private Integer exchangeId;
	private Long accountSourceId;
	private String accountsType;
	private String accountsName;
	private String state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(Integer exchangeId) {
		this.exchangeId = exchangeId;
	}
	public Long getAccountSourceId() {
		return accountSourceId;
	}
	public void setAccountSourceId(Long accountSourceId) {
		this.accountSourceId = accountSourceId;
	}
	public String getAccountsType() {
		return accountsType;
	}
	public void setAccountsType(String accountsType) {
		this.accountsType = accountsType;
	}
	public String getAccountsName() {
		return accountsName;
	}
	public void setAccountsName(String accountsName) {
		this.accountsName = accountsName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
