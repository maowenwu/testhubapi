package aliyun;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.GeneratePresignedUrlRequest;

import cn.huobi.framework.util.ALiYunOssUtil;
import cn.huobi.framework.util.Constants;

/**
 * <p>Description: <／p>
 * <p>Company: www.eeepay.cn<／p> 
 * @author liusha
 * @date 2016年6月1日
 */
public class TestAliyun {
	private static final String READ_PATH=TestAliyun.class.getResource("TestAliyun.class").getPath();
	private static final String WRITE_PATH="";
	private static final String FILE_NAME="TestAliyun.class";
	
	@Test
	public void testSave() throws IOException {
		File file=new File(READ_PATH);
		//判断文件是否存
		if(!ALiYunOssUtil.exists(Constants.ALIYUN_OSS_TEMP_TUCKET, FILE_NAME)){
			//保存文件名
			ALiYunOssUtil.saveFile(Constants.ALIYUN_OSS_TEMP_TUCKET, FILE_NAME, file);
		}
		//获取文件URL
		System.out.println(ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_TEMP_TUCKET, FILE_NAME, new Date(2016,5,2)));
	}
	@Test
	public void testGetURL() throws IOException {
		//获取文件URL
		Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
		System.out.println(ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, "20160905111026987128img0.jpg", expiresDate));
	}
	
}
