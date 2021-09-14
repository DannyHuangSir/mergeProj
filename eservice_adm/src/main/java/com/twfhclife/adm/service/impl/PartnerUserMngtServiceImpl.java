package com.twfhclife.adm.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.PartnerUserEntityDao;
import com.twfhclife.adm.dao.RoleDivAuthDao;
import com.twfhclife.adm.dao.UsersDao;
import com.twfhclife.adm.model.PartnerUserEntityVo;
import com.twfhclife.adm.model.RoleDivAuthVo;
import com.twfhclife.adm.model.UsersVo;
import com.twfhclife.adm.service.IPartnerUserMgntService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_model.MessageTriggerRequestVo;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;
import com.twfhclife.generic.util.RandomUtils;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.KeycloakService;

/**
 * 外部人員管理服務.
 * 
 * @author all
 */
@Service
@EnableAutoConfiguration
public class PartnerUserMngtServiceImpl implements IPartnerUserMgntService {

	private static final Logger logger = LogManager.getLogger(PartnerUserMngtServiceImpl.class);
	
	@Autowired
	private PartnerUserEntityDao partnerUserEntityDao;

	@Autowired
	private KeycloakService keycloakService;

	@Autowired
	private IMailService mailService;
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	MessageTemplateClient messageTemplateClient;
	
	@Value("${keycloak.elife-realm}")
	protected String elifeRealm;
	
	@Autowired
	private RoleDivAuthDao roleDivAuthDao;
	
//	@Value("${partner-user-add.email.template}")
//	protected String addUserEmailTemplate;
	
	/**
	 * 外部人員管理-分頁查詢.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳人員管理-分頁查詢結果
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getPartnerUserEntityPageList(PartnerUserEntityVo userEntityVo) {
		userEntityVo.setRealmId(elifeRealm);
		return partnerUserEntityDao.getPartnerUserEntityPageList(userEntityVo);
	}

	/**
	 * 外部人員管理-查詢結果總筆數.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getPartnerUserEntityPageTotal(PartnerUserEntityVo userEntityVo) {
		userEntityVo.setRealmId(elifeRealm);
		return partnerUserEntityDao.getPartnerUserEntityPageTotal(userEntityVo);
	}

	/**
	 * 外部人員-取得名稱
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳名稱
	 */
	@RequestLog
	@Override
	public String getNextPartnerUserName(PartnerUserEntityVo userEntityVo) {
		String agenCode = userEntityVo.getAgenCode();
		if (!StringUtils.isEmpty(agenCode)) {
			userEntityVo.setAgenCode(agenCode.toUpperCase());
		}
		
		// 通路別為銀行
		if ("C01".equals(userEntityVo.getChalCode())) {
			return String.format("%s%s", StringUtils.leftPad(userEntityVo.getAgenCode(), 3, "0"),
					StringUtils.leftPad(userEntityVo.getBranchCode(), 4, "0"));
		}
		
		userEntityVo.setRealmId(elifeRealm);
		return partnerUserEntityDao.getNextPartnerUserName(userEntityVo);
	}
	
	/**
	 * 外部人員-檢查名稱是否存在.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳筆數
	 */
	@RequestLog
	@Override
	public int checkUserNameExist(PartnerUserEntityVo userEntityVo) {
		userEntityVo.setRealmId(elifeRealm);
		int cnt = partnerUserEntityDao.checkUserNameExist(userEntityVo);
		if (cnt == 0) {
			cnt = usersDao.checkUserIdExist(userEntityVo.getUsername());
		}
		return cnt;
	}
	
	/**
	 * 外部人員-新增.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int insertPartnerUser(PartnerUserEntityVo userEntityVo) {
		int result = 0;
		
		String password = RandomUtils.getNumberEngRandomString(6); // 系統隨機產生6碼英數夾雜字串
		
		KeycloakUser keycloakUser = new KeycloakUser();
		keycloakUser.setUsername(userEntityVo.getUsername());
		keycloakUser.setPassword(password);
		keycloakUser.setEmail(userEntityVo.getEmail());
		keycloakUser.setMobile(userEntityVo.getMobile());
		String id = keycloakService.createUser(elifeRealm, keycloakUser);
		
		String usename = keycloakUser.getUsername();
		if (usename.length() > 10) {
			usename = usename.substring(0, 10);
		}
		if (!StringUtils.isEmpty(id)) {
			sendMail(userEntityVo.getEmail(), userEntityVo.getUsername(), password);
//			MessageTriggerRequestVo msgVo = new MessageTriggerRequestVo();
//			msgVo.setMessagingTemplateCode(addUserEmailTemplate);
//			msgVo.setSendType("email");
//			List<String> mm = new ArrayList<>();
//			mm.add(userEntityVo.getEmail());
//			msgVo.setMessagingReceivers(mm);
//			Map<String, String> pp = new HashMap<>();
//			pp.put("Username", userEntityVo.getUsername());
//			pp.put("Password", password);
//			msgVo.setParameters(pp);
//			messageTemplateClient.msgApi(msgVo);
			
			UsersVo usersVo = new UsersVo();
			usersVo.setUserId(keycloakUser.getUsername());
			usersVo.setUserType(userEntityVo.getRoleCode());
			usersVo.setRocId(usename);
			usersVo.setEmail(keycloakUser.getEmail());
			usersVo.setMobile(keycloakUser.getMobile());
			usersVo.setCreateDate(new Date());
			result = usersDao.insertUsers(usersVo);
		}
		return result;
	}
	
	/**
	 * 寄送郵件通知.
	 * 
	 * @param mailTo 寄件人
	 * @param password 初始密碼
	 */
	private void sendMail(String mailTo, String username, String password) {
		try {
			String content = String.format("您的帳號/密碼為%s/%s, 請於第一次登入後變更密碼", username, password);
			String subject = "密碼通知";
			
			mailService.sendMail(content, subject, mailTo, "", null);
		} catch (Exception e) {
			logger.error("Unable to sendMail: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * 外部人員-刪除.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int deletePartnerUser(PartnerUserEntityVo userEntityVo) {
		int result = keycloakService.deleteUser(elifeRealm, userEntityVo.getId());
		if (result > 0) {
			UsersVo usersVo = new UsersVo();
			usersVo.setUserId(userEntityVo.getUsername());
			usersDao.deleteUsers(usersVo);
		}
		
		return result;
	}
	
	/**
	 * 外部人員-解鎖.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int unlockUser(PartnerUserEntityVo userEntityVo) {
		KeycloakUser keycloakUser = keycloakService.getUser(elifeRealm, userEntityVo.getId());
		String password = RandomUtils.getNumberEngRandomString(8); // 系統隨機產生8碼英數夾雜字串
		keycloakService.updatePassword(keycloakUser, password, elifeRealm);

		UsersVo usersVo = new UsersVo();
		usersVo.setUserId(userEntityVo.getUsername());
		usersVo.setStatus(ApConstants.USERS_STATUS_ENABLED);
		usersVo.setLoginFailCount(new BigDecimal(0));
		int cut = usersDao.updateUsers(usersVo);
		
		//send mail via message_template
		if (!StringUtils.isEmpty(userEntityVo.getEmail())) {
			MessageTriggerRequestVo req = new MessageTriggerRequestVo();
			req.setMessagingTemplateCode("ELIFE_MAIL-001");
			req.setSendType("email");
			
			List<String> receivers = new ArrayList<String>();
			receivers.add(userEntityVo.getEmail());
			req.setMessagingReceivers(receivers);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("Password", password);
			req.setParameters(map);
			
			ReturnHeader returnHeader = messageTemplateClient.msgApi(req);
			logger.debug("returnHeader:{}", MyJacksonUtil.object2Json(returnHeader));
//			this.sendMail(userEntityVo.getEmail(), userEntityVo.getUsername(), password);
		}
		
		//send sms via message_template
		if (!StringUtils.isEmpty(userEntityVo.getMobile())) {
			MessageTriggerRequestVo req = new MessageTriggerRequestVo();
			req.setMessagingTemplateCode("ELIFE_SMS-001");
			req.setSendType("sms");
			
			List<String> receivers = new ArrayList<String>();
			receivers.add(userEntityVo.getMobile());
			req.setMessagingReceivers(receivers);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("Password", password);
			req.setParameters(map);
			
			ReturnHeader returnHeader = messageTemplateClient.msgApi(req);
			logger.debug("returnHeader:{}", MyJacksonUtil.object2Json(returnHeader));
		}
		
		return cut;
	}

	@Override
	public int updateOnlineChangeUse(UsersVo usersVo) {
		int result = usersDao.updateOnlineChangeUse(usersVo);
		return result;
	}

	@Override
	public List<RoleDivAuthVo> getRoleDivAuthByUserId(String userId) {
		List<RoleDivAuthVo> rtnList = null;
		rtnList = roleDivAuthDao.getRoleDivAuthByUserId(userId);
		return rtnList;
	}
}