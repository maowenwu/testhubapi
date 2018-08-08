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
import com.huobi.quantification.entity.StrategyRiskConfig;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.RiskService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value="/risk")
public class RiskAction {
	private static final Logger log = LoggerFactory.getLogger(RiskAction.class);
	
	@Resource
	private RiskService riskService;
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByCondition.do")
	@ResponseBody
	public Page<StrategyRiskConfig> selectJobByCondition(@RequestParam("baseInfo") String baseInfo ,
                                              @Param("page") Page<StrategyRiskConfig> page) throws Exception {
		StrategyRiskConfig config = JSONObject.parseObject(baseInfo, StrategyRiskConfig.class);
		try {
			List<StrategyRiskConfig> configs = riskService.selectByCondition(config, page);
			page.setResult(configs);
		} catch (Exception e) {
			log.error("条件查询风控配置失败");
			e.printStackTrace();
		}
		return page;
	}
	
	@RequestMapping(value="/updateRisk.do")
	@ResponseBody
	@SystemLog(description = "更新任务",operCode="risk.update")
	public Map<String, Object> updateRisk(@RequestParam("newInfo")String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		StrategyRiskConfig riskConfig = JSON.parseObject(newInfo, StrategyRiskConfig.class);
		try {
			int status = riskService.updateRisk(riskConfig);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			}else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("更新风控配置失败");
			e.printStackTrace();
		}
		return msg;
	}
}
