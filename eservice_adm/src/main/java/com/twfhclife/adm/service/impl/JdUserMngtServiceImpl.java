package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.JdUserBatchDao;
import com.twfhclife.adm.dao.JdUserEntityDao;
import com.twfhclife.adm.model.JdUserVo;
import com.twfhclife.adm.model.NotifySearchVo;
import com.twfhclife.adm.model.UserEntityVo;
import com.twfhclife.adm.service.IJdUserMgntService;
import com.twfhclife.generic.annotation.RequestLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 人員管理服務.
 * 
 * @author all
 */
@Service
public class JdUserMngtServiceImpl implements IJdUserMgntService {

	@Autowired
	private JdUserEntityDao userEntityDao;
	@Autowired
	private JdUserBatchDao jdUserBatchDao;
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
		userEntityVo.setRealmId("elife_jdzq");
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
		userEntityVo.setRealmId("elife_jdzq");
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

    @Override
    public List<Map<String, Object>> getNotifyUsers(NotifySearchVo userEntityVo) {
        return userEntityDao.getNotifyUsers(userEntityVo);
    }


	@Override
	public List<Map<String, Object>> getJdUserQuery(JdUserVo vo) {
		return jdUserBatchDao.getJdUserQuery(vo);
	}

	@Override
	public int countJdUser(JdUserVo vo) {
		return jdUserBatchDao.countJdUser(vo);
	}

	@Override
	public List<Map<String, Object>> getJdUserIcQuery(JdUserVo vo) {
		return jdUserBatchDao.getJdUserIcQuery(vo);
	}

	@Override
	public int countJdUserIc(JdUserVo vo) {
		return jdUserBatchDao.countJdUserIc(vo);
	}

	@Override
	public UserEntityVo getUser(String userName, String realId) {
		return userEntityDao.getUser(userName,realId);
	}

}