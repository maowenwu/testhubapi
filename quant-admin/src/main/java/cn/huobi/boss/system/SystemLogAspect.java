package cn.huobi.boss.system;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.huobi.framework.model.BossOperLog;
import cn.huobi.framework.model.Result;
import cn.huobi.framework.model.UserLoginInfo;
import cn.huobi.framework.service.BossOperLogService;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

/**
 * AOP日志记录切面
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:37:42
 */
@Aspect    
@Component    
public  class SystemLogAspect { 
	
	private final Logger log = LoggerFactory.getLogger(SystemLogAspect.class);
	
	@Autowired
	private BossOperLogService bossOperLogService ;
	
	/**
	 * 切入点
	 */
    @Pointcut("@annotation(cn.huobi.boss.system.SystemLog)")    
    public  void controllerAspect() {    
    }    
    
    
	/**
	 * 配置后置返回通知
	 * @param joinPoint
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	@AfterReturning(pointcut="controllerAspect()",returning="result")
	public void afterReturn(JoinPoint joinPoint, Object result){
	    
		Map<String, Object> result_map= null;
		if (result instanceof  Map){
			result_map = (Map<String, Object>)result;
		}else if(result instanceof Result){
			Result temp = (Result) result;
			result_map = new HashMap<>();
			result_map.put("status", temp.isStatus());
		}

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
        
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        try {   
            BossOperLog bossOperLog =new BossOperLog();
            bossOperLog.setMethod_desc(getControllerMethodDescription(joinPoint)[0]);//方法描述
            bossOperLog.setOper_code(getControllerMethodDescription(joinPoint)[1]);//菜单代码
            bossOperLog.setOper_ip(getIpAddr(request));//请求IP
            //返回状态参数不统一(有的是result，有的是bols, status)
			if(result_map!=null && result_map.get("result")!=null){
				if((Boolean)result_map.get("result")){
					bossOperLog.setOper_status(CommonConst.ONE);//成功
				}else{
					bossOperLog.setOper_status(CommonConst.ZERO);//失败
				}
			}
			if(result_map!=null && result_map.get("bols")!=null){
				if((Boolean)result_map.get("bols")){
					bossOperLog.setOper_status(CommonConst.ONE);//成功
				}else{
					bossOperLog.setOper_status(CommonConst.ZERO);//失败
				}
			}
			if(result_map!=null && result_map.get("status")!=null){
				if((Boolean)result_map.get("status")){
					bossOperLog.setOper_status(CommonConst.ONE);//成功
				}else{
					bossOperLog.setOper_status(CommonConst.ZERO);//失败
				}
			}
			if(result_map!=null && result_map.get("state")!=null){
				if((Boolean)result_map.get("state")){
					bossOperLog.setOper_status(CommonConst.ONE);//成功
				}else{
					bossOperLog.setOper_status(CommonConst.ZERO);//失败
				}
			}
            bossOperLog.setRequest_method(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");//请求方法
            bossOperLog.setRequest_params(getRequestParams(joinPoint));//请求参数
            String result_str=JSON.toJSONString(result);
            if(result_str.length()>100){//有的结果会返回异常
            	result_str=result_str.substring(0, 99);
            }
            bossOperLog.setReturn_result(result_str);//返回结果
            bossOperLog.setUser_id(principal.getId());//请求编号
            bossOperLog.setUser_name(principal.getRealName());//请求名称
            //保存数据库    
            bossOperLogService.insert(bossOperLog);
        }  catch (Exception e) {    
            //记录本地异常日志    
        	e.printStackTrace();
        	log.error(e.getMessage(), e);    
        }

	}
    
	/**
	 * 获取注解中对方法的描述信息
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
     public  String[] getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {    
        String targetName = joinPoint.getTarget().getClass().getName();    
        String methodName = joinPoint.getSignature().getName();    
        Object[] arguments = joinPoint.getArgs();    
        Class targetClass = Class.forName(targetName);    
        Method[] methods = targetClass.getMethods();    
        String[] str = new String[2];
         for (Method method : methods) {    
             if (method.getName().equals(methodName)) {    
                Class[] clazzs = method.getParameterTypes();    
                 if (clazzs.length == arguments.length) {    
                	 str[0] = method.getAnnotation(SystemLog. class).description(); 
                	 str[1] = method.getAnnotation(SystemLog. class).operCode();
                     break;    
                }    
            }    
        }    
         return str;    
    } 
     
	/**
	 * 获取请求参数
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
      public  String getRequestParams(JoinPoint joinPoint)  throws Exception {    
          StringBuilder params = new StringBuilder();    
          if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {    
              for ( int i = 0; i < joinPoint.getArgs().length; i++) {
				try {
					Object obj=joinPoint.getArgs()[i];
					if(obj instanceof String )
						params.append(obj.toString());
					else
						params.append(JSON.toJSONString(obj));
				}catch (Exception e) {
				}
             }    
         }
         //去除结尾的;
         int index_od = params.lastIndexOf(";");
         if(index_od != -1 && index_od == (params.length()-1)){
         	params.setLength(params.length()-1);
         }
//         大于10000且小于20000的打印出来，并截取10000存入数据库
//         超过20000的不打印（一般就是导入）
         if(params.length()>10000){ //导入文件参数过长
//        	 params="导入文件参数过长";
        	 log.info("导入参数过长，原参数：{}", params);
        	 params.setLength(10000);
         }
         return params.toString();
     } 
     
	/**
	 * 返回客户端IP地址
	 * @param request
	 * @return
	 */
 	protected String getIpAddr(HttpServletRequest request) {
 		String ip = request.getHeader("X-Forwarded-For");
 		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
 			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
 			int index = ip.indexOf(",");
 			if (index != -1) {
 				return ip.substring(0, index);
 			} else {
 				return ip;
 			}
 		}
 		ip = request.getHeader("X-Real-IP");
 		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
 			return ip;
 		}
 		return request.getRemoteAddr();
 	}
}
