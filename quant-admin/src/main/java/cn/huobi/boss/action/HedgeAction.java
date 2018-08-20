package cn.huobi.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.StrategyHedgeConfigMapper;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.entity.StrategyRiskConfig;
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
import com.huobi.quantification.entity.StrategyHedgeConfig;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.HedgeConfig;
import cn.huobi.framework.service.HedgeService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value = "/hedge")
public class HedgeAction {
    private static final Logger log = LoggerFactory.getLogger(HedgeAction.class);

    @Resource
    private HedgeService hedgeService;

    @Autowired
    StrategyHedgeConfigMapper strategyHedgeConfigMapper;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByCondition.do")
    @ResponseBody
    public PageInfo<HedgeConfig> selectByCondition(@RequestParam("baseInfo") String baseInfo,
                                                   @Param("page") PageInfo<HedgeConfig> page) throws Exception {
        HedgeConfig config = JSONObject.parseObject(baseInfo, HedgeConfig.class);
        page = hedgeService.selectByCondition(config, page);
        return page;
    }

    @RequestMapping(value = "/updateHedge.do")
    @ResponseBody
    @SystemLog(description = "更新配置", operCode = "hedge.update")
    public Map<String, Object> update(@RequestParam("newInfo") String newInfo) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        HedgeConfig riskConfig = JSON.parseObject(newInfo, HedgeConfig.class);
        int status = hedgeService.updateHedge(riskConfig);
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
    public StrategyHedgeConfig selectBySymbolContractType(@RequestParam("symbol") String symbol, @RequestParam("contractType") String contractType) {
        return strategyHedgeConfigMapper.selectBySymbolContractType(symbol, contractType);
    }
}
