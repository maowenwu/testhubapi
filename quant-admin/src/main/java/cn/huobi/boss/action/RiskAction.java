package cn.huobi.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/risk")
public class RiskAction {
    private static final Logger log = LoggerFactory.getLogger(RiskAction.class);

    @Resource
    private RiskService riskService;

    @Autowired
    StrategyRiskConfigMapper strategyRiskConfigMapper;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByCondition.do")
    @ResponseBody
    public PageInfo<StrategyRiskConfig> selectJobByCondition(@RequestParam("baseInfo") String baseInfo,
                                                             @Param("page") PageInfo<StrategyRiskConfig> page) throws Exception {
        StrategyRiskConfig config = JSONObject.parseObject(baseInfo, StrategyRiskConfig.class);
        page = riskService.selectByCondition(config, page);
        return page;
    }

    @RequestMapping(value = "/updateRisk.do")
    @ResponseBody
    @SystemLog(description = "更新任务", operCode = "risk.update")
    public Map<String, Object> updateRisk(@RequestParam("newInfo") String newInfo) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        StrategyRiskConfig riskConfig = JSON.parseObject(newInfo, StrategyRiskConfig.class);
        int status = riskService.updateRisk(riskConfig);
        if (status > 0) {
            msg.put("status", true);
            msg.put("msg", "更新成功！");
        } else {
            msg.put("status", false);
            msg.put("msg", "更新失败");
        }
        return msg;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectBySymbolContractType.do")
    @ResponseBody
    public StrategyRiskConfig selectBySymbolContractType(@RequestParam("symbol") String symbol, @RequestParam("contractType") String contractType) {
        StrategyRiskConfig strategyRiskConfig=strategyRiskConfigMapper.selectBySymbolContractType(symbol, contractType);
        return strategyRiskConfig;
    }
}
