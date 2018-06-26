
package com.huobi.contract.index.service.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.huobi.contract.index.common.util.SpringContextHolder;

@Configuration
@EnableTransactionManagement
public class MyBatisConfig implements TransactionManagementConfigurer {

	private static final Logger LOG = LoggerFactory.getLogger(MyBatisConfig.class);

	@Autowired
	private DataSourceProperties properties;


	@Bean
	public DruidDataSource getDataSource() {
		LOG.info("开始连接数据源...");
		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName(this.properties.getDriverClass());
		ds.setUrl(this.properties.getUrl());
		ds.setUsername(this.properties.getUsername());
		ds.setPassword(this.properties.getPassword());
		ds.setMaxActive(this.properties.getMaxActive());
		ds.setInitialSize(this.properties.getInitialSize());
		ds.setValidationQuery(this.properties.getValidationQuery());
		ds.setPoolPreparedStatements(this.properties.isPoolPreparedStatements());
		ds.setMaxPoolPreparedStatementPerConnectionSize(this.properties.getMaxPoolPreparedStatementPerConnectionSize());
		ds.setTestWhileIdle(true);
		ds.setTestOnBorrow(false);
		ds.setTestOnReturn(false);

		WallFilter wallFilter = new WallFilter();
		WallConfig config = new WallConfig();
		config.setMultiStatementAllow(true);
		wallFilter.setConfig(config);
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(wallFilter);
		ds.setProxyFilters(filters);

		try {
			ds.setFilters(this.properties.getFilters());
		} catch (SQLException e) {
			LOG.error("数据源连接异常：", e);
		}
		ds.setConnectionProperties(this.properties.getConnectionProperties());
		return ds;
	}

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean() {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(getDataSource());
		bean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
		// 添加插件
		//bean.setPlugins(new Interceptor[] { paginationInterceptor });
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			bean.setMapperLocations(resolver.getResources("classpath:com/huobi/contract/index/dao/mysql/*.xml"));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		try {
			return bean.getObject();
		} catch (Exception e) {
			LOG.error("获取SQL Session Factory异常：", e);
			throw new RuntimeException("获取SQL Session Factory异常：", e);
		}
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(getDataSource());
	}

	@Bean
	public SpringContextHolder initSpringContextHolder() {
		return new SpringContextHolder();
	}
}
