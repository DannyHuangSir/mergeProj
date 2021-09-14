package com.twfhclife.adm.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.RoleDao;
import com.twfhclife.adm.model.RoleVo;
import com.twfhclife.adm.service.IRoleService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 使用者角色管理服務.
 * 
 * @author all
 */
@Service
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleDao roleDao;

	/**
	 * 後台預設最高權限管理員帳號
	 */
	@Value("${eservice_admin.administrator}")
	protected String systemAdminUser;
	
	/**
	 * 取得使用者角色管理查詢結果.
	 * 
	 * @param roleVo RoleVo
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID
	 * @return 回傳使用者角色管理查詢結果
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getRolePageList(RoleVo roleVo, String userName, String keyCloakUserId) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (StringUtils.equals(userName, systemAdminUser) ? "Y" : "N");
		return roleDao.getRolePageList(roleVo, keyCloakUserId, adminUserFlag);
	}

	/**
	 * 取得使用者角色管理查詢結果總筆數.
	 * 
	 * @param roleVo RoleVo
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getRolePageTotal(RoleVo roleVo, String userName, String keyCloakUserId) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (StringUtils.equals(userName, systemAdminUser) ? "Y" : "N");
		return roleDao.getRolePageTotal(roleVo, keyCloakUserId, adminUserFlag);
	}
	
	/**
	 * 使用者角色管理-查詢.
	 * 
	 * @param roleVo RoleVo
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<RoleVo> getRole(RoleVo roleVo, String userName, String keyCloakUserId) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (StringUtils.equals(userName, systemAdminUser) ? "Y" : "N");
		return roleDao.getRole(roleVo, keyCloakUserId, adminUserFlag);
	}

	/**
	 * 使用者角色管理-依照使用者權限查詢.
	 * 
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<RoleVo> getRoleByAuth(String userName, String keyCloakUserId) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (StringUtils.equals(userName, systemAdminUser) ? "Y" : "N");
		return roleDao.getRoleByAuth(keyCloakUserId, adminUserFlag);
	}

	/**
	 * 使用者角色管理-新增.
	 * 
	 * @param roleVo RoleVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int insertRole(RoleVo roleVo) {
		return roleDao.insertRole(roleVo);
	}
	
	/**
	 * 使用者角色管理-更新.
	 * 
	 * @param roleVo RoleVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int updateRole(RoleVo roleVo) {
		return roleDao.updateRole(roleVo);
	}

	/**
	 * 使用者角色管理-刪除.
	 * 
	 * @param roleVo RoleVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int deleteRole(RoleVo roleVo) {
		return roleDao.deleteRole(roleVo);
	}
}