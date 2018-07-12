package cn.huobi.framework.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class Constants {
	public static final String sys_config_list_redis_key = "sys_config_list_redis_key";

	/*阿里云存储boss附件临时bucket*/
	public static final String ALIYUN_OSS_TEMP_TUCKET="boss-temp";

	/*阿里云存储boss附件bucket*/
	public static final String ALIYUN_OSS_ATTCH_TUCKET="agent-attch";
	public static final String ALIYUN_OSS_SIGN_TUCKET="sign-img";

	//超级银行家，银行家后台的秘钥
	public static final String SUPER_BANK_SECRET = "superBankKey!@#$%^&*5646665656check";
	//数据库，从库
	public static final String DATA_SOURCE_SLAVE = "slave";
	//数据库，主库
	public static final String DATA_SOURCE_MASTER = "master";

}
