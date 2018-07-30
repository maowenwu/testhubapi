/*package cn.huobi.boss.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.huobi.boss.system.DataSource;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SysMenu;
import cn.huobi.framework.service.QuanAccountHistoryService;
import cn.huobi.framework.util.Constants;

@Controller
public class QuanAccountHistoryAction {

	@Resource
	QuanAccountHistoryService quanAccountHistoryService;
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getQuanAccountHistoryPage.do")
	@ResponseBody
	public Page<SysMenu> selectMenuByCondition() {
		System.out.println("=========");
		return null;
	}
	
	
}
*/