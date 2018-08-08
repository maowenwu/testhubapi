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
import com.huobi.quantification.entity.StrategyFinanceHistory;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FinanceHistory;
import cn.huobi.framework.service.FinanceService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value="/finance")
public class FinanceAction {
	private static final Logger log = LoggerFactory.getLogger(FinanceAction.class);
	
	@Resource
	private FinanceService financeService;
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByCondition.do")
	@ResponseBody
	public Page<FinanceHistory> selectByCondition(@RequestParam("baseInfo") String baseInfo ,
                                              @Param("page") Page<FinanceHistory> page) throws Exception {
		FinanceHistory config = JSONObject.parseObject(baseInfo, FinanceHistory.class);
		try {
			List<FinanceHistory> configs = financeService.selectByCondition(config, page);
			page.setResult(configs);
		} catch (Exception e) {
			log.error("条件查询财务列表失败");
			e.printStackTrace();
		}
		return page;
	}
	
	@RequestMapping(value="/insertFinance.do")
	@ResponseBody
	@SystemLog(description = "添加财务信息",operCode="finance.insert")
	public Map<String, Object> insertFinance(@RequestParam("newInfo")String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		StrategyFinanceHistory config = JSON.parseObject(newInfo, StrategyFinanceHistory.class);
		config.setInit(0);
		config.setCreateTime(new Date());
		config.setUpdateTime(new Date());
		if (config.getMoneyType() == null) {
			msg.put("status", false);
			msg.put("msg", "更新失败");
			return msg;
		}
		try {
			int status = financeService.insert(config);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			}else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("添加财务列表失败");
			e.printStackTrace();
		}
		return msg;
	}
}
