package cn.huobi.framework.service.sysUser;

import ch.qos.logback.core.util.SystemInfo;

public interface SystemInfoService {
	SystemInfo findSystemInfo(SystemInfo systemInfo) throws Exception;
	int updateSystemInfo(SystemInfo systemInfo) throws Exception;
}
