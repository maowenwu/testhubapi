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
import com.huobi.quantification.entity.StrategyHedgeConfig;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.HedgeConfig;
import cn.huobi.framework.service.HedgeService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value="/hedge")
public class HedgeAction {
	private static final Logger log = LoggerFactory.getLogger(HedgeAction.class);
	
	@Resource
	private HedgeService hedgeService;
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByCondition.do")
	@ResponseBody
	public Page<HedgeConfig> selectByCondition(@RequestParam("baseInfo") String baseInfo ,
                                              @Param("page") Page<HedgeConfig> page) throws Exception {
		HedgeConfig config = JSONObject.parseObject(baseInfo, HedgeConfig.class);
		try {
			List<HedgeConfig> configs = hedgeService.selectByCondition(config, page);
			page.setResult(configs);
		} catch (Exception e) {
			log.error("条件查询对冲配置失败");
			e.printStackTrace();
		}
		return page;
	}
	
	@RequestMapping(value="/updateHedge.do")
	@ResponseBody
	@SystemLog(description = "更新配置",operCode="hedge.update")
	public Map<String, Object> updateSpotJob(@RequestParam("newInfo")String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		StrategyHedgeConfig riskConfig = JSON.parseObject(newInfo, StrategyHedgeConfig.class);
		log.error(newInfo);
		try {
			int status = hedgeService.updateHedge(riskConfig);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			}else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("更新对冲配置失败");
			e.printStackTrace();
		}
		return msg;
	}
}
