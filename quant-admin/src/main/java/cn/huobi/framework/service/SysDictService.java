package cn.huobi.framework.service;

import cn.huobi.framework.db.pagination.Page;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.SysDict;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

public interface SysDictService {


    Map<String, Object> insert(SysDict info);
    Map<String, Object> update(SysDict info) throws SchedulerException;
    int delete(Integer id);
    JSONObject selectDictAndChildren();
    SysDict getByKey(String string);
    int updateSysValue(SysDict sysDict);
    PageInfo<SysDict> selectPage(SysDict entity, PageInfo<SysDict> pageInfo);
    List<SysDict> selectList(SysDict entity);
}
