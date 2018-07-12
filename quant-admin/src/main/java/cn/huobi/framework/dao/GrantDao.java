package cn.huobi.framework.dao;

import org.apache.ibatis.annotations.*;

import cn.huobi.framework.model.MenuInfo;
import cn.huobi.framework.model.RightInfo;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface GrantDao {
	public static final String GRANT_USER_ROLE="INSERT INTO boss_user_role(user_id,role_id) values ";
	public static final String GRANT_ROLE_USER="INSERT INTO boss_user_role(role_id,user_id) values ";
	public static final String GRANT_USER_RIGHT="INSERT INTO boss_user_right(user_id,right_id) values ";
	public static final String GRANT_RIGHT_USER="INSERT INTO boss_user_right(right_id,user_id) values ";
	public static final String GRANT_ROLE_RIGHT="INSERT INTO boss_role_right(role_id,right_id) values ";
	public static final String GRANT_RIGHT_ROLE="INSERT INTO boss_role_right(right_id,role_id) values ";
	public static final String GRANT_RIGHT_MENU="INSERT INTO boss_right_menu(right_id,menu_id) values ";
	public static final String GRANT_MENU_RIGHT="INSERT INTO boss_right_menu(menu_id,right_id) values ";
	public static final String GRANT_USER_MENU="INSERT INTO boss_user_menu(user_id,menu_id) values ";
	
	
	@InsertProvider(type=SqlProvide.class,method="grantUserRoleRight")
	int grantUserRole(@Param("id") Integer id, @Param("list") List<Integer> list, @Param("type") String type);
	@Delete("DELETE FROM boss_user_role where user_id=#{userId} and role_id=#{roleId}")
	int delUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
	@Insert("insert into boss_user_role(user_id,role_id) values(#{userId},#{roleId})")
	int insertUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

	@InsertProvider(type=SqlProvide.class,method="grantUserRoleRight")
	int grantUserRight(@Param("id") Integer id, @Param("list") List<Integer> list, @Param("type") String type);
	@Delete("DELETE FROM boss_user_right where user_id=#{userId} and right_id=#{rightId}")
	int delUserRight(@Param("userId") Integer userId, @Param("rightId") Integer rightId);
	@Insert("insert into boss_user_right(user_id,right_id) values(#{userId},#{rightId})")
	int insertUserRight(@Param("userId") Integer userId, @Param("rightId") Integer rightId);

	@Delete("delete from boss_user_menu where user_id=#{userId}")
	int deleteMenuByUser(Integer userId);
	@InsertProvider(type=SqlProvide.class, method="grantUserRoleRight")
	int grantUserMenu(@Param("id") Integer userId, @Param("list") List<Integer> menuIds, @Param("type") String type);

	@InsertProvider(type=SqlProvide.class,method="grantUserRoleRight")
	int grantRoleRight(@Param("id") Integer id, @Param("list") List<Integer> list, @Param("type") String type);
	@Delete("DELETE FROM boss_role_right where role_id=#{roleId} and right_id=#{rightId}")
	int delRoleRight(@Param("roleId") Integer roleId, @Param("rightId") Integer rightId);
	@Insert("insert into boss_role_right(roleId,right_id) values(#{roleId},#{rightId})")
	int insertRoleRight(@Param("roleId") Integer roleId, @Param("rightId") Integer rightId);

	@Delete("DELETE FROM boss_right_menu where right_id=#{rightId}")
	void deleteRightMenuByRightId(Integer rightId);
	@Delete("DELETE FROM boss_right_menu where menu_id=#{menuId}")
	void deleteRightMenuByMenuId(Integer menuId);
	@Delete("DELETE FROM boss_right_menu where right_id=#{rightId} and menu_id=#{menuId}")
	int delRightMenu(@Param("rightId") Integer rightId, @Param("menuId") Integer menuId);
	@Insert("insert into boss_right_menu(right_id,menu_id) values(#{rightId},#{menu_id})")
	int insertRightMenu(@Param("rightId") Integer rightId, @Param("menuId") Integer menuId);

	public class SqlProvide{
		public String grantUserRoleRight(Map<String, Object> param){
			@SuppressWarnings("unchecked")
            List<Integer> list = (List<Integer>)param.get("list");
			String type="";
			switch(param.get("type").toString()){
				case "user_role":
					type=GRANT_USER_ROLE;
					break;
				case "role_user":
					type=GRANT_ROLE_USER;
					break;
				case "user_right":
					type=GRANT_USER_RIGHT;
					break;
				case "right_user":
					type=GRANT_RIGHT_USER;
					break;
				case "role_right":
					type=GRANT_ROLE_RIGHT;
					break;
				case "right_role":
					type=GRANT_RIGHT_ROLE;
				case "right_menu":	
					type=GRANT_RIGHT_MENU;
					break;
				case "menu_right":	
					type=GRANT_MENU_RIGHT;
					break;
				case "user_menu":	
					type=GRANT_USER_MENU;
					break;
			}
			StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(type);
            MessageFormat messageFormat = new MessageFormat("(#'{'id},#'{'list[{0}]})");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(messageFormat.format(new Integer[]{i}));
                stringBuilder.append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
		}
	}
	
	@Select("SELECT sr.* FROM (SELECT right_id FROM boss_user_right WHERE user_id=#{userId} UNION"
			+ " SELECT rr.right_id FROM boss_user_role ur,boss_role_right rr WHERE ur.role_id=rr.role_id "
			+ " AND user_id=#{userId}) urc LEFT JOIN boss_shiro_right sr  ON urc.right_id=sr.id")
	@ResultType(RightInfo.class)
	public List<RightInfo> getUserAllRight(Integer userId);
	
	@Select("SELECT sr.* FROM (SELECT menu_id FROM boss_user_menu WHERE user_id=#{userId} UNION"
			+ " SELECT rr.menu_id FROM boss_user_right ur,boss_right_menu rr WHERE ur.right_id=rr.right_id "
			+ " AND user_id=#{userId}) urc LEFT JOIN boss_sys_menu sr  ON urc.menu_id=sr.id")
	@ResultType(MenuInfo.class)
    List<MenuInfo> getUserAllMenu(Integer uId);
	

}
