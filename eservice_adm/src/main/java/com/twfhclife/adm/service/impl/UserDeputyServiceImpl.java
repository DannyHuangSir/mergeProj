package com.twfhclife.adm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.UserDeputyDao;
import com.twfhclife.adm.model.UserDeputyVo;
import com.twfhclife.adm.service.IUserDeputyService;

/**
 * 人員管理 - 代理人服務
 * @author Ken Wei
 *
 */
@Service
public class UserDeputyServiceImpl implements IUserDeputyService {
	
	@Autowired
	private UserDeputyDao userDeputyDao;
	
	/**
	 * 後台預設最高權限管理員帳號
	 */
	@Value("${eservice_admin.administrator}")
	private String systemAdminUser;
	
	@Value("${keycloak.adm-realm}")
	private String admRealm;

	@Override
	public List<UserDeputyVo> getUserDeputy(UserDeputyVo userDeputyVo) {
		return userDeputyDao.getUserDeputy(userDeputyVo);
	}

	@Override
	public int insertUserDeputy(UserDeputyVo userDeputyVo) {
		return userDeputyDao.insertUserDeputy(userDeputyVo);
	}

	@Override
	public int getUserDeputyPageTotal(UserDeputyVo userDeputyVo) {
		return userDeputyDao.getUserDeputyPageTotal(userDeputyVo);
	}

	@Override
	public List<Map<String, Object>> getUserDeputyPageList(UserDeputyVo userDeputyVo) {
		return userDeputyDao.getUserDeputyPageList(userDeputyVo);
	}

	@Override
	public List<Map<String, Object>> getCanDeputyUser(UserDeputyVo userDeputyVo) {
		return userDeputyDao.getCanDeputyUser(userDeputyVo, admRealm);
	}

	@Override
	public int deleteDeputyById(Integer id) {
		return userDeputyDao.deleteDeputyById(id);
	}

}
