package cn.huobi.boss.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.huobi.quantification.entity.StrategyFinanceHistory;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FinanceHistory;
import cn.huobi.framework.service.FinanceService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value = "/finance")
public class FinanceAction {
    private static final Logger log = LoggerFactory.getLogger(FinanceAction.class);

    @Resource
    private FinanceService financeService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByCondition.do")
    @ResponseBody
    public PageInfo<FinanceHistory> selectByCondition(@RequestParam("baseInfo") String baseInfo,
                                                      @Param("page") PageInfo<FinanceHistory> page) throws Exception {
        FinanceHistory history = JSONObject.parseObject(baseInfo, FinanceHistory.class);
        page = financeService.selectByCondition(history, page);
        return page;
    }

    @RequestMapping(value = "/insertFinance.do")
    @ResponseBody
    @SystemLog(description = "添加财务信息", operCode = "finance.insert")
    public Map<String, Object> insertFinance(@RequestParam("newInfo") String newInfo) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        StrategyFinanceHistory history = JSON.parseObject(newInfo, StrategyFinanceHistory.class);
        if (history.getMoneyType() == null || history.getExchangeId() == null) {
            msg.put("status", false);
            msg.put("msg", "更新失败");
            return msg;
        }
        int status = financeService.insert(history);
        if (status > 0) {
            msg.put("status", true);
            msg.put("msg", "更新成功！");
        } else {
            msg.put("status", false);
            msg.put("msg", "更新失败");
        }
        return msg;
    }
}
