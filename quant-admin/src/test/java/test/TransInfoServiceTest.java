package test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.auth0.jwt.JWTSigner;

import cn.huobi.framework.enums.FreezeStatus;
import cn.huobi.framework.util.ClientInterface;

public class TransInfoServiceTest extends BaseTest{
	
	private static final Logger log = LoggerFactory.getLogger(TransInfoServiceTest.class);
	
	@Value("${api.secret:zouruijin}")
	private String apiSecret;
    @Test
    public void testFreezeTrans() throws Exception {
    	
    	final String secret = apiSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
		final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		JWTSigner signer = new JWTSigner(secret);
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("transOrderNo", "SK416029753259629126");
		claims.put("freezeStatus", FreezeStatus.CAIWUFREEZE.toString());
		claims.put("iat", iat);
		claims.put("jti", jti);

		String token = signer.sign(claims);
		String url = "http://localhost:8012/boss2/transInfoAction/freezeTrans.do";
		System.out.println("url:" + url);
		Map<String,String> params=new HashMap<>();
		params.put("token", token);
		String responseText = new ClientInterface(url, params).postRequest();
		System.out.println("response:" + responseText);
		Map<String, Object> responseMap = JSON.parseObject(responseText, new TypeReference<Map<String, Object>>() {});
		Object msg = responseMap.get("msg");
		Boolean status = (Boolean)responseMap.get("status");
		log.info(responseMap.toString());
    	
		
	
	}
    
}
