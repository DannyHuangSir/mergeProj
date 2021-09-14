package com.twfhclife.eservice.auth.service;

import java.util.List;

/**
 * 功能權限服務.
 * 
 * @author all
 */
public interface IFuctionAuthService {
	
	/**
	 * 取得拒絕訪問的div 名稱清單.
	 * 
	 * @param keycloakUserId 使用者ID
	 * @return 回傳查詢結果
	 */
	public List<String> getRejectDivName(String keycloakUserId);
}