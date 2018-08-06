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
import com.huobi.quantification.entity.StrategyRiskConfig;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.FinanceService;
import cn.huobi.framework.service.RiskService;
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
	public Page<StrategyFinanceHistory> selectByCondition(@RequestParam("baseInfo") String baseInfo ,
                                              @Param("page") Page<StrategyFinanceHistory> page) throws Exception {
		StrategyFinanceHistory config = JSONObject.parseObject(baseInfo, StrategyFinanceHistory.class);
		try {
			List<StrategyFinanceHistory> configs = financeService.selectByCondition(config, page);
			page.setResult(configs);
		} catch (Exception e) {
			log.error("条件查询风控配置失败");
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
		config.setInit(1);
		config.setCreateTime(new Date());
		config.setUpdateTime(new Date());
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
			log.error("更新现货任务失败");
			e.printStackTrace();
		}
		return msg;
	}
}
