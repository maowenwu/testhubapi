package cn.huobi.boss.action;

import java.util.Date;
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
import com.huobi.quantification.entity.QuanProxyIp;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.ProxyIp;
import cn.huobi.framework.service.QuanProxyIpService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value = "/proxy")
public class ProxyAction {
	private static final Logger log = LoggerFactory.getLogger(ProxyAction.class);

	@Resource
	private QuanProxyIpService quanProxyIpService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectByCondition.do")
	@ResponseBody
	public Page<ProxyIp> selectByCondition(@RequestParam("baseInfo") String baseInfo,
			@Param("page") Page<ProxyIp> page) throws Exception {
		ProxyIp proxy = JSONObject.parseObject(baseInfo, ProxyIp.class);
		try {
			List<ProxyIp> proxys = quanProxyIpService.selectByCondition(proxy, page);
			page.setResult(proxys);
		} catch (Exception e) {
			log.error("条件查询代理IP失败");
			e.printStackTrace();
		}
		return page;
	}

	@RequestMapping(value = "/updateProxy.do")
	@ResponseBody
	@SystemLog(description = "更新代理", operCode = "proxy.update")
	public Map<String, Object> insert(@RequestParam("newInfo") String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		ProxyIp proxy = JSON.parseObject(newInfo, ProxyIp.class);
		JSONObject parseObject = JSON.parseObject(newInfo);
		Boolean switchValue = parseObject.getBoolean("status");
		try {
			int status = quanProxyIpService.update(proxy, switchValue);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			} else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("更新代理IP失败");
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping(value = "/insertProxy.do")
	@ResponseBody
	@SystemLog(description = "添加代理", operCode = "proxy.insert")
	public Map<String, Object> update(@RequestParam("newInfo") String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		QuanProxyIp proxy = JSON.parseObject(newInfo, QuanProxyIp.class);
		JSONObject parseObject = JSON.parseObject(newInfo);
		Boolean switchValue = parseObject.getBoolean("status");
		try {
			if (switchValue == null || !switchValue) {
				proxy.setState(0);
			}else {
				proxy.setState(1);
			}
			proxy.setCreateTime(new Date());
			proxy.setUpdateTime(new Date());
			int status = quanProxyIpService.insert(proxy);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			} else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("添加代理IP失败");
			e.printStackTrace();
		}
		return msg;
	}

	@RequestMapping(value="/deleteProxy.do")
	@ResponseBody
	@SystemLog(description = "删除代理",operCode="proxy.delete")
	public Map<String, Object> delete(@RequestParam("id")Integer id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			int status = quanProxyIpService.deleteById(id);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			}else {
				msg.put("status", false);
			}	
		} catch (Exception e) {
			log.error("代理IP删除失败");
			e.printStackTrace();
		}
		return msg;
	}
}