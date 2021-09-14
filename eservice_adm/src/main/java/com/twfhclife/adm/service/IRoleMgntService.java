package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.RoleMgntVo;
import com.twfhclife.adm.model.RoleVo;

/**
 * 角色權限維護服務.
 * 
 * @author all
 */
public interface IRoleMgntService {
	
	public void updateRole(RoleMgntVo vo, String user);
	
	public List<Map<String, Object>> getRoleFuncAuth(RoleVo roleVo, String userName, String keyCloakUserId);
}