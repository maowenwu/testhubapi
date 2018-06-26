package com.huobi.contract.index.message.alert;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huobi.contract.index.dao.SendHistoryMapper;
import com.huobi.contract.index.entity.Phone;
import com.huobi.contract.index.entity.SMSMessage;
import com.huobi.contract.index.entity.SmsRequest;
import com.huobi.contract.index.entity.SmsResponse;


@Component
public class SmsCallBackSender implements  RabbitTemplate.ConfirmCallback{

	Logger LOG = LoggerFactory.getLogger( SmsCallBackSender.class );

	@Autowired
	private RabbitTemplate rabbitTemplatenew;

	@Autowired
	private SendHistoryMapper sendhistorymapper;

	/**
	 * 方法描述：发送短信到rabbitmq.
	 */
	public void send(Long id, SmsResponse smsresponse) {

		try {
			rabbitTemplatenew.setConfirmCallback(this);
			SmsResponse res = smsresponse;
			
			long sms_id = res.getSms_id();
			
			// 手机号码，只有一个，可以带国家号码(用|分割开来)
			String destination = res.getDestination();
			
			// 模板参数信息
			String content = res.getContent();
			
			// 模板类型 
			String messge_id = res.getMessage_id();
			
			if (destination == null || content == null || messge_id == null){
				LOG.error("sms_id:{} , destination or content or messge_id is null.",sms_id);
				return ;
			}
			
			String[] tel = null;
			if (destination.indexOf("\\|") != -1){
				tel = destination.split("\\|");
			}else {
				tel = new String[2];
				tel[0] = "";
				tel[1] = destination;
			}
			
			Phone phone = new Phone();
			phone.setMobile(tel[1]); 
			phone.setNationCode(tel[0]); 

			ArrayList<Phone> phones = new ArrayList<Phone>();
			phones.add(phone);
			
			if (content.indexOf("|") == -1){
				LOG.error("sms_id:{} , 模板参信息不符合规范,  destination:{}",destination);
				return ;
			}
			
			String[] contents = content.split("\\|");
			ArrayList<String> params = new ArrayList<String>();
			for(String param:contents) {
				params.add(param);
			}
			
			int type = Integer.parseInt(messge_id);
			
			SMSMessage smsmessage = new SMSMessage();
			smsmessage.setId(sms_id);
			smsmessage.setTempType(type);
			smsmessage.setPhones(phones);
			smsmessage.setParams(params);
			
			Gson gson = new GsonBuilder().serializeNulls().create(); 
			String jsonStr = gson.toJson(smsmessage, SMSMessage.class);

			//这里根据表的id来设置id 
			CorrelationData correlationData = new CorrelationData(sms_id + ""); 
			
			LOG.info("callbackSender id: {} , jsonstr: " ,correlationData.getId(), jsonStr.toString());  
			
			this.rabbitTemplatenew.convertAndSend("exchange", "topic.messages", jsonStr, correlationData);  
			
			int dealTimes = smsresponse.getDeal_times();
			SmsRequest req = new SmsRequest(); 
			req.setSmsId(id);
			req.setDealTimes(++dealTimes);
			if (dealTimes < 4){
				req.setStatus("retry");
			}else {
				req.setStatus("failure");
			}
			
			sendhistorymapper.setSmsStatus(req);
			
		} catch (NumberFormatException e){
			
			LOG.error("error :{}",e.getMessage());
			e.printStackTrace();
			
		} catch (AmqpException e){
			
			LOG.error("error :{}",e.getMessage());
			e.printStackTrace();
		}
	}   
	
	/**
	 * 回调确认.
	 */
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		// TODO Auto-generated method stub  
		String id  = correlationData.getId();
		if (correlationData!= null && id != null){
			
			try {
				
				LOG.info("Sms callbakck confirm: {}",id);
				Long idlong = Long.parseLong(id);
				SmsRequest req = new SmsRequest();
				req.setSmsId(idlong);
				req.setStatus("success");
				sendhistorymapper.setSmsStatusBack(req);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error("error :{}",e.getMessage());
				e.printStackTrace();
			}
			
		}else {
			LOG.info("Sms callbakck confirm null ,This is normal");
		}
	}

}
