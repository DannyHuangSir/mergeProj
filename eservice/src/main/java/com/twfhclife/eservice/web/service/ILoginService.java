package com.twfhclife.eservice.web.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.web.model.AuditLogVo;
import com.twfhclife.eservice.web.model.LoginRequestVo;
import com.twfhclife.eservice.web.model.LoginResultVo;

/**
 * 前台登入服務.
 * 
 * @author alan
 */
public interface ILoginService {
	
	/**
	 * 登入.
	 * 
	 * @param loginRequestVo LoginRequestVo
	 * @return 回傳登入結果
	 */
	public LoginResultVo doLogin(LoginRequestVo loginRequestVo);
	
	/**
	 * 登出.
	 * 
	 * @param  realm
	 * @param  userId
	 * @return 回傳結果
	 */
	public boolean doLogout(String realm, String userId);
	
	/**
	 * 取得登入sessionId.
	 * 
	 * @param  keyCloakUserId KeyCloak用戶id
	 * @return 回傳登入sessionId
	 */
	public List<String> getKeycloakUserSessionIdList(String keyCloakUserId);
	
	/**
	 * 登出其他使用者.
	 * 
	 * @param  sessionIdList sessionId清單
	 * @return 回傳結果
	 */
	public String logoutOtherUser(List<String> sessionIdList);
	
	/**
	 * 取得使用者的保單名稱清單.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳使用者的保單名稱清單
	 */
	public List<Map<String, String>> getProductNameList(String rocId);
	
	/**
	 * 取得使用者的保單主約被保險人名稱清單.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳使用者的保單名稱清單
	 */
	public List<Map<String, String>> getMainInsuredNameList(String rocId);
	
	/**
	 * 是否是保代所屬角色.
	 * 
	 * @param userType 使用者類型
	 * 
	 * @return 回傳使用者的角色ID清單
	 */
	public boolean isPartnerRole(String userType);
	
	/**
	 * 新增登入記錄.
	 * 
	 * @param auditLogVo AuditLogVo
	 */
	public void addAuditLog(AuditLogVo auditLogVo);
	
	/**
	 * 更新登出記錄.
	 * 
	 * @param userId 用戶名稱
	 */
	public void updateLogoutDate(String userId);
	
	/**
	 * 取得最近登入日期.
	 * 
	 * @param userId 用戶名稱
	 */
	public Date getLastLoginTime(String userId);
	
	/**
	 * 查詢最近的n筆登入記錄.
	 * 
	 * @param userId
	 * @param row
	 */
	public List<AuditLogVo> getLastAuditLog(String userId, String row);

}