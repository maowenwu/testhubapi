package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.MenuInfo;
import cn.huobi.framework.model.RightInfo;
import cn.huobi.framework.model.RoleInfo;
import cn.huobi.framework.model.UserInfo;

public interface RightDao {

	@SelectProvider(type=SqlProvider.class, method="selectRightByCondition")
	@ResultType(RightInfo.class)
	List<RightInfo> selecRightByCondition(@Param("right") RightInfo right, Page<RightInfo> page);
	
	@Select("select 1 from boss_shiro_right where EXISTS (select 1 from boss_shiro_right r where r.right_code=#{rightCode}) limit 1")
	@ResultType(Integer.class)
	Integer checkCodeUnique(@Param("rightCode")String rightCode);
	
	@Insert("insert into boss_shiro_right (right_code,right_name,right_comment) values (#{right.rightCode},#{right.rightName},"
			+ "#{right.rightComment})")
	int saveRight(@Param("right")RightInfo right);

	@Update("update boss_shiro_right set right_code=#{right.rightCode},right_name=#{right.rightName},right_comment=#{right.rightComment} "
			+ "where id=#{right.id}")
	int updateRight(@Param("right")RightInfo right);
	
	@Delete("DELETE FROM boss_user_right where right_id=#{rightId}")
	void delUsers(Integer rightId);

	@Delete("DELETE FROM boss_role_right where right_id=#{rightId}")
	void delRoles(Integer rightId);

	@Delete("DELETE FROM boss_shiro_right where id=#{rightId}")
	int delRight(Integer rightId);
	
	@Select("SELECT r.* FROM boss_role_right rr,boss_shiro_role r WHERE r.id=rr.role_id AND rr.right_id=#{rightId}")
	@ResultType(RoleInfo.class)
	List<RoleInfo> getRoleByRight(Integer rightId);
	
	@Select("SELECT u.* FROM boss_user_right ur,boss_shiro_user u WHERE u.id=ur.user_id AND ur.right_id=#{rightId}")
	@ResultType(UserInfo.class)
	List<UserInfo> getUserByRight(Integer rightId);

	@Select("select * from boss_shiro_right")
	@ResultType(RightInfo.class)
	List<RightInfo> getAllRights();

	@Select("select m.* from boss_sys_menu m,boss_right_menu rm where rm.right_id=#{id} and m.id=rm.menu_id")
	@ResultType(MenuInfo.class)
	List<MenuInfo> getMenuByRight(Integer id);
	
	class SqlProvider{
		public String selectRightByCondition(Map<String, Object> param){
			final RightInfo right = (RightInfo)param.get("right");
			return new SQL(){
				{
					SELECT("*");
					FROM("boss_shiro_right");
					if(right!= null){
						if(StringUtils.isNotBlank(right.getRightName())){
							right.setRightName(right.getRightName()+"%");
							WHERE("right_name like #{right.rightName}");
						}
						if(StringUtils.isNotBlank(right.getRightCode())){
							right.setRightCode(right.getRightCode()+"%");
							WHERE("right_code like #{right.rightCode}");
						}
					}
				}
			}.toString();
		}
	}

}
