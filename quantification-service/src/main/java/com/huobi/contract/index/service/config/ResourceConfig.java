
package com.huobi.contract.index.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
//@ImportResource({ "classpath:/META-INF/spring-contract-index-dubbo.xml", "classpath:/META-INF/spring-contract-index-job.xml","classpath:/META-INF/MonitorConfig.xml","classpath:/META-INF/WsConfig.xml" })
@ImportResource({ "classpath:/META-INF/spring-contract-index-dubbo.xml", "classpath:/META-INF/spring-contract-index-job.xml","classpath:/META-INF/MonitorConfig.xml"})
public class ResourceConfig {

}