package cn.huobi.boss.system;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

//@Component
@Aspect
public class DaoAspet {

    private final static Logger log = LoggerFactory.getLogger(DaoAspet.class);

    /**
     * 切入点
     */
//    @Pointcut("execution(* cn.huobi.framework.service.impl.*.*(..))")
//    public void read(){}

    @Pointcut("@annotation(cn.huobi.boss.system.DataSource)")
    public void read(){}

//    @Pointcut("execution(* cn.huobi.framework.service.impl.*.*(..))")
////    @Pointcut("execution(* cn.huobi.framework.dao.*.*(..))")
//    public void read(){}

    @Before("read()")
    public void getDataSource(JoinPoint point){
        //获取切到的类
        //获取切到的方法名称
        //获取切到的参数类型
        //获取到方法
        //获取到方法上的注解，以及对应的值
        //设置对应的数据源
        Object object = point.getTarget();
        String methodName = point.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature)point.getSignature()).getParameterTypes();
        try {
            Method method = object.getClass().getMethod(methodName, parameterTypes);
            DataSource dataSource = method.getAnnotation(DataSource.class);
            String dbKey = dataSource.value();
            System.out.println("当前数据库:" + dbKey);
            DynamicDataSourceHolder.putDataSource(dbKey);
        } catch (NoSuchMethodException e) {
            log.info("读写分离获取数据源异常", e);
        }
    }

    @After("read()")
    public void afterDynamicSource(){
        DynamicDataSourceHolder.clearDbType();
    }

}
