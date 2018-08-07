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

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SpotAccount;
import cn.huobi.framework.service.SpotAccountService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value = "/spotAccount")
public class SpotAccountAction {
	private static final Logger log = LoggerFactory.getLogger(SpotAccountAction.class);

	@Resource
	private SpotAccountService spotAccountService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectByCondition.do")
	@ResponseBody
	public Page<SpotAccount> selectByCondition(@RequestParam("baseInfo") String baseInfo,
			@Param("page") Page<SpotAccount> page) throws Exception {
		SpotAccount account = JSONObject.parseObject(baseInfo, SpotAccount.class);
		try {
			List<SpotAccount> accounts = spotAccountService.selectByCondition(account, page);
			page.setResult(accounts);
		} catch (Exception e) {
			log.error("条件查询风控配置失败");
			e.printStackTrace();
		}
		return page;
	}

	@RequestMapping(value = "/updateSpotAccount.do")
	@ResponseBody
	@SystemLog(description = "更新账户信息", operCode = "spotAccount.update")
	public Map<String, Object> insert(@RequestParam("newInfo") String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		SpotAccount account = JSON.parseObject(newInfo, SpotAccount.class);
		try {
			int status = spotAccountService.update(account);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			} else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("更新现货任务失败");
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping(value = "/insertSpotAccount.do")
	@ResponseBody
	@SystemLog(description = "添加账户", operCode = "spotAccount.insert")
	public Map<String, Object> update(@RequestParam("newInfo") String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		SpotAccount account = JSON.parseObject(newInfo, SpotAccount.class);
		account.setState("working");
		try {
			int status = spotAccountService.insert(account);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			} else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("更新现货任务失败");
			e.printStackTrace();
		}
		return msg;
	}

	@RequestMapping(value="/deleteSpotAccount.do")
	@ResponseBody
	@SystemLog(description = "删除账户信息",operCode="spotAccount.delete")
	public Map<String, Object> delete(@RequestParam("id")Integer id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			int status = spotAccountService.deleteById(id);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			}else {
				msg.put("status", false);
			}	
		} catch (Exception e) {
			log.error("账户删除失败");
			e.printStackTrace();
		}
		return msg;
	}
}
