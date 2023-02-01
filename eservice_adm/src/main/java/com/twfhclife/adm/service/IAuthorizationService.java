package com.twfhclife.adm.service;

import com.twfhclife.adm.domain.DownloadUserAuthCSVResponse;

import java.util.List;
import java.util.Map;

/**
 * 權限查詢與報表服務.
 * 
 * @author all
 */
public interface IAuthorizationService {

	/**
	 * 設定使用者的系統跟角色權限資料.
	 * 
	 * @param userAuthList 使用者清單
	 */
	public void setUserSystemRoleAuth(List<Map<String, Object>> userAuthList);

	/**
	 * 取得使用者角色功能權限樹.
	 * 
	 * @param roleIds 使用者角色
	 * @param disable 是否要disable選取
	 * @return 回傳使用者角色功能權限資料
	 */
	public List<Map<String, Object>> getRoleTree(List<String> roleIds, boolean disable);
	
	/**
	 * Invoking implementation of service to download user-authorization CSV by system.
	 * 
	 * @param sysId
	 * @return List<DownloadUserAuthCSVResponse>
	 * @throws Exception
	 */
	List<DownloadUserAuthCSVResponse> downloadUserAuthCSV(String sysId);

	public void setJdUserSystemRoleAuth(List<Map<String, Object>> userAuthList);
}
