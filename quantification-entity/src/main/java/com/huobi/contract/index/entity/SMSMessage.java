package com.huobi.contract.index.entity;

import java.util.ArrayList;

/**    
 * 说明：消息实体.
 * @author LI TINGYU   
 * @version 0.1   
 */ 

public class SMSMessage {

	private Long id;

	private ArrayList<Phone>  phones;

	private  Integer  tempType;

	private  ArrayList<String> params;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArrayList<Phone> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<Phone> phones) {
		this.phones = phones;
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
