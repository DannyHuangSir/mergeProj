package com.twfhclife.eservice.auth.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.auth.dao.RoleDivAuthDao;
import com.twfhclife.eservice.auth.service.IFuctionAuthService;

/**
 * 功能權限服務.
 * 
 * @author all
 */
@Service
public class FuctionAuthServiceImpl implements IFuctionAuthService {

	@Autowired
	private RoleDivAuthDao roleDivAuthDao;
	
	/**
	 * 取得拒絕訪問的div 名稱清單.
	 * 
	 * @param keycloakUserId 使用者ID
	 * @return 回傳查詢結果
	 */
	@Override
	public List<String> getRejectDivName(String keycloakUserId) {
		return roleDivAuthDao.getRejectDivName(keycloakUserId);
	}
}