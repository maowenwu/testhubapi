package com.huobi.contract.index.entity;

import java.util.Arrays;
import java.util.Date;

public class EmailResponse {
	
	private Long email_id;//  '邮件ID'
	private String email_from;//  '发件人'
	private String email_to;//  '收件人'
	private String email_cc;//'抄送'
	private  byte[] subject;// '标题'
	private  byte[]  content;// '邮件内容'
	private Date input_time;// '录入时间'
	private Date send_time;//'发送时间'
	private String status;// '发送状态:waiting-等待处理；success-成功；failure-失败'
	private int email_type;// '邮件类型:1:文本 2:HTML'
	private String error_msg;//'异常信息'
	public Long getEmail_id() {
		return email_id;
	}
	public void setEmail_id(Long email_id) {
		this.email_id = email_id;
	}
	public String getEmail_from() {
		return email_from;
	}
	public void setEmail_from(String email_from) {
		this.email_from = email_from;
	}
	public String getEmail_to() {
		return email_to;
	}
	public void setEmail_to(String email_to) {
		this.email_to = email_to;
	}
	public String getEmail_cc() {
		return email_cc;
	}
	public void setEmail_cc(String email_cc) {
		this.email_cc = email_cc;
	}
	public byte[] getSubject() {
		return subject;
	}
	public void setSubject(byte[] subject) {
		this.subject = subject;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public Date getInput_time() {
		return input_time;
	}
	public void setInput_time(Date input_time) {
		this.input_time = input_time;
	}
	public Date getSend_time() {
		return send_time;
	}
	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getEmail_type() {
		return email_type;
	}
	public void setEmail_type(int email_type) {
		this.email_type = email_type;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	@Override
	public String toString() {
		return "EmailResponse [email_id=" + email_id + ", email_from=" + email_from + ", email_to=" + email_to
				+ ", email_cc=" + email_cc + ", subject=" + Arrays.toString(subject) + ", content="
				+ Arrays.toString(content) + ", input_time=" + input_time + ", send_time=" + send_time + ", status="
				+ status + ", email_type=" + email_type + ", error_msg=" + error_msg + "]";
	}
	
	
	
    
}
