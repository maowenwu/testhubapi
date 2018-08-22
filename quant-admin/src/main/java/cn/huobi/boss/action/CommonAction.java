package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.framework.dto.StrategyInstanceInfo;
import cn.huobi.framework.service.sysUser.CommonService;
import cn.huobi.framework.util.Constants;
import com.huobi.quantification.dao.StrategyHedgeConfigMapper;
import com.huobi.quantification.entity.StrategyInstanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/common")
public class CommonAction {
    private static final Logger log = LoggerFactory.getLogger(CommonAction.class);

    @Resource
    private StrategyHedgeConfigMapper strategyHedgeConfigMapper;

    @Autowired
    CommonService commonService;


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/findStrategyInstanceInfo.do")
    @ResponseBody
    public List<StrategyInstanceInfo> findStrategyInstanceInfo(String futureBaseCoin) {
        List<StrategyInstanceInfo> list = commonService.findStrategyInstanceInfo(futureBaseCoin);
        return list;
    }


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/findStrategyInstanceBaseInfo.do")
    @ResponseBody
    public StrategyInstanceConfig findStrategyInstanceBaseInfo(Integer id) {
        return commonService.findStrategyInstanceBaseInfo(id);
    }


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/findAccountInfo.do")
    @ResponseBody
    public Map<String, Object> findAccountInfo(Integer id) {
        return commonService.findAccountInfo(id);
    }


    //策略    风控统计 收益统计
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/strategyRiskProfitCount.do")
    @ResponseBody
    public Map<String, Object> strategyRiskCount(Integer id) {
        return commonService.strategyRiskProfitCount(id);
    }

    //实例  风控统计 收益统计
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/instanceRiskProfitCount.do")
    @ResponseBody
    public Map<String, Object> strategyRiskProfitCount(Long instanceId) {
        return commonService.instanceRiskProfitCount(instanceId);
    }

    //运行中实例  基本信息
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/runningInstance.do")
    @ResponseBody
    public Map<String, Object> runningInstance(Long instanceId) {
        return commonService.instanceRiskProfitCount(instanceId);
    }


}
