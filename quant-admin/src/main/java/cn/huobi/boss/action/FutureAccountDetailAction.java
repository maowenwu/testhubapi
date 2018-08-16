package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FutureAccount;
import cn.huobi.framework.service.FutureAccountDetailService;
import cn.huobi.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.entity.QuanAccountFuture;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/futureAccountDetail")
public class FutureAccountDetailAction {
    private static final Logger log = LoggerFactory.getLogger(FutureAccountDetailAction.class);

    @Autowired
    private FutureAccountDetailService futureAccountDetailService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAccount.do")
    @ResponseBody
    public Page<QuanAccountFuture> selectAccount(@RequestParam("baseInfo") String baseInfo,
                                                 @Param("page") Page<QuanAccountFuture> page) throws Exception {
        JSONObject baseInfoObject = JSON.parseObject(baseInfo);
        Integer accountId = baseInfoObject.getInteger("accountId");
        QuanAccountFuture quanAccountFuture = futureAccountDetailService.selectAccountById(accountId);
        List<QuanAccountFuture> quanAccountFutures = new ArrayList<>();
        quanAccountFutures.add(quanAccountFuture);
        page.setResult(quanAccountFutures);
        return page;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAccountDetail.do")
    @ResponseBody
    public Page<FutureAccount> selectAccountDetail(@RequestParam("baseInfo") String baseInfo,
                                                   @Param("page") Page<FutureAccount> page) throws Exception {
        JSONObject baseInfoObject = JSON.parseObject(baseInfo);
        Integer accountId = baseInfoObject.getInteger("accountId");
        futureAccountDetailService.selectAccountDetail(accountId);
        return page;
    }

}
