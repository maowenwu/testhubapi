package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.model.HedgeConfig;
import cn.huobi.framework.service.HedgeService;
import cn.huobi.framework.service.sysUser.CommonService;
import cn.huobi.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.StrategyHedgeConfigMapper;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/test")
public class TestAction {
    private static final Logger log = LoggerFactory.getLogger(TestAction.class);

    @Resource
    private StrategyHedgeConfigMapper strategyHedgeConfigMapper;

    @Autowired
    CommonService commonService;


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/test1.do")
    @ResponseBody
    public List<StrategyHedgeConfig> selectByCondition(String symbol) {
        List<StrategyHedgeConfig> list = strategyHedgeConfigMapper.selectList(null);
        return list;
    }


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/test2.do")
    @ResponseBody
    public StrategyHedgeConfig selectBySymbolContractType(String contractType) {
        return strategyHedgeConfigMapper.selectBySymbolContractType("btc", "this_week");
    }


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/test3.do")
    @ResponseBody
    public Map<String, Object> test3() {
        return commonService.findAccountInfo();
    }


}
