package com.huobi.contract.index.entity;

import java.util.ArrayList;

/**说明：电子邮件实体.  
 * @author LI TINGYU   
 * @version 0.1   
 */ 
public class EmailMessage {

	private long id;

	private String[] emails;

	private Integer  tempType;

	private ArrayList<String> params;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String[] getEmails() {
		return emails;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}
	
	public Integer getTempType() {
		return tempType;
	}

	public void setTempType(Integer tempType) {
		this.tempType = tempType;
	}

	public ArrayList<String> getParams() {
		return params;
	}

	public void setParams(ArrayList<String> params) {
		this.params = params;
	}

}
