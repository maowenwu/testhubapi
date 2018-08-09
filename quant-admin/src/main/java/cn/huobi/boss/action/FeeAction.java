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
import com.huobi.quantification.entity.StrategyTradeFee;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.TradeFee;
import cn.huobi.framework.service.FeeService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value="/fee")
public class FeeAction {
	private static final Logger log = LoggerFactory.getLogger(FeeAction.class);
	
	@Resource
	private FeeService feeService;
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectByCondition.do")
	@ResponseBody
	public Page<TradeFee> selectByCondition(@RequestParam("baseInfo") String baseInfo ,
                                              @Param("page") Page<TradeFee> page) throws Exception {
		TradeFee tradeFee = JSONObject.parseObject(baseInfo, TradeFee.class);
		try {
			List<TradeFee> configs = feeService.selectByCondition(tradeFee, page);
			page.setResult(configs);
		} catch (Exception e) {
			log.error("条件查询费率配置失败");
			e.printStackTrace();
		}
		return page;
	}
	
	@RequestMapping(value="/updateFee.do")
	@ResponseBody
	@SystemLog(description = "更新费率",operCode="fee.update")
	public Map<String, Object> update(@RequestParam("newInfo")String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		TradeFee fee = JSON.parseObject(newInfo, TradeFee.class);
		try {
			int status = feeService.updateHedge(fee);
			if (status > 0) {
				msg.put("status", true);
				msg.put("msg", "更新成功！");
			}else {
				msg.put("status", false);
				msg.put("msg", "更新失败");
			}
		} catch (Exception e) {
			log.error("更新费率配置失败");
			e.printStackTrace();
		}
		return msg;
	}
}
