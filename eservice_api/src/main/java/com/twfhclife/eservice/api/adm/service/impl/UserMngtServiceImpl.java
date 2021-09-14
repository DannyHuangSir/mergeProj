package com.twfhclife.eservice.api.adm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.adm.model.UserEntityVo;
import com.twfhclife.eservice.api.adm.service.IUserMgntService;
import com.twfhclife.generic.dao.KeycloakUserDao;

/**
 * 人員管理服務.
 * 
 * @author all
 */
@Service
public class UserMngtServiceImpl implements IUserMgntService {

	@Autowired
	private KeycloakUserDao keycloakUserDao;

	/**
	 * 人員管理-分頁查詢.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo UserEntityVo
	 * @return 回傳人員管理-分頁查詢結果
	 */
	@Override
	public List<Map<String, Object>> getUserEntityPageList(UserEntityVo userEntityVo, boolean isAdmin) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (isAdmin ? "Y" : "N");
		return keycloakUserDao.getUserEntityPageList(userEntityVo, adminUserFlag);
	}

	/**
	 * 人員管理-查詢結果總筆數.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo UserEntityVo
	 * @return 回傳總筆數
	 */
	@Override
	public int getUserEntityPageTotal(UserEntityVo userEntityVo, boolean isAdmin) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (isAdmin ? "Y" : "N");
		return keycloakUserDao.getUserEntityPageTotal(userEntityVo, adminUserFlag);
	}
	
	/**
	 * 人員管理-查詢.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @return 回傳查詢結果
	 */
	@Override
	public List<UserEntityVo> getUserEntity(UserEntityVo userEntityVo) {
		return keycloakUserDao.getUserEntity(userEntityVo);
	}
}