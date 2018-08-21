package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.framework.dto.StrategyInstanceInfo;
import cn.huobi.framework.service.sysUser.CommonService;
import cn.huobi.framework.util.Constants;
import com.huobi.quantification.dao.StrategyHedgeConfigMapper;
import com.huobi.quantification.entity.StrategyHedgeConfig;
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


    //策略    收益统计
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/test4.do")
    @ResponseBody
    public Map<String, Object> test4() {
        Map<String, Object> map = new HashMap<>();
        map.put("totalProfit", "1111");//总盈亏（USDT）
        map.put("yieldRate", "12");//收益率
        map.put("annualizedReturn", "20");//年化收益率
        return map;
    }

    //策略    风控统计
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/test5.do")
    @ResponseBody
    public Map<String, Object> test5() {
        Map<String, Object> map = new HashMap<>();
        map.put("netPositionUSDT", "123323");//净头寸（USDT）
        map.put("riskRate", "89");//保证金率
        return map;
    }

    //实例
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/test6.do")
    @ResponseBody
    public Map<String, Object> test6() {
        Map<String, Object> map = new HashMap<>();
        map.put("", "");//实例ID
        map.put("", "");//策略实例启动时间
        map.put("", "");//合约代码
        map.put("", "");//策略实例运行时间

        map.put("", "");//摆盘
        map.put("", "");
        map.put("", "");
        map.put("", "");
        map.put("", "");


        return map;
    }

}
