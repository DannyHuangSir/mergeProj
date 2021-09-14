package com.twfhclife.adm.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.adm.dao.RoleDivAuthDao;
import com.twfhclife.adm.dao.RoleFuncAuthDao;
import com.twfhclife.adm.dao.RoleSysAuthDao;
import com.twfhclife.adm.model.RoleDivAuthVo;
import com.twfhclife.adm.model.RoleFuncAuthVo;
import com.twfhclife.adm.model.RoleMgntVo;
import com.twfhclife.adm.model.RoleSysAuthVo;
import com.twfhclife.adm.model.RoleVo;
import com.twfhclife.adm.service.IRoleMgntService;
import com.twfhclife.adm.service.IRoleService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 角色權限維護服務.
 * 
 * @author all
 */
@Service
public class RoleMgntServiceImpl implements IRoleMgntService {
	
	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private RoleSysAuthDao roleSysAuthDao;
	
	@Autowired
	private RoleFuncAuthDao roleFuncAuthDao;
	
	@Autowired
	private RoleDivAuthDao roleDivAuthDao;
	
	@Transactional
	@RequestLog
	@Override
	public void updateRole(RoleMgntVo vo, String user) {
		String roleId = vo.getRoleId();
		
		// 刪除所有的系統權限
		roleSysAuthDao.deleteByRoleId(roleId);
		// 刪除所有的功能權限
		roleFuncAuthDao.deleteByRoleId(roleId);
		// 刪除所有的div權限
		roleDivAuthDao.deleteByRoleId(roleId);
		
		// 新增系統權限
		for(String sysId : vo.getSysIds()) {
			RoleSysAuthVo sysAuthVo = new RoleSysAuthVo();
			sysAuthVo.setRoleId(roleId);
			sysAuthVo.setSysId(sysId);
			sysAuthVo.setCreateUser(user);
			roleSysAuthDao.insertRoleSysAuth(sysAuthVo);
		}
		
		// 新增功能權限
		for(String funId : vo.getFunIds()) {
			RoleFuncAuthVo funcAuthVo = new RoleFuncAuthVo();
			funcAuthVo.setRoleId(roleId);
			funcAuthVo.setFunctionId(new BigDecimal(funId));
			funcAuthVo.setCreateUser(user);
			roleFuncAuthDao.insertRoleFuncAuth(funcAuthVo);
		}
		
		// 新增REJECT DIV功能權限
		for(String divId : vo.getDivIds()) {
			divId = divId.replace("div_", "");
			
			RoleDivAuthVo divAuthVo = new RoleDivAuthVo();
			divAuthVo.setRoleId(roleId);
			divAuthVo.setDivId(new BigDecimal(divId));
			divAuthVo.setCreateUser(user);
			roleDivAuthDao.insertRoleDivAuth(divAuthVo);
		}
	}
	
	@RequestLog
	@Override
	public List<Map<String, Object>> getRoleFuncAuth(RoleVo roleVo, String userName, String keyCloakUserId){
		List<Map<String, Object>> result = new ArrayList<>();
		
		List<RoleVo> roles = roleService.getRole(roleVo, userName, keyCloakUserId);
		for(RoleVo role : roles){
			// 取得當前查詢角色的功能列表
			List<Map<String, Object>> roleFunction = roleFuncAuthDao.getRoleFuncAuthByRoleId(role.getRoleId());
			if(!CollectionUtils.isEmpty(roleFunction)){
				result.addAll(roleFunction);
			}
		}
		return result;
	}
}