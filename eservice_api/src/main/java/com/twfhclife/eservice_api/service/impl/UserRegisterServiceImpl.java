package com.twfhclife.eservice_api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.adm.model.ParameterCategoryVo;
import com.twfhclife.eservice_api.service.IUserRegisterService;
import com.twfhclife.generic.dao.adm.UsersDao;
import com.twfhclife.generic.domain.ResetPwdRequest;
import com.twfhclife.generic.model.KeycloakUser;
import com.twfhclife.generic.model.UserRoleVo;
import com.twfhclife.generic.model.UserVo;
import com.twfhclife.generic.utils.MyJacksonUtil;
import com.twfhclife.generic.utils.MyStringUtil;
import com.twfhclife.generic.utils.ValidateUtil;
import com.twfhclife.keycloak.service.KeycloakService;

/**
 * 參數類型維護服務.
 * 
 * @author all
 */
@Service
public class UserRegisterServiceImpl implements IUserRegisterService {

	private static final Logger logger = LogManager.getLogger(UserRegisterServiceImpl.class);

	@Value("${keycloak.elife-realm}")
	protected String ESERVICE_REALM;

	@Value("${keycloak.elife-clientId}")
	protected String ESERVICE_CLIENT_ID;
	
	@Autowired
	KeycloakService keycloakService;

	@Autowired
	@Qualifier("apiUsersDao")
	private UsersDao userDao;

	@Override
	public Map<String, String> createEserviceUser(UserVo user) {
		logger.info("registerUserData user=" + MyJacksonUtil.object2Json(user));
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("userId", "");
		retMap.put("error", "");
		String userId = null;
		KeycloakUser keUser = new KeycloakUser();
		// 不分大小寫 統一轉小寫
		keUser.setUsername(user.getUsername().toLowerCase());
		keUser.setPassword(user.getPassword());
		keUser.setMobile(user.getMobile());
		keUser.setEmail(user.getEmail());
		keUser.setRocId(user.getRocId());
		keUser.setFbId(user.getFbId());
		keUser.setCardSn(user.getMoicaId());
		// call keycloak
		try {
			userId = keycloakService.createUser(ESERVICE_REALM, keUser);
			retMap.put("userId", userId);
		} catch (Exception e) {
			if (e.getMessage().contains("409")) {
				// 用戶名稱重複 (非錯誤故不寫error log)
				retMap.put("error", "409");
			} else {
				// 其他錯誤
				logger.error("Error occured in keycloakService.createUser():", e);
				retMap.put("error", "400");
			}
		}

		// create Users
		if (MyStringUtil.isNotNullOrEmpty(userId)) {
			// Keycloak create user成功，繼續存會員資料到eservice db
			try {
				userDao.createUser(user);
				KeycloakUser ku = keycloakService.getUserByUsername(ESERVICE_REALM, user.getUsername());

				UserRoleVo userRole = new UserRoleVo();
				userRole.setUserType(String.format("user_type=%s", user.getUserType()));
				userRole.setUserId(ku.getId());

				userDao.createUserRole(userRole);
			} catch (Exception e) {
				logger.error("Error occured in userDao.createUser():", e);
				retMap.put("error", "SQL");
			}
		}

		return retMap;
	}

	@Override
	public int deleteEserviceUser(UserVo user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ParameterCategoryVo> updateEserviceUser(UserVo user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String resetEservicePassword(ResetPwdRequest req) {
		String msg = "";
		if (ValidateUtil.isPwd(req.getNewPassword())) {
			// 先查詢使用者帳號
			String userId = keycloakService.getUserIdByUsername(ESERVICE_REALM, req.getUsername());
			if(userId == null) {
				//使用者帳號不存在
				msg = "使用者帳號不存在";
			} else {
				Map<String, Object> resultmap = keycloakService.resetPwd(ESERVICE_REALM, userId, req.getNewPassword());
				String result = resultmap.get("result").toString();
				if(result.contentEquals("true")) {
					logger.debug("update password success!");
					userDao.updatePasswordDate(req.getUsername());
					logger.debug("update last password date success!");
				} else {
					if(resultmap.get("error_description") != null) {
						msg = resultmap.get("error_description").toString();
					} else {
						msg = "error";
					}
				}
			}
		} else {
			msg = "請輸入8～20碼混合之數字及英文字母和符號(須區分大小寫)！";
		}
		
		return msg;
	}

}
