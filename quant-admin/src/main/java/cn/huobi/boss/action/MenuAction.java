package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.MenuInfo;
import cn.huobi.framework.model.RightInfo;
import cn.huobi.framework.model.SysMenu;
import cn.huobi.framework.service.MenuService;
import cn.huobi.framework.service.RightService;
import cn.huobi.framework.service.UserGrantService;
import cn.huobi.framework.util.Constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/menu")
public class MenuAction {
	private static final Logger log = LoggerFactory.getLogger(MenuAction.class);
	@Resource
	private RightService rightService;
	
	@Resource
	private UserGrantService userGrantService;
	 
	@Resource
	public MenuService menuService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/selectMenuByCondition.do")
	@ResponseBody
	public Page<SysMenu> selectMenuByCondition(@RequestParam("baseInfo") String baseInfo,
                                               @Param("page") Page<SysMenu> page) throws Exception {
		SysMenu sysMenu = JSON.parseObject(baseInfo, SysMenu.class);
		try{
			menuService.selectMenuByCondition(page, sysMenu);
		} catch(Exception e) {
			log.error("查询所有菜单失败");
		}
		return page;
	}
	
	// 新增
	@RequestMapping(value = "/saveMenu.do")
	@ResponseBody
	@SystemLog(description = "新增菜单",operCode="menu.insert")
	public Map<String, Object> saveMenu(@RequestBody String menu) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			msg = menuService.insertMenu(JSONObject.parseObject(menu, MenuInfo.class));
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "添加失败！");
			log.error("新增菜单失败",e);
		}
		return msg;
	}

	// 修改用户
	@RequestMapping(value = "/updateMenu.do")
	@ResponseBody
	@SystemLog(description = "修改菜单",operCode="menu.update")
	public Map<String, Object> updateMenu(@RequestBody String menu) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			msg = menuService.updateMenu(JSONObject.parseObject(menu, MenuInfo.class));
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "修改失败！");
			log.error("修改菜单失败",e);
		}
		return msg;
	}
	
	@RequestMapping(value="/deleteMenu.do")
	@ResponseBody
	@SystemLog(description = "删除菜单",operCode="menu.delete")
	public Map<String, Object> deleteMenu(@RequestParam("id")Integer id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = menuService.deleteMenus(id);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "删除成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "删除失败");
			log.error("删除菜单失败",e);
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getRights.do")
	@ResponseBody
	public Map<String, Object> getRights(@RequestParam("id")Integer id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			List<RightInfo> menuRights = menuService.getRightsByMenu(id);
			List<RightInfo> rightGroup = rightService.getAllRights();
			msg.put("menuRights", menuRights);
			msg.put("rightGroup", rightGroup);
		} catch (Exception e) {
			log.error("查询相关权限信息失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/editMenuRight.do")
	@ResponseBody
	public Map<String, Object> editRoleRight(@RequestBody String param) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			Integer menuId=JSONObject.parseObject(json.getString("menuId"), Integer.class);
			List<Integer> rightIds=JSONArray.parseArray(json.getString("rightIds"),Integer.class);
			int num = userGrantService.updateRightToMenu(menuId, rightIds);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "添加失败");
			log.error("添加菜单权限失败");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/selectRightByMenu.do")
	@ResponseBody
	public Map<String, Object> selectRightByMenu(@RequestParam("baseInfo") String baseInfo,
                                                 @Param("page") Page<RightInfo> page) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		RightInfo right = JSON.parseObject(baseInfo, RightInfo.class);
		try{
			rightService.selectRightByCondition(right, page);
			List<RightInfo> menuRights = menuService.getRightsByMenu(right.getId());
			msg.put("page", page);
			msg.put("menuRights", menuRights);
		} catch(Exception e) {
			log.error("查询所有菜单失败");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/selectMenuAndChildren")
	@ResponseBody
	public Map<String, Object> selectMenuAndChildren(@RequestParam("id")Integer id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try{
			List<MenuInfo> allMenus = menuService.getAllMenuAndChildren();
			List<MenuInfo> allPages = menuService.getAllPage();
			List<MenuInfo> rightMenus = rightService.getMenuByRight(id);
			msg.put("rightMenus", rightMenus);
			msg.put("allPages", allPages);
			msg.put("allMenus", allMenus);
		} catch(Exception e) {
			log.error("查询所有菜单失败");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/findMenuPageList")
	@ResponseBody
	public List<MenuInfo> findMenuPageList(Integer menuId) throws Exception {
		List<MenuInfo> pages = null;
		try{
			pages = menuService.findMenuPageList(menuId);
		} catch(Exception e) {
			log.error("查询所有菜单失败");
		}
		return pages;
	}
	
	/**
	 * 开启、关闭
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/switchLogFlag.do")
	@ResponseBody
	@SystemLog(description = "菜单是否记录日志开关",operCode="menu.switchLogFlag")
	public Map<String, Object> switchStatus (@RequestBody MenuInfo info) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try{
			int num = menuService.updateStatus(info);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "操作成功");
			}else{
				msg.put("status", false);
				msg.put("msg", "操作失败");
			}
		} catch (Exception e){
			msg.put("status", false);
			log.error("Switch 菜单是否记录日志开关失败！",e);
		}
		return msg;
	}

	/**
	 * 获取子菜单
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getMenuByParent")
	@ResponseBody
	public List<MenuInfo> getMenuByParent(@RequestParam Integer parentId) throws Exception {
		List<MenuInfo> list = null;
		try {
			list = menuService.getMenuByParent(parentId);
		} catch (Exception e) {
			log.error("获取子菜单信息失败");
			e.printStackTrace();
		}
		return list;
	}

}
