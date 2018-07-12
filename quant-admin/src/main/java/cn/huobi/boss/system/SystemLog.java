package cn.huobi.boss.system;

import java.lang.annotation.*;    

/**
 * 自定义注解,用于记录操作日志    
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:37:31
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface SystemLog {    
    
	//方法描述
    String description() default "";
    
	//菜单代码
    String operCode() default "";
}
