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
import cn.huobi.framework.model.FutureAccount;
import cn.huobi.framework.service.FutureAccountService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value = "/futureAccount")
public class FutureAccountAction {
	private static final Logger log = LoggerFactory.getLogger(FutureAccountAction.class);

	@Resource
	private FutureAccountService futureAccountService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectByCondition.do")
	@ResponseBody
	public Page<FutureAccount> selectByCondition(@RequestParam("baseInfo") String baseInfo,
			@Param("page") Page<FutureAccount> page) throws Exception {
		FutureAccount account = JSONObject.parseObject(baseInfo, FutureAccount.class);
		try {
			List<FutureAccount> accounts = futureAccountService.selectByCondition(account, page);
			page.setResult(accounts);
		} catch (Exception e) {
			log.error("条件查询期货账户失败");
			e.printStackTrace();
		}
		return page;
	}

	@RequestMapping(value = "/updateFutureAccount.do")
	@ResponseBody
	@SystemLog(description = "更新账户信息", operCode = "futureAccount.update")
	public Map<String, Object> insert(@RequestParam("newInfo") String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		FutureAccount account = JSON.parseObject(newInfo, FutureAccount.class);
		try {
			int status = futureAccountService.update(account);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			} else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("更新期货账户失败");
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping(value = "/insertFutureAccount.do")
	@ResponseBody
	@SystemLog(description = "添加账户", operCode = "futureAccount.insert")
	public Map<String, Object> update(@RequestParam("newInfo") String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		FutureAccount account = JSON.parseObject(newInfo, FutureAccount.class);
		account.setState("working");
		try {
			int status = futureAccountService.insert(account);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			} else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("更新期货账户失败");
			e.printStackTrace();
		}
		return msg;
	}

	@RequestMapping(value="/deleteFutureAccount.do")
	@ResponseBody
	@SystemLog(description = "删除账户信息",operCode="futureAccount.delete")
	public Map<String, Object> delete(@RequestParam("id")Integer id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			int status = futureAccountService.deleteById(id);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			}else {
				msg.put("status", false);
			}	
		} catch (Exception e) {
			log.error("期货账户删除失败");
			e.printStackTrace();
		}
		return msg;
	}
}
