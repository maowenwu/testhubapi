package cn.huobi.framework.model;

public class SpotJob {
	/**
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private Integer id;

	/**
	 * 交易所id
	 * 
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private String exchangeId;

	/**
	 * 任务类型
	 * 
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private Integer jobType;

	/**
	 * 任务名
	 * 
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private String jobName;

	/**
	 * 任务所需参数
	 * 
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private String jobParam;

	/**
	 * 任务描述
	 * 
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private String jobDesc;

	/**
	 * cron表达式
	 * 
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private String cron;

	/**
	 * 状态
	 * 
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private String state;

	/**
	 * 更新时间
	 * 
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private String updateDate;

	/**
	 * 创建时间
	 * 
	 * @mbg.generated 2018-07-27 14:41:01
	 */
	private String createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	public Integer getJobType() {
		return jobType;
	}

	public void setJobType(Integer jobType) {
		this.jobType = jobType;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobParam() {
		return jobParam;
	}

	public void setJobParam(String jobParam) {
		this.jobParam = jobParam;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
