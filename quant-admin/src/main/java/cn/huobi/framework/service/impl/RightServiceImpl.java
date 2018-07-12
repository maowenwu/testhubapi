package cn.huobi.framework.service.impl;

import cn.huobi.framework.dao.RightDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.MenuInfo;
import cn.huobi.framework.model.RightInfo;
import cn.huobi.framework.model.RoleInfo;
import cn.huobi.framework.model.UserInfo;
import cn.huobi.framework.service.RightService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("rightService")
@Transactional
public class RightServiceImpl implements RightService {

	@Resource
	private RightDao rightDao;
	
	@Override
	public Page<RightInfo> selectRightByCondition(RightInfo right, Page<RightInfo> page) {
		rightDao.selecRightByCondition(right, page);
		return page;
	}

	@Override
	public int insertRight(RightInfo right) {
		return rightDao.saveRight(right);
	}

	@Override
	public int updateRight(RightInfo right) {
		return rightDao.updateRight(right);
	}

	@Override
	public Map<String, Object> checkCodeUnique(String rightCode) {
		Map<String, Object> msg = new HashMap<>();
		Integer num = rightDao.checkCodeUnique(rightCode);
		if(num != null){
			msg.put("msg", "权限编码已存在");
			msg.put("status", false);
		}  else {
			msg.put("msg", "权限编码可以使用");
			msg.put("status", true);
		}
		return msg;
	}

	@Override
	public int deleteRight(Integer rightId) {
		rightDao.delUsers(rightId);
		rightDao.delRoles(rightId);
		int num = rightDao.delRight(rightId);
		return num;
	}

	@Override
	public List<RoleInfo> getRoleByRight(Integer rightId) {
		return rightDao.getRoleByRight(rightId);
	}

	@Override
	public List<UserInfo> getUserByRight(Integer rightId) {
		return rightDao.getUserByRight(rightId);
	}

	@Override
	public List<RightInfo> getAllRights() {
		return rightDao.getAllRights();
	}


	@Override
	public List<MenuInfo> getMenuByRight(Integer id) {
		return rightDao.getMenuByRight(id);
	}
	
	
}
