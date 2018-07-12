package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.MenuInfo;
import cn.huobi.framework.model.RightInfo;
import cn.huobi.framework.model.SysMenu;

public interface MenuDao {

	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,"
			+ "menu_type,order_no,log_flag from boss_sys_menu where menu_type='menu' "
			+ "and menu_level=0 order by (order_no+0) ")
	@ResultMap("cn.huobi.framework.dao.MenuInfoMapper.OneToManyResultMap")
	List<MenuInfo> getAllMenuAndChildren();
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,"
			+ "menu_type,order_no from boss_sys_menu where menu_type='menu' "
			+ "and menu_level=0 order by (order_no+0) ")
	@ResultMap("cn.huobi.framework.dao.SysMenuMapper.OneToManyResultMap")
	List<SysMenu> getSysMenuAndChildren();

	@Select("select * from boss_sys_menu")
	@ResultType(MenuInfo.class)
	List<MenuInfo> getAllMenu();
	
	@Select("select * from boss_sys_menu where menu_type='page'")
	@ResultType(MenuInfo.class)
	List<MenuInfo> getAllPage();

	@SelectProvider(type = SqlProvider.class, method = "selectMenuByCondition")
	@ResultType(SysMenu.class)
	List<SysMenu> selectMenuByCondition(Page<SysMenu> page, @Param("sysMenu")SysMenu sysMenu);
	
	@Select("select r.* from boss_shiro_right r,boss_right_menu rm where rm.menu_id=#{id} and r.id=rm.right_id")
	@ResultType(RightInfo.class)
	List<RightInfo> getRightsByMenu(Integer id);

	/**
	 * 根据权限id获取可用的菜单ID列表
	 * @param permits 逗号分隔的权限id字符串
	 * @return
	 */
	@Select("select m.id from boss_sys_menu m join boss_right_menu rm on m.id=rm.menu_id join boss_shiro_right r on rm.right_id = r.id and r.right_code in (${permits})")
	@ResultType(Integer.class)
	List<Integer> getMenuIdsByPermits(@Param("permits")String permits);

	@Select("select * from boss_sys_menu where id=#{parentId}")
	@ResultType(MenuInfo.class)
	MenuInfo getParent(Integer parentId);

	@Insert("insert into boss_sys_menu(parent_id,menu_code,menu_name,menu_url,menu_level,menu_type,order_no) values "
			+ "(#{menu.parentId},#{menu.menuCode},#{menu.menuName},#{menu.menuUrl},#{menu.menuLevel},#{menu.menuType},#{menu.orderNo})")
	int insertMenu(@Param("menu")MenuInfo menu);
	
	@Update("UPDATE boss_sys_menu set parent_id=#{menu.parentId},menu_code=#{menu.menuCode},menu_name=#{menu.menuName},menu_url=#{menu.menuUrl}"
			+ ",menu_level=#{menu.menuLevel},menu_type=#{menu.menuType},order_no=#{menu.orderNo} where id=#{menu.id}")
	int updateMenu(@Param("menu")MenuInfo menu);

	@Delete("DELETE FROM boss_right_menu WHERE  menu_id=#{menuId}")
	int deleteRightByMenuId(Integer menuId);
	
	@Delete("DELETE FROM boss_sys_menu WHERE  id=#{menuId}")
	int deleteMenus(Integer menuId);
	
	
	public class SqlProvider{
		public String selectMenuByCondition(Map<String, Object> param){
			final SysMenu sysMenu = (SysMenu)param.get("sysMenu");
			return new SQL(){{
				SELECT("*");
				FROM("boss_sys_menu");
				if(sysMenu != null){
					if(StringUtils.isNotBlank(sysMenu.getMenuName())){
						sysMenu.setMenuName(sysMenu.getMenuName()+"%");
						WHERE("menu_name like #{sysMenu.menuName}");
					}
					if(StringUtils.isNotBlank(sysMenu.getMenuUrl())){
						sysMenu.setMenuUrl(sysMenu.getMenuUrl()+"%");
						WHERE("menu_url like #{sysMenu.menuUrl}");
					}
					if(StringUtils.isNotBlank(sysMenu.getMenuCode())){
						sysMenu.setMenuCode(sysMenu.getMenuCode()+"%");
						WHERE("menu_code like #{sysMenu.menuCode}");
					}
				}
				ORDER_BY("order_no+0");
			}}.toString();
		}
	}

	@Select("select * from boss_sys_menu where menu_type='page' and menu_level=2 and parent_id=#{menuId} order by (order_no+0)")
	@ResultType(MenuInfo.class)
	List<MenuInfo> findMenuPageList(Integer menuId);

	@Select("select m1.menu_code from boss_sys_menu m1 left join boss_sys_menu m2 on m1.menu_url=m2.menu_url where m2.menu_code=#{menuCode}")
	@ResultType(String.class)
	List<String> getMenuCodeByCode(@Param("menuCode")String menuCode);

	@Select("select m.id from boss_sys_menu m where m.menu_code=#{menuCode}")
	@ResultType(MenuInfo.class)
	MenuInfo checkMenuCode(String menuCode);

	@Update("update boss_sys_menu m set m.log_flag=#{logFlag} where m.id=#{id}")
	int updateStatus(MenuInfo info);
	
	@Select("select m.* from boss_sys_menu m where m.parent_id=#{parentId} order by m.order_no;")
	@ResultType(MenuInfo.class)
	List<MenuInfo> getMenuByParent(Integer parentId);

	
}
