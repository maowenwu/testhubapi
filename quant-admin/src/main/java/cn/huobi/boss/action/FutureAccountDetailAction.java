package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.framework.service.FutureAccountDetailService;
import cn.huobi.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/futureAccountDetail")
public class FutureAccountDetailAction {
    private static final Logger log = LoggerFactory.getLogger(FutureAccountDetailAction.class);

    @Autowired
    private FutureAccountDetailService futureAccountDetailService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAccount.do")
    @ResponseBody
    public List<QuanAccountFuture> selectAccount(@RequestParam("futureAccount") String futureAccount) throws Exception {
        QuanAccountFuture quanFutureAccount = JSON.parseObject(futureAccount, QuanAccountFuture.class);
        List<QuanAccountFuture> quanAccountFutureArrayList = new ArrayList<>();
        quanAccountFutureArrayList.add(quanFutureAccount);
        return quanAccountFutureArrayList;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAccountDetail.do")
    @ResponseBody
    public List<QuanAccountFutureAsset> selectAccountDetail(@RequestParam("futureAccount") String futureAccount) throws Exception {
        QuanAccountFuture quanAccountFuture = JSON.parseObject(futureAccount,QuanAccountFuture.class);
        quanAccountFuture = futureAccountDetailService.selectAccountId(quanAccountFuture);
        List<QuanAccountFutureAsset> accountFutureAssets = futureAccountDetailService.selectAccountDetail(quanAccountFuture);
        return accountFutureAssets;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectFinanceHistory.do")
    @ResponseBody
    public PageInfo<StrategyFinanceHistory> selectFinanceHistory(@RequestParam("futureAccount") String futureAccount,@RequestParam("baseInfo") String baseInfo,
                                                                 @Param("page") PageInfo<StrategyFinanceHistory> page) throws Exception {
        BossFinanceQuery bossFinanceQuery = new BossFinanceQuery();
        if (StringUtils.isNotBlank(baseInfo) && !baseInfo.equals("null")){
            bossFinanceQuery = JSON.parseObject(baseInfo, BossFinanceQuery.class);
        }
        QuanAccountFuture quanAccountFuture = JSON.parseObject(futureAccount,QuanAccountFuture.class);
        bossFinanceQuery.setAccountId(quanAccountFuture.getAccountSourceId());
        bossFinanceQuery.setExchangeId(quanAccountFuture.getExchangeId());
        page = futureAccountDetailService.selectFinanceHistory(bossFinanceQuery, page);
        return page;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAccountPosition.do")
    @ResponseBody
    public PageInfo<QuanAccountFuturePosition> selectAccountPosition(@RequestParam("futureAccount") String futureAccount,@RequestParam("positionInfo") String positionInfo,
                                                                     @Param("page") PageInfo<QuanAccountFuturePosition> page) throws Exception {
        QuanAccountFuture quanAccountFuture = JSON.parseObject(futureAccount,QuanAccountFuture.class);
        quanAccountFuture = futureAccountDetailService.selectAccountId(quanAccountFuture);
        QuanAccountFuturePosition quanAccountFuturePosition = JSON.parseObject(positionInfo,QuanAccountFuturePosition.class);
        quanAccountFuturePosition.setAccountFutureId(quanAccountFuture.getId());
        page = futureAccountDetailService.selectAccountPosition(quanAccountFuturePosition, page);
        return page;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectOrder.do")
    @ResponseBody
    public PageInfo<QuanOrderFuture> selectOrder(@RequestParam("futureAccount") String futureAccount,@RequestParam("orderInfo") String orderInfo,
                                                                     @Param("page") PageInfo<QuanOrderFuture> page) throws Exception {
        QuanAccountFuture quanAccountFuture = JSON.parseObject(futureAccount,QuanAccountFuture.class);
        quanAccountFuture = futureAccountDetailService.selectAccountId(quanAccountFuture);
        QuanOrderFuture quanOrderFuture = new QuanOrderFuture();
        if (StringUtils.isNotBlank(orderInfo) && !orderInfo.equals("null")){
            JSON.parseObject(orderInfo,QuanOrderFuture.class);
            JSONObject orderInfoObject = JSON.parseObject(orderInfo);
            Integer idType = null;
            Long id = null;
            if (null != orderInfoObject){
                idType = orderInfoObject.getInteger("idType");
                id = orderInfoObject.getLong("id");
            }
            if (null != id && null != idType){
                switch (idType){
                    case 0 :
                        quanOrderFuture.setInnerOrderId(id);
                        break;
                    case 1 :
                        quanOrderFuture.setExOrderId(id);
                        break;
                    case 2 :
                        quanOrderFuture.setLinkOrderId(id);
                        break;
                }
            }
        }
        quanOrderFuture.setAccountId(quanAccountFuture.getId());
        List<Integer> sourceStatus = new ArrayList<>();
        if (null == quanOrderFuture || null == quanOrderFuture.getSourceStatus()){
            page = futureAccountDetailService.selectAccountOrder(quanOrderFuture,page);
        }else if (quanOrderFuture.getSourceStatus()== -1){
            sourceStatus.add(3);
            sourceStatus.add(4);
            sourceStatus.add(5);
            sourceStatus.add(6);
            sourceStatus.add(7);
            quanOrderFuture.setSourceStatus(null);
            page = futureAccountDetailService.selectAccountOrder(sourceStatus, quanOrderFuture, page);
        }else if (quanOrderFuture.getSourceStatus()== -2){
            sourceStatus.add(3);
            sourceStatus.add(4);
            quanOrderFuture.setSourceStatus(null);
            page = futureAccountDetailService.selectAccountOrder(sourceStatus, quanOrderFuture, page);
        }else if (quanOrderFuture.getSourceStatus()== -3){
            sourceStatus.add(5);
            sourceStatus.add(6);
            sourceStatus.add(7);
            quanOrderFuture.setSourceStatus(null);
            page = futureAccountDetailService.selectAccountOrder(sourceStatus, quanOrderFuture, page);
        }
        return page;
    }

    @RequestMapping(value = "/insertFinance.do")
    @ResponseBody
    public Map<String, Object> insertFinance(@RequestParam("financeInfo") String financeInfo, @RequestParam("futureAccount") String futureAccount) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        StrategyFinanceHistory history = JSON.parseObject(financeInfo, StrategyFinanceHistory.class);
        QuanAccountFuture accountFuture = JSON.parseObject(futureAccount, QuanAccountFuture.class);
        history.setAccountSourceId(accountFuture.getAccountSourceId());
        history.setExchangeId(accountFuture.getExchangeId());
        int result = futureAccountDetailService.insertFinance(history);
        if (result > 0) {
            msg.put("status", true);
            msg.put("msg", "更新成功！");
        } else {
            msg.put("status", false);
            msg.put("msg", "更新失败");
        }
        return msg;
    }

}
