package cn.huobi.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.PageInfo;
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
import cn.huobi.framework.model.SpotJob;
import cn.huobi.framework.service.QuanJobService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value="/spotJob")
public class SpotJobAction {
	private static final Logger log = LoggerFactory.getLogger(SpotJobAction.class);
	
	@Resource
	private QuanJobService quanJobService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="selectJobByCondition.do")
	@ResponseBody
	public PageInfo<SpotJob> selectJobByCondition(@RequestParam("baseInfo") String baseInfo ,
                                              @Param("page") PageInfo<SpotJob> page) throws Exception {
		SpotJob job = JSONObject.parseObject(baseInfo, SpotJob.class);
		try {
			page = quanJobService.selectByCondition(job, page);
		} catch (Exception e) {
			log.error("条件查询现货任务失败");
			e.printStackTrace();
		}
		return page;
	}
	
	@RequestMapping(value="/updateSpotJob.do")
	@ResponseBody
	@SystemLog(description = "更新任务",operCode="spotJob.update")
	public Map<String, Object> updateSpotJob(@RequestParam("newInfo")String newInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		JSONObject parseObject = JSON.parseObject(newInfo);
		try {
			int status = quanJobService.updateSpotJob(parseObject.getBoolean("status"),parseObject.getInteger("id"));
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
