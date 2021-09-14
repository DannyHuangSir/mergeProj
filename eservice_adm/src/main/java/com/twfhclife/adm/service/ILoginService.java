package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.FunctionVo;
import com.twfhclife.adm.model.LoginLogVo;

/**
 * 後台登入服務.
 * 
 * @author alan
 */
public interface ILoginService {
	
	/**
	 * 取得使用者的功能清單
	 * 
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID.
	 * 
	 * @return 回傳使用者的功能清單.
	 */
	public List<FunctionVo> getMenuList(String userName, String keyCloakUserId);
	
	/**
	 * 新增登入記錄.
	 * 
	 * @param vo LoginLogVo
	 */
	public void addLoginLog(@Param("loginLogVo") LoginLogVo vo);
	
	/**
	 * 寫登入記錄.
	 * 
	 * @param vo LoginLogVo
	 */
	public void updateLogoutLog(@Param("loginLogVo") LoginLogVo vo);
	
	public Map<String, List<com.twfhclife.generic.api_model.FunctionDivVo>> getDivAuthMap();
	
	/**
	 * 查詢使用者角色
	 * @param userId
	 * @return
	 */
	public Map<String, String> getRoleName(String userId);
}
