package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.*;
import cn.huobi.framework.service.*;
import cn.huobi.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.BossShiroRight;
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
@RequestMapping(value = "/right")
public class RightAction {

    private static final Logger log = LoggerFactory.getLogger(RightAction.class);

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private RightService rightService;

    @Resource
    private MenuService menuService;

    @Resource
    private UserGrantService userGrantService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectRightByCondition.do")
    @ResponseBody
    public PageInfo<BossShiroRight> selectRightByCondition(@RequestParam("baseInfo") String baseInfo,
                                                           @Param("page") PageInfo<BossShiroRight> page) {
        BossShiroRight right = JSON.parseObject(baseInfo, BossShiroRight.class);
        page = rightService.selectPage(right, page);
        return page;
    }

    @RequestMapping(value = "/checkCodeUnique.do")
    @ResponseBody
    public Map<String, Object> checkCodeUnique(@RequestParam("rightCode") String rightCode) throws Exception {
        Map<String, Object> msg = null;
        try {
            msg = rightService.checkCodeUnique(rightCode);
        } catch (Exception e) {
            log.error("验证编码是否唯一，出现错误");
        }
        return msg;
    }

    @RequestMapping(value = "/saveRight.do")
    @ResponseBody
    @SystemLog(description = "新增角色", operCode = "right.insert")
    public Map<String, Object> saveRight(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject json = JSONObject.parseObject(param);
            RightInfo right = JSONObject.parseObject(json.getString("right"), RightInfo.class);
            int num = rightService.insertRight(right);
            if (num == 1) {
                msg.put("status", true);
                msg.put("msg", "添加成功！");
            } else {
                msg.put("status", true);
                msg.put("msg", "添加失败！");
            }
        } catch (Exception e) {
            log.error("新增用户失败");
        }
        return msg;
    }

    @RequestMapping(value = "/updateRight.do")
    @ResponseBody
    @SystemLog(description = "修改角色", operCode = "right.update")
    public Map<String, Object> updateRight(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject json = JSONObject.parseObject(param);
            RightInfo right = JSONObject.parseObject(json.getString("right"), RightInfo.class);
            int num = rightService.updateRight(right);
            if (num == 1) {
                msg.put("status", true);
                msg.put("msg", "添加成功！");
            } else {
                msg.put("status", true);
                msg.put("msg", "添加失败！");
            }
        } catch (Exception e) {
            log.error("新增用户失败");
        }
        return msg;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/getUsers.do")
    @ResponseBody
    public Map<String, Object> getUsers(@RequestParam("id") Integer id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            List<UserInfo> rightUsers = rightService.getUserByRight(id);
            List<UserInfo> userGroup = userService.getAllUsers();
            msg.put("rightUsers", rightUsers);
            msg.put("userGroup", userGroup);
        } catch (Exception e) {
            log.error("查询相关用户信息失败");
        }
        return msg;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/getRoles.do")
    @ResponseBody
    public Map<String, Object> getRoles(@RequestParam("id") Integer id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            List<RoleInfo> rightRoles = rightService.getRoleByRight(id);
            List<RoleInfo> roleGroup = roleService.getAllRoles();
            msg.put("rightRoles", rightRoles);
            msg.put("roleGroup", roleGroup);
        } catch (Exception e) {
            log.error("查询相关用户信息失败");
        }
        return msg;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/getMenus.do")
    @ResponseBody
    public Map<String, Object> getMenus(@RequestParam("id") Integer id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            List<MenuInfo> rightMenus = rightService.getMenuByRight(id);
            List<MenuInfo> menuGroup = menuService.getAllMenu();
            msg.put("rightMenus", rightMenus);
            msg.put("menuGroup", menuGroup);
        } catch (Exception e) {
            log.error("查询相关用户信息失败");
        }
        return msg;
    }

    @RequestMapping(value = "/editRightUser.do")
    @ResponseBody
    public Map<String, Object> editRightUser(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject json = JSONObject.parseObject(param);
            Integer rightId = JSONObject.parseObject(json.getString("rightId"), Integer.class);
            List<Integer> userIds = JSONArray.parseArray(json.getString("userIds"), Integer.class);
            int num = userGrantService.updateRightAddUser(rightId, userIds);
            if (num > 0) {
                msg.put("status", true);
                msg.put("msg", "添加成功");
            }
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "添加失败");
            log.error("添加权限用户失败");
        }
        return msg;
    }

    @RequestMapping(value = "/editRightRole.do")
    @ResponseBody
    public Map<String, Object> editRightRole(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject json = JSONObject.parseObject(param);
            Integer rightId = JSONObject.parseObject(json.getString("rightId"), Integer.class);
            List<Integer> roleIds = JSONArray.parseArray(json.getString("roleIds"), Integer.class);
            int num = userGrantService.updateRightAddRole(rightId, roleIds);
            if (num > 0) {
                msg.put("status", true);
                msg.put("msg", "添加成功");
            }
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "添加失败");
            log.error("添加权限角色失败");
        }
        return msg;
    }

    @RequestMapping(value = "/editRightMenu.do")
    @ResponseBody
    @SystemLog(description = "添加权限菜单", operCode = "right.menu")
    public Map<String, Object> editRightMenu(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject json = JSONObject.parseObject(param);
            Integer rightId = JSONObject.parseObject(json.getString("rightId"), Integer.class);
            List<Integer> menuIds = JSONArray.parseArray(json.getString("menuIds"), Integer.class);
            int num = userGrantService.updateMenuToRight(rightId, menuIds);
            if (num > 0) {
                msg.put("status", true);
                msg.put("msg", "添加成功");
            }
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "添加失败");
            log.error("添加权限菜单失败");
        }
        return msg;
    }

    @RequestMapping(value = "/deleteRight.do")
    @ResponseBody
    @SystemLog(description = "删除角色", operCode = "right.delete")
    public Map<String, Object> deleteRight(@RequestParam("id") Integer id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            int num = rightService.deleteRight(id);
            if (num > 0) {
                msg.put("status", true);
                msg.put("msg", "删除成功");
            }
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "删除失败");
            log.error("删除权限失败");
        }
        return msg;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectMenuByRight.do")
    @ResponseBody
    public Map<String, Object> selectMenuByRight(@RequestParam("baseInfo") String baseInfo,
                                                 @Param("page") Page<SysMenu> page) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        SysMenu sysMenu = JSON.parseObject(baseInfo, SysMenu.class);
        try {
            menuService.selectMenuByCondition(page, sysMenu);
            List<MenuInfo> rightMenus = rightService.getMenuByRight(sysMenu.getId());
            msg.put("page", page);
            msg.put("rightMenus", rightMenus);
        } catch (Exception e) {
            log.error("根据权限查询菜单，条件查询失败");
        }
        return msg;
    }
}
