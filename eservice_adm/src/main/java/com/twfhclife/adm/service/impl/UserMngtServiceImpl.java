package com.twfhclife.adm.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.UserEntityDao;
import com.twfhclife.adm.model.UserEntityVo;
import com.twfhclife.adm.service.IUserMgntService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 人員管理服務.
 * 
 * @author all
 */
@Service
public class UserMngtServiceImpl implements IUserMgntService {

	@Autowired
	private UserEntityDao userEntityDao;

	/**
	 * 後台預設最高權限管理員帳號
	 */
	@Value("${eservice_admin.administrator}")
	protected String systemAdminUser;
	
	@Value("${keycloak.adm-realm}")
	protected String admRealm;
	
	/**
	 * 人員管理-分頁查詢.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo UserEntityVo
	 * @return 回傳人員管理-分頁查詢結果
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getUserEntityPageList(String userId, UserEntityVo userEntityVo) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (StringUtils.equals(userId, systemAdminUser) ? "Y" : "N");
		userEntityVo.setRealmId(admRealm);
		return userEntityDao.getUserEntityPageList(userEntityVo, adminUserFlag);
	}

	/**
	 * 人員管理-查詢結果總筆數.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo UserEntityVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getUserEntityPageTotal(String userId, UserEntityVo userEntityVo) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (StringUtils.equals(userId, systemAdminUser) ? "Y" : "N");
		userEntityVo.setRealmId(admRealm);
		return userEntityDao.getUserEntityPageTotal(userEntityVo, adminUserFlag);
	}
	
	/**
	 * 人員管理-查詢.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<UserEntityVo> getUserEntity(UserEntityVo userEntityVo) {
		return userEntityDao.getUserEntity(userEntityVo);
	}
}