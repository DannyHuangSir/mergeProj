package com.twfhclife.eservice_batch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.UserDao;
import com.twfhclife.eservice_batch.model.CommLogRequestVo;
import com.twfhclife.eservice_batch.model.UserVo;
import com.twfhclife.eservice_batch.util.MailService;
import com.twfhclife.eservice_batch.util.MailTemplateUtil;

public class BatchCheckPwdExpireService {

	private static final Logger logger = LogManager.getLogger(BatchCheckPwdExpireService.class);
	
	private static String EXPIRE_LIMIT_MONTH;	
	private static String EXPIRE_NOTICE_MONTH;	
	private static String NOTICE_MAIL_INNER_TITLE;
	private static String NOTICE_MAIN_INNER_CONTENT;
	private static String NOTICE_MAIL_SUBJECT;
	private static String LAST_LOGIN_LIMIT_YEARS;	
	private UserDao userDao = new UserDao();
	private MailService mailService = new MailService();

	public BatchCheckPwdExpireService() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		EXPIRE_LIMIT_MONTH = rb.getString("expire_limit_month");
		EXPIRE_NOTICE_MONTH = rb.getString("expire_notice_month");
		NOTICE_MAIL_INNER_TITLE = rb.getString("notice_mail_inner_title");
		NOTICE_MAIN_INNER_CONTENT = rb.getString("notice_main_inner_content");
		NOTICE_MAIL_SUBJECT = rb.getString("notice_mail_subject");
		LAST_LOGIN_LIMIT_YEARS = rb.getString("last_login_limit_years");
		logger.debug(
				"EXPIRE_LIMIT_MONTH:{}, EXPIRE_NOTICE_MONTH:{}, NOTICE_MAIL_INNER_TITLE:{}, NOTICE_MAIN_INNER_CONTENT:{}, NOTICE_MAIL_SUBJECT:{}, LAST_LOGIN_LIMIT_YEARS:{}",
				EXPIRE_LIMIT_MONTH, EXPIRE_NOTICE_MONTH,
				NOTICE_MAIL_INNER_TITLE, NOTICE_MAIN_INNER_CONTENT,
				NOTICE_MAIL_SUBJECT, LAST_LOGIN_LIMIT_YEARS);
	}
	
	public void checkNoticeUser() {
		try {
			//1. get users data that lastChangePwdDate != null and lastChangePwdDate = add_months(sysdate, expire_notice_month - expire_limit_month)
			//for example:lastChangePwdDate=20190101, expire=20190401, notice date=20190301
			List<UserVo> userList = userDao.getUserNoticeList(EXPIRE_LIMIT_MONTH, EXPIRE_NOTICE_MONTH);
			
			//2. send mail notification
			if (userList != null && userList.size() > 0) {
				String templateContent = MailTemplateUtil.getMailTempleteContent("common_template.html");
				String cnt = templateContent.replaceFirst("\\{title\\}", NOTICE_MAIL_INNER_TITLE).replaceFirst("\\{content\\}", NOTICE_MAIN_INNER_CONTENT);
				List<String> mailTo = new ArrayList<String>();
				for (UserVo vo : userList) {
					if (!StringUtils.isEmpty(vo.getEmail())) {
						mailTo.add(vo.getEmail());
						logger.debug("prepard notice email:{}", vo.getEmail());
					}
				}
				String sentCnt = cnt.replaceFirst("\\{0\\}", EXPIRE_NOTICE_MONTH);
				logger.debug("mailTo:{}", mailTo.toString());
				logger.debug("sentCnt:{}", sentCnt);
				mailService.sendMail(cnt.replaceFirst("\\{0\\}", EXPIRE_NOTICE_MONTH), NOTICE_MAIL_SUBJECT, mailTo, null, null);
				
				// 儲存郵件簡訊發送紀錄
				BatchApiService apiService = new BatchApiService();
				for (String addr : mailTo) {
					apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "email", addr, sentCnt));
				}
				
			}
		} catch (Exception e) {
			logger.error("checkNoticeUser error: {}", ExceptionUtils.getStackTrace(e));
		}
		
	}
	
	public void clearUsersLastChgPwdDate() {
		List<UserVo> userList = null;
		StringBuilder sb = null;
		try {
			//3. get users data that pwd expired (lastChangePwdDate + n個月 + 1天 = sysdate)
			userList = userDao.getUserExpiredList(EXPIRE_LIMIT_MONTH);
			//4. update users set lastChangePwdDate = null
			if (userList != null && userList.size() > 0) {
				sb = new StringBuilder("\n=============================================================================\n");
				sb.append("Users below password expired, clear its last change password date:\n");
				for (UserVo vo : userList) {
					userDao.clearUsersLastChgPwdDate(vo.getUserId());
					sb.append(vo.getUserId() + "\n");
				}
				sb.append("=============================================================================\n");
			}
		} catch (Exception e) {
			logger.error("clearUsersLastChgPwdDate error: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			if (userList != null && userList.size() > 0) {
				logger.debug(sb);
			}
		}
	}
	
	public void checkLastLoginOverYears() {
		try {
			//1. get user last login date over limit years
			List<UserVo> userList = userDao.getUserLastLoginOverYears(LAST_LOGIN_LIMIT_YEARS);
			//2. update users set status = locked
			if (userList != null) {
				for (UserVo vo : userList) {
					userDao.lockUserStatus(vo.getUserId());
					logger.debug("update userId:{} status = 'locked' ", vo.getUserId());
				}
			}
		} catch (Exception e) {
			logger.error("checkLastLoginOverYears error: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
