package cn.huobi.framework.service.sysUser.impl;
//package cn.huobi.framework.service.sysUser.impl;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import cn.huobi.framework.dao.sysUser.SysMenuDao;
//import cn.huobi.framework.model.ShiroRigth;
//import cn.huobi.framework.model.SysMenu;
//import cn.huobi.framework.service.sysUser.ShiroRigthService;
//import cn.huobi.framework.service.sysUser.SysMenuService;
//
//@Service("sysMenuService")
//@Transactional
//public class SysMenuServiceImpl implements SysMenuService{
//	
//	@Resource
//	public SysMenuDao sysMenuDao;
//	@Resource
//	public SysMenuService sysMenuService;
//	@Resource
//	public ShiroRigthService shiroRigthService;
//	
//	private static final Logger log = LoggerFactory.getLogger(SysMenuServiceImpl.class);
//
////	@Override
//	public SysMenu findSysMenuById(Integer id) throws Exception {
//		return sysMenuDao.findSysMenuById(id);
//	}
//	@Override
//	public List<SysMenu> findAllSysMenu() throws Exception {
//		return sysMenuDao.findAllSysMenu();
//	}
//	@Override
//	public List<SysMenu> findUserRolePrivilegeSysMenu(Integer userId) throws Exception {
//		return sysMenuDao.findUserRolePrivilegeSysMenu(userId);
//	}
//	@Override
//	public List<SysMenu> findAllPageSysMenuByParentId(Integer parentId) throws Exception {
//		return sysMenuDao.findAllPageSysMenuByParentId(parentId);
//	}
//	@Override
//	public SysMenu findSysMenuByMenuCode(String menuCode) throws Exception {
//		return sysMenuDao.findSysMenuByMenuCode(menuCode);
//	}
//	@Override
//	public int updateSysMenu(SysMenu sysMenu) {
//		return sysMenuDao.updateSysMenu(sysMenu);
//	}
//	@Override
//	public int insertSysMenu(SysMenu sysMenu) {
//		int i =0,n=0 ;
//		ShiroRigth shiroRigth = new ShiroRigth();
//		shiroRigth.setRigthCode(sysMenu.getMenuCode());
//		shiroRigth.setRigthName(sysMenu.getMenuName());
//		try {
//			i = sysMenuDao.insertSysMenu(sysMenu);
//			n = shiroRigthService.insertShiroRigth(shiroRigth);
//			i = i + n;
//			
//		} catch (Exception e) {
//			//e.printStackTrace();
//			log.error(e.getMessage());
//		}
//		return i;
//	}
//	@Override
//	public int deleteSysMenu(Integer id) {
//		int i =0,n=0 ;
//		//删除菜单的同时删除对应权限
//		try {
//			SysMenu sysMenu = this.findSysMenuById(id);
//			i = sysMenuDao.deleteSysMenu(id);
//			n = shiroRigthService.deleteShiroRigthByRigthCode(sysMenu.getMenuCode());
//			i = i + n;
//		} catch (Exception e) {
//			log.error(e.getMessage());
//		}
//		
//		return i;
//	}
//	@Override
//	public int deleteMenuAndChildren(Integer menuId) {
//		int i = 0;
//		List<SysMenu> sysMenus;
//		//删除菜单的同时删除对应权限
//		try {
//			sysMenus = sysMenuService.findAllPageSysMenuByParentId(menuId);
//			for (SysMenu sysMenu : sysMenus) {
//				int n = sysMenuService.deleteSysMenu(sysMenu.getId());
//				int m = shiroRigthService.deleteShiroRigthByRigthCode(sysMenu.getMenuCode());
//				i = i + n + m;
//			}
//			int n = sysMenuService.deleteSysMenu(menuId);
//			SysMenu sm = this.findSysMenuById(menuId);
//			int m = shiroRigthService.deleteShiroRigthByRigthCode(sm.getMenuCode());
//			i = i + n + m;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return i;
//	}
//	@Override
//	public List<SysMenu> findAllNotBlankUrlSysMenu() throws Exception {
//		return sysMenuDao.findAllNotBlankUrlSysMenu();
//	}
//	@Override
//	public List<SysMenu> selectAllSysMenuAndChildrenList() {
//		return sysMenuDao.selectAllSysMenuAndChildrenList();
//	}
//	@Override
//	public List<SysMenu> findMenuPageList(Integer menuId) {
//		return sysMenuDao.findAllPageSysMenuByParentId(menuId);
//	}
//	
//
//
//
//
//
//
//
//}
