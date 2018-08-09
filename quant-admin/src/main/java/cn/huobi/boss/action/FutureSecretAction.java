package cn.huobi.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.entity.QuanAccountFutureSecret;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.FutureSecretService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value = "/futureSecret")
public class FutureSecretAction {
	private static final Logger log = LoggerFactory.getLogger(FutureSecretAction.class);

	@Resource
	private FutureSecretService futureSecretService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectByCondition.do")
	@ResponseBody
	public Page<QuanAccountFutureSecret> selectByCondition(@RequestParam("baseInfo") String baseInfo,
			@Param("page") Page<QuanAccountFutureSecret> page) throws Exception {
		QuanAccountFutureSecret secret = JSONObject.parseObject(baseInfo, QuanAccountFutureSecret.class);
		try {
			List<QuanAccountFutureSecret> secrets = futureSecretService.selectByCondition(secret, page);
			page.setResult(secrets);
		} catch (Exception e) {
			log.error("条件查询密钥失败");
			e.printStackTrace();
		}
		return page;
	}

	@RequestMapping(value = "/updateFutureSecret.do")
	@ResponseBody
	@SystemLog(description = "更新密钥", operCode = "futureSecret.update")
	public Map<String, Object> insert(@RequestParam("newInfo") String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		QuanAccountFutureSecret secret = JSON.parseObject(newInfo, QuanAccountFutureSecret.class);
		try {
			int status = futureSecretService.update(secret);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			} else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("更新密钥失败");
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping(value = "/insertFutureSecret.do")
	@ResponseBody
	@SystemLog(description = "添加密钥", operCode = "futureSecret.insert")
	public Map<String, Object> update(@RequestParam("newInfo") String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		QuanAccountFutureSecret secret = JSON.parseObject(newInfo, QuanAccountFutureSecret.class);
		try {
			int status = futureSecretService.insert(secret);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			} else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("添加密钥失败");
			e.printStackTrace();
		}
		return msg;
	}

	@RequestMapping(value="/deleteFutureSecret.do")
	@ResponseBody
	@SystemLog(description = "删除密钥",operCode="futureSecret.delete")
	public Map<String, Object> delete(@RequestParam("id")Integer id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			int status = futureSecretService.deleteById(id);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			}else {
				msg.put("status", false);
			}	
		} catch (Exception e) {
			log.error("密钥删除失败");
			e.printStackTrace();
		}
		return msg;
	}
}
