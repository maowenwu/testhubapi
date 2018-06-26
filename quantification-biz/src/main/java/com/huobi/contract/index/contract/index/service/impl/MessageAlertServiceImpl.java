package com.huobi.contract.index.contract.index.service.impl;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huobi.contract.index.contract.index.service.IndexMesssageAlertService;
import com.huobi.contract.index.dao.SendHistoryMapper;
import com.huobi.contract.index.entity.EmailMessage;
import com.huobi.contract.index.entity.EmailRequest;
import com.huobi.contract.index.entity.EmailResponse;
import com.huobi.contract.index.entity.SmsRequest;
import com.huobi.contract.index.entity.SmsResponse;
import com.huobi.contract.index.message.alert.SmsCallBackSender;


@Service("messageAlertService")
public class MessageAlertServiceImpl implements IndexMesssageAlertService{
	private static final Logger LOG = LoggerFactory.getLogger(MessageAlertServiceImpl.class);


	@Autowired
	private SendHistoryMapper sendhistorymapper;

	@Autowired
	private SmsCallBackSender callBackSender; 

	@Autowired
	private AmqpTemplate rabbitTemplate; 

	@Override
	public void smsRetryTask() {
		// TODO Auto-generated method stub

		SmsRequest smsrequest = new SmsRequest();
		smsrequest.setStatus("retry");
		
		Map<Long, SmsResponse> response = sendhistorymapper.getSmsInfo(smsrequest);
		if (response == null) {
			return ;
		}
		
		for (Map.Entry<Long, SmsResponse> entry : response.entrySet()) {
			LOG.info("FOR=SmsRetry=Entry: {}: {}",entry.getKey(),entry.getValue()); 
			Long id = entry.getKey();
			SmsResponse res =  entry.getValue();
			if (res == null) {
				LOG.info("id :{} ,SmsRetry ,SmsResponse is null",id);
				return ;
			}
			callBackSender.send(id, res);  
		}

	}
	
	@Override
	public void smsWaitingTask() {
		// TODO Auto-generated method stub

		SmsRequest smsrequest = new SmsRequest();
		smsrequest.setStatus("waiting");

		Map<Long, SmsResponse> response = sendhistorymapper.getSmsInfo(smsrequest);

		if (response == null) {
			return ;
		}

		for (Map.Entry<Long, SmsResponse> entry : response.entrySet()) {
			LOG.info("FOR=SmsWaiting=Entry: {}: {}",entry.getKey(),entry.getValue()); 
			Long id = entry.getKey();
			SmsResponse res =  entry.getValue();

			if (res == null) {
				LOG.info("id :{} ,SmsWaiting ,SmsResponse is null",id);
				return ;
			}
			callBackSender.send(id, res); 
		}

	}

	@Override
	public void emailTask() {
		// TODO Auto-generated method stub

		EmailRequest emailrequest = new EmailRequest();
		emailrequest.setStatus("waiting");
		Map<Long, EmailResponse> response = sendhistorymapper.getEmailInfo(emailrequest);

		if (response == null) {
			return ;
		}

		for (Map.Entry<Long, EmailResponse> entry : response.entrySet()){

			try {
				Long id = entry.getKey();
				EmailResponse res =  entry.getValue();

				byte[] contentbyte = res.getContent() ;

				if (contentbyte.length == 0) {
					LOG.info("id :{} ,contentbyte.length is 0",id);
					return ;
				}

				String content = new String(contentbyte);

				LOG.info("FOR=email=Entry :{} , content:{}, entry :{}",entry.getKey() ,content,entry.getValue()); 

				Long email_id  = res.getEmail_id();
				String email_to = res.getEmail_to();
				int email_type = res.getEmail_type();

				if (email_to == null ) {
					LOG.info("id :{} ,email_to is null ",id); 
					return ;
				}

				String[] contents = content.split("\\|");
				final String[] emails = email_to.split("\\|");
				ArrayList<String> params = new ArrayList<String>();
				for(String param:contents) {
					params.add(param);
				}

				EmailMessage emailmessage = new EmailMessage();
				emailmessage.setId(email_id);
				emailmessage.setTempType(email_type);
				emailmessage.setEmails(emails);
				emailmessage.setParams(params);

				Gson gson = new GsonBuilder().serializeNulls().create();
				String jsonStr = gson.toJson(emailmessage, EmailMessage.class);

				LOG.info("jsonStr: {}", jsonStr);

				this.rabbitTemplate.convertAndSend("helloQueue", jsonStr); 

				EmailRequest res2 = new EmailRequest();
				res2.setEmailId(id);
				res2.setStatus("success");
				sendhistorymapper.setEmailStatus(res2); 

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.error("error:{}", e.getMessage());
			} 
		}
	}
}


