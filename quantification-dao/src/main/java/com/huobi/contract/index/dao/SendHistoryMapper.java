package com.huobi.contract.index.dao;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.huobi.contract.index.entity.EmailRequest;
import com.huobi.contract.index.entity.EmailResponse;
import com.huobi.contract.index.entity.SmsRequest;
import com.huobi.contract.index.entity.SmsResponse;


@Mapper
public interface SendHistoryMapper {

	/**
	 * 获取Email的信息
	 */
	@MapKey("email_id")
	public Map<Long, EmailResponse> getEmailInfo(EmailRequest request); 

	/**
	 * 获取sms的信息
	 */
	@MapKey("sms_id")
	public Map<Long, SmsResponse> getSmsInfo(SmsRequest request); 
	
	/**
	 * 设置email的status的状态
	 */
	public int  setEmailStatus(EmailRequest request); 

	/**
	 * 设置sms的status的状态
	 */
	public int  setSmsStatus(SmsRequest request); 

	/**
	 * 回调设置sms的status的状态
	 */
	public int  setSmsStatusBack(SmsRequest request); 

}


