package com.huobi.contract.index.entity;

import java.util.Date;

public class SmsResponse {

	private long sms_id; // 'SMS标识',
	private String client_id; // '客户端标识',
	private String destination; //  '目的地址',
	private String content; //  '短信内容',
	private String message_id; //  '短信编号',
	private Date last_send_time; // '最后发送时间',
	private int deal_times; //  '处理次数',
	private String status; //  '发送状态 :waiting-等待发送 sending-正在发送 retry-需要重试 success-发送成功 failure-发送失败',
	private Date input_time; // '录入时间',
	private Date expected_send_time; //  '预期发送时间',
	private String error_code; // '异常编码',
	private String error_msg; //  '异常信息'
	
	public long getSms_id() {
		return sms_id;
	}
	public void setSms_id(long sms_id) {
		this.sms_id = sms_id;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public Date getLast_send_time() {
		return last_send_time;
	}
	public void setLast_send_time(Date last_send_time) {
		this.last_send_time = last_send_time;
	}
	public int getDeal_times() {
		return deal_times;
	}
	public void setDeal_times(int deal_times) {
		this.deal_times = deal_times;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getInput_time() {
		return input_time;
	}
	public void setInput_time(Date input_time) {
		this.input_time = input_time;
	}
	public Date getExpected_send_time() {
		return expected_send_time;
	}
	public void setExpected_send_time(Date expected_send_time) {
		this.expected_send_time = expected_send_time;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	@Override
	public String toString() {
		return "SmsResponse [sms_id=" + sms_id + ", client_id=" + client_id + ", destination=" + destination
				+ ", content=" + content + ", message_id=" + message_id + ", last_send_time=" + last_send_time
				+ ", deal_times=" + deal_times + ", status=" + status + ", input_time=" + input_time
				+ ", expected_send_time=" + expected_send_time + ", error_code=" + error_code + ", error_msg="
				+ error_msg + "]";
	}

	

}
