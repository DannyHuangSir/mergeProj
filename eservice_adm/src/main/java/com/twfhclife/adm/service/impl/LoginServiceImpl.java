package com.twfhclife.adm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.LoginLogDao;
import com.twfhclife.adm.dao.LoginDao;
import com.twfhclife.adm.model.FunctionVo;
import com.twfhclife.adm.model.LoginLogVo;
import com.twfhclife.adm.service.ILoginService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.FuncMgmtClient;
import com.twfhclife.generic.api_model.FunctionAuthVo;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MenuUtil;

/**
 * 後台登入服務.
 * 
 * @author alan
 */
@Service
public class LoginServiceImpl implements ILoginService {
	
	private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);

	/**
	 * 後台預設最高權限管理員帳號
	 */
	@Value("${eservice_admin.administrator}")
	protected String systemAdminUser;
	
	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private LoginLogDao loginLogDao;
	
	@Autowired
	FuncMgmtClient funcMgmtClient;
	
	private Map<String, List<com.twfhclife.generic.api_model.FunctionDivVo>> divAuthMap;

	/**
	 * 取得使用者的功能清單
	 * 
	 * @param userName 使用者帳號
	 * @param keyCloakUserId keyCloak user UUID.
	 * 
	 * @return 回傳使用者的功能清單.
	 */
	@RequestLog
	@Override
	public List<FunctionVo> getMenuList(String userName, String keyCloakUserId) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (StringUtils.equals(userName, systemAdminUser) ? "Y" : "N");
		boolean adminUserFlagB = (StringUtils.equals(userName, systemAdminUser) ? true : false);
		logger.info("[{}] is admin user: {}", userName, adminUserFlag);

		// 取得使用者的功能清單
//		List<FunctionVo> menuList = loginDao.getMenuList(ApConstants.SYSTEM_ID, keyCloakUserId, adminUserFlag);
		
		// 透過API取得menu權限
		List<FunctionVo> menuList = funcMgmtClient.getMenuList(ApConstants.SYSTEM_ID, keyCloakUserId,
				adminUserFlag);
		
		//轉換為FunctionDiv Map方便後續取用個功能div權限(admin不受限)
		divAuthMap = new HashMap<>();
		if (!adminUserFlagB && !CollectionUtils.isEmpty(menuList)) {
			for (FunctionVo func : menuList) {
				if (func.getFunctionType().equals("F") && func.getFunctionUrl() != null
						&& func.getForbiddenDivs() != null) {
					divAuthMap.put(func.getFunctionUrl(), func.getForbiddenDivs());
					logger.debug("put divAuthMap url=" + func.getFunctionUrl(), func.getForbiddenDivs());
				}
			}
		}
		
		List<FunctionVo> userMenuList = new ArrayList<FunctionVo>();
		if (!CollectionUtils.isEmpty(menuList)) {
			userMenuList = MenuUtil.convertMenuTree(userName, menuList, ApConstants.SYSTEM_ID);
		} else {
			logger.info("Unable to get menu list for user: {}", userName);
		}

		return userMenuList;
	}

	
	/**
	 * 新增登入記錄.
	 * 
	 * @param vo LoginLogVo
	 */
	@RequestLog
	public void addLoginLog(LoginLogVo vo) {
		try {
			loginLogDao.addLoginLog(vo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 寫登出記錄.
	 * 
	 * @param vo AuditLogVo
	 */
	@RequestLog
	public void updateLogoutLog(LoginLogVo vo) {
		try {
			loginLogDao.updateLogoutLog(vo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}


	public Map<String, List<com.twfhclife.generic.api_model.FunctionDivVo>> getDivAuthMap() {
		return divAuthMap;
	}
	
	/**
	 * 查詢使用者角色
	 * @param userId
	 * @return
	 */
	@Override
	public Map<String, String> getRoleName(String userId) {
		Map<String, String> rMap = new HashMap<String, String>();
		String key = "";
		String name = "";
		List<String> results = loginDao.getRoleName(userId);
		if (results != null && results.size() != 0) {
			for (String temp : results) {
				String[] strs = temp.split("\\|");
				if (strs.length > 0) {
					key = key + strs[0] + ",";
					name = name + strs[1] + ",";
				}
			}
		}
		if (key.endsWith(",")) {
			key =	key.substring(0,key.length() - 1);
		}
		if (name.endsWith(",")) {
			name =	name.substring(0,name.length() - 1);
		}
		rMap.put("key", key);
		rMap.put("name", name);
		return rMap;
	}
	
}
