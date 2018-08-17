package cn.huobi.framework.service;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.MenuInfo;
import cn.huobi.framework.model.RightInfo;
import cn.huobi.framework.model.RoleInfo;
import cn.huobi.framework.model.UserInfo;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.BossShiroRight;

import java.util.List;
import java.util.Map;

public interface RightService {
    //查询权限
    Page<RightInfo> selectRightByCondition(RightInfo right, Page<RightInfo> page);

    //新增权限
    int insertRight(RightInfo right);

    //修改权限
    int updateRight(RightInfo right);

    //删除权限
    int deleteRight(Integer rightId);

    Map<String, Object> checkCodeUnique(String rightCode);

    //权限已授权的角色
    List<RoleInfo> getRoleByRight(Integer rightId);

    //权限已授权的用户
    List<UserInfo> getUserByRight(Integer rightId);

    //查询所有的权限
    List<RightInfo> getAllRights();

    //权限已授权的菜单
    List<MenuInfo> getMenuByRight(Integer id);

    public PageInfo<BossShiroRight> selectPage(BossShiroRight entity, PageInfo<BossShiroRight> pageInfo);
}
