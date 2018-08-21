package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.SysDictService;
import cn.huobi.framework.util.Constants;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.SysDict;
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
@RequestMapping(value = "/sysDict")
public class SysDictAction {

    private static final Logger log = LoggerFactory.getLogger(SysDictAction.class);

    @Resource
    private SysDictService sysDictService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectDicByCondition.do")
    @ResponseBody
    public PageInfo<SysDict> selectDicByCondition(@RequestParam("baseInfo") String baseInfo,
                                                  @Param("page") PageInfo<SysDict> page) throws Exception {
        SysDict dict = JSONObject.parseObject(baseInfo, SysDict.class);
        page = sysDictService.selectPage(dict, page);
        return page;
    }

    /**
     * 保存数据字典
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveSysDict.do")
    @ResponseBody
    @SystemLog(description = "新增|修改数据字典", operCode = "sysDict.insert")
    public Map<String, Object> saveSysDict(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        JSONObject json = JSONObject.parseObject(param);
        SysDict info = JSONObject.parseObject(json.getString("info"), SysDict.class);
        if (info == null) {
            msg.put("status", false);
            msg.put("msg", "添加失败");
            return msg;
        }
        try {
            if (info.getId() == null) {
                msg = sysDictService.insert(info);
            } else {
                msg = sysDictService.update(info);
            }
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "操作失败");
            log.error("添加 || 修改数据字典失败", e);
        }
        return msg;
    }

    @RequestMapping(value = "/deleteSysDict.do")
    @ResponseBody
    @SystemLog(description = "删除数据字典", operCode = "sysDict.delete")
    public Map<String, Object> deleteSysDict(@RequestParam("id") Integer id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            int num = 0;
            if (id == null || id == 0) {
                msg.put("status", false);
                msg.put("msg", "操作失败");
                return msg;
            }
            num = sysDictService.delete(id);
            if (num > 0) {
                msg.put("status", true);
                msg.put("msg", "操作成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "操作失败");
            }
        } catch (Exception e) {
            log.error("删除数据字典失败");
        }
        return msg;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "selectDictAndChildren.do")
    @ResponseBody
    public Object selectDictAndChildren() throws Exception {
        Map<String, Object> msg = null;
        try {
            msg = sysDictService.selectDictAndChildren();
        } catch (Exception e) {
            log.error("查询所有的数据字典失败!", e);
        }
        return msg;
    }

    /**
     * @return
     * @throws Exception
     * @author tans
     * @date 2017年6月21日 下午3:13:08
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "getByKey.do")
    @ResponseBody
    public Map<String, Object> getByKey(@Param("sysKey") String sysKey) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        msg.put("msg", "查询失败");
        if (StringUtils.isBlank(sysKey)) {
            msg.put("msg", "参数不能空");
            return msg;
        }
        try {
            SysDict sysDict = sysDictService.getByKey(sysKey);
            if (sysDict == null) {
                msg.put("msg", "找不到对应的字典");
                return msg;
            }
            msg.put("status", true);
            msg.put("sysDict", sysDict);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("查询失败!", e);
        }
        return msg;
    }

    /**
     * 修改字典sysValue
     *
     * @param sysDict
     * @return
     * @author tans
     * @date 2017年6月21日 下午3:50:21
     */
    @RequestMapping(value = "updateSysValue.do")
    @ResponseBody
    public Map<String, Object> updateSysValue(@RequestBody SysDict sysDict) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        msg.put("msg", "操作失败");
        try {
//			SysDict sysDict = JSONObject.parseObject(param, SysDict.class);
            if (sysDict == null || StringUtils.isBlank(sysDict.getSysKey()) || StringUtils.isBlank(sysDict.getSysValue())) {
                msg.put("msg", "参数不能空");
                return msg;
            }
            int num = sysDictService.updateSysValue(sysDict);
            if (num == 1) {
                msg.put("status", true);
                msg.put("msg", "操作成功");
            }
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "操作失败");
            log.error("操作失败!", e);
        }
        return msg;
    }

    @RequestMapping(value = "selectListByType.do")
    @ResponseBody
    public List<SysDict> selectListByType(@Param("type") String type) {
        SysDict entity = new SysDict();
        entity.setSysKey(type);
        List<SysDict> list = sysDictService.selectList(entity);
        return list;

    }

}
