package cn.huobi.boss.listener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import cn.huobi.framework.service.RedisService;

//@SuppressWarnings("rawtypes")
//@Component
public class AuthenticationLoginListener 
  implements ApplicationListener  {
	@Resource
	private RedisService redisService;
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof AuthenticationFailureBadCredentialsEvent) {
			 AuthenticationFailureBadCredentialsEvent authEvent = (AuthenticationFailureBadCredentialsEvent) event;
			System.out.println("AuthenticationFailureBadCredentialsEvent");
			WebAuthenticationDetails auth = (WebAuthenticationDetails) 
					authEvent.getAuthentication().getDetails();
	         
	        try {
	            String ip = auth.getRemoteAddress();
	            String key = "blocked:"+ip;
	            Integer num = 0;
	            if(redisService.exists(key)){
	            	num = Integer.valueOf(redisService.select(key).toString());
	            }
	            num = num + 1;
	            redisService.insertString(key, num.toString(), (long) (60*30));//30分钟
			} catch (Exception e2) {
				
			}
		}
		if (event instanceof AuthenticationSuccessEvent) {
			AuthenticationSuccessEvent authEvent = (AuthenticationSuccessEvent) event;
			System.out.println("AuthenticationSuccessEvent");
			WebAuthenticationDetails auth = (WebAuthenticationDetails) 
					 authEvent.getAuthentication().getDetails();
			         try {
			    	    String ip = auth.getRemoteAddress();
			            String key = "blocked:"+ip;
			            
			            if(redisService.exists(key)){
			            	List<String> keys = new ArrayList<String>();
			            	keys.add(key);
			            	redisService.delete(keys);
			            }
					} catch (Exception e2) {
						
					}
		}
		
	}

 

 
}