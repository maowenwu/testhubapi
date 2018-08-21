package cn.huobi.framework.service.impl;

import cn.huobi.boss.quartz.QuartzManager;
import cn.huobi.boss.quartz.QuartzSimpleManager;
import cn.huobi.framework.dao.SysDictDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.RedisService;
import cn.huobi.framework.service.SysDictService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.SysDictMapper;
import com.huobi.quantification.entity.BossShiroUser;
import com.huobi.quantification.entity.SysDict;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysDictService")
@Transactional
public class SysDictServiceImpl implements SysDictService {

    private final Logger log = LoggerFactory.getLogger(SysDictServiceImpl.class);

    @Resource
    private SysDictDao sysDictDao;

    @Resource
    private QuartzSimpleManager quartzSimpleManager;

    @Autowired
    private QuartzManager quartzManager;

    @Resource
    private RedisService redisService;

    @Autowired
    SysDictMapper sysDictMapper;


    @Override
    public Map<String, Object> insert(SysDict info) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        msg.put("msg", "操作失败");
        if (info != null && "BOSS_UNIQUE".equals(info.getParentId())) {
            int num = sysDictDao.checkUnique(info);
            if (num > 0) {
                msg.put("msg", "已存在唯一的数据字典");
                return msg;
            }
        }
        int num = sysDictDao.insert(info);
        if (num > 0) {
            msg.put("status", true);
            msg.put("msg", "操作成功");
        }
        return msg;
    }

    @Override
    public Map<String, Object> update(SysDict info) throws SchedulerException {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        msg.put("msg", "操作失败");
        if (info != null && "BOSS_UNIQUE".equals(info.getParentId())) {
            int num = sysDictDao.checkUnique(info);
            if (num > 0) {
                msg.put("msg", "已存在唯一的数据字典");
                return msg;
            }
        }
        int num = sysDictDao.update(info);
        if (num > 0) {
            msg.put("status", true);
            msg.put("msg", "操作成功");
        }
        return msg;
    }

    @Override
    public int delete(Integer id) {
        return sysDictDao.delete(id);
    }

    @Override
    public JSONObject selectDictAndChildren() {
        List<SysDict> list = sysDictDao.selectAllDict();
        JSONObject json = new JSONObject();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SysDict dic = list.get(i);
                JSONArray array = null;
                if (json.containsKey(dic.getSysKey())) {
                    array = json.getJSONArray(dic.getSysKey());
                } else {
                    array = new JSONArray();
                    json.put(dic.getSysKey(), array);
                }
                JSONObject item = new JSONObject();
                item.put("text", dic.getSysName());
                array.add(item);
            }
        }
        return json;
    }

    @Override
    public SysDict getByKey(String string) {
        return sysDictDao.getByKey(string);
    }


    @Override
    public int updateSysValue(SysDict sysDict) {
        int num = sysDictDao.updateSysValue(sysDict);
        if (num > 1) {
            throw new RuntimeException("updateSysValue一次只能修改一个唯一的数据字典");
        }
        return num;
    }


    @Override
    public PageInfo<SysDict> selectPage(SysDict entity, PageInfo<SysDict> pageInfo) {
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<SysDict> list = sysDictMapper.selectList(entity);
        pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<SysDict> selectList(SysDict entity) {
        List<SysDict> list = sysDictMapper.selectList(entity);
        return list;
    }
}
