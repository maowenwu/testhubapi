package cn.huobi.boss.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.StrategyOrderConfigMapper;
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
import com.huobi.quantification.entity.StrategyOrderConfig;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.OrderService;
import cn.huobi.framework.util.Constants;

@Controller
@RequestMapping(value = "/order")
public class OrderAction {
    private static final Logger log = LoggerFactory.getLogger(OrderAction.class);

    @Resource
    private OrderService orderService;

    @Autowired
    StrategyOrderConfigMapper strategyOrderConfigMapper;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByCondition.do")
    @ResponseBody
    public PageInfo<StrategyOrderConfig> selectByCondition(@RequestParam("baseInfo") String baseInfo,
                                                           @Param("page") PageInfo<StrategyOrderConfig> page) throws Exception {
        StrategyOrderConfig config = JSONObject.parseObject(baseInfo, StrategyOrderConfig.class);
        page = orderService.selectByCondition(config, page);
        return page;
    }

    @RequestMapping(value = "/updateOrder.do")
    @ResponseBody
    @SystemLog(description = "更新配置", operCode = "order.update")
    public Map<String, Object> updateOrder(@RequestParam("newInfo") String newInfo) {
        Map<String, Object> msg = new HashMap<>();
        try {
            StrategyOrderConfig config = JSON.parseObject(newInfo, StrategyOrderConfig.class);
            int status = orderService.updateOrder(config);
            if (status > 0) {
                msg.put("status", true);
                msg.put("msg", "更新成功！");
            } else {
                msg.put("status", false);
                msg.put("msg", "更新失败");
            }
        } catch (Exception e) {
            log.error("更新摆单配置失败", e);
            msg.put("status", false);
            msg.put("msg", "更新失败");
        }
        return msg;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectBySymbolContractType.do")
    @ResponseBody
    public StrategyOrderConfig selectBySymbolContractType(@RequestParam("symbol") String symbol, @RequestParam("contractType") String contractType) {
        return strategyOrderConfigMapper.selectBySymbolContractType(symbol, contractType);
    }
}
