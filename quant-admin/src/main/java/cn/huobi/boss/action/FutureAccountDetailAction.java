package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.framework.service.FutureAccountDetailService;
import cn.huobi.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "/futureAccountDetail")
public class FutureAccountDetailAction {
    private static final Logger log = LoggerFactory.getLogger(FutureAccountDetailAction.class);

    @Autowired
    private FutureAccountDetailService futureAccountDetailService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAccount.do")
    @ResponseBody
    public PageInfo<QuanAccountFuture> selectAccount(@RequestParam("futureAccount") String futureAccount,
                                                     @Param("page") PageInfo<QuanAccountFuture> page) throws Exception {
        QuanAccountFuture quanFutureAccount = JSON.parseObject(futureAccount, QuanAccountFuture.class);
        ArrayList<QuanAccountFuture> arrayList = new ArrayList<>();
        arrayList.add(quanFutureAccount);
        page = new PageInfo<>(arrayList);
        return page;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAccountDetail.do")
    @ResponseBody
    public PageInfo<QuanAccountFutureAsset> selectAccountDetail(@RequestParam("futureAccount") String futureAccount,
                                                     @Param("page") PageInfo<QuanAccountFutureAsset> page) throws Exception {
        QuanAccountFuture quanAccountFuture = JSON.parseObject(futureAccount,QuanAccountFuture.class);
        page = futureAccountDetailService.selectAccountDetail(quanAccountFuture, page);
        return page;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAccountPosition.do")
    @ResponseBody
    public PageInfo<QuanAccountFuturePosition> selectAccountPosition(@RequestParam("positionInfo") String positionInfo,
                                                                     @Param("page") PageInfo<QuanAccountFuturePosition> page) throws Exception {
        System.err.println(positionInfo);
        QuanAccountFuturePosition quanAccountFuturePosition = JSON.parseObject(positionInfo,QuanAccountFuturePosition.class);
        page = futureAccountDetailService.selectAccountPosition(quanAccountFuturePosition, page);
        return page;
    }

}
