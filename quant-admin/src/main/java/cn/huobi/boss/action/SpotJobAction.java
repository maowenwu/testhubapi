package cn.huobi.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.entity.QuanAccount;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SpotJob;
import cn.huobi.framework.model.SysDict;
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
	public Page<SpotJob> selectJobByCondition(@RequestParam("baseInfo") String baseInfo ,
                                              @Param("page") Page<SpotJob> page) throws Exception {
		SpotJob job = JSONObject.parseObject(baseInfo, SpotJob.class);
		try {
			List<SpotJob> spotJobs = quanJobService.selectDicByCondition(job, page);
			page.setResult(spotJobs);
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
	
	/**
	 * 保存任务
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveSpotJob.do")
	@ResponseBody
	@SystemLog(description = "新增|修改任务",operCode="spotJob.insert")
	public Map<String, Object> saveSpotJob(@RequestBody String param) throws Exception {
		return null;
	}
	
	@RequestMapping(value="/deleteSpotJob.do")
	@ResponseBody
	@SystemLog(description = "删除任务",operCode="spotJob.delete")
	public Map<String, Object> deleteSpotJob(@RequestParam("id")Integer id) throws Exception {
		try {
			quanJobService.deleteById(id);
		} catch (Exception e) {
			log.error("{}号任务数据删除失败");
			e.printStackTrace();
		}
		return null;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="selectDictAndChildren.do")
	@ResponseBody
	public Object selectDictAndChildren() throws Exception {
		return null;
	}
	
	/**
	 * 
	 * @author tans
	 * @date 2017年6月21日 下午3:13:08
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="getByKey.do")
	@ResponseBody
	public Map<String, Object> getByKey(@Param("sysKey")String sysKey) {
		return null;
	}
	
	/**
	 * 修改字典sysValue
	 * @author tans
	 * @date 2017年6月21日 下午3:50:21
	 * @param sysDict
	 * @return
	 */
	@RequestMapping(value="updateSysValue.do")
	@ResponseBody
	public Map<String, Object> updateSysValue(@RequestBody SysDict sysDict) {
		return null;
	}
}
