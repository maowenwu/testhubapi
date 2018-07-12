package cn.huobi.boss.system;

import org.springframework.stereotype.Repository;

import cn.huobi.framework.util.Constants;

import java.lang.annotation.*;

/**
 * 数据源的注解，用来选择主从库
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repository
public @interface DataSource {

    String value() default Constants.DATA_SOURCE_MASTER;

}
