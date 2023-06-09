package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.UserMapper;
import com.twfhclife.eservice_batch.model.UserVo;

public class UserDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(UserDao.class);

	public UserVo getUserByUsername(String username) {
		UserVo user = null;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			user = userMapper.findById(username);
			logger.debug("user email="+user.getEmail());
		} catch (Exception e) {
			logger.error("getUserByUsername error:", e);
		} finally {
			this.release();
		}
		return user;
	}
	
	public UserVo getContactByUsername(String realm, String username) {
		UserVo user = null;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			user = userMapper.findContactByUsername(realm, username);
			logger.debug("email=" + user.getEmail() + ", mobile=" + user.getMobile());
		} catch (Exception e) {
			logger.error("getContactByUsername error:", e);
		} finally {
			this.release();
		}
		return user;
	}
	
	public List<UserVo> getUserNoticeList(String expireLimitMonths, String expireNoticeMonths) {
		List<UserVo> userList = null;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			userList = userMapper.getUserNoticeList(expireLimitMonths, expireNoticeMonths);
		} catch (Exception e) {
			logger.error("getUserNoticeList error:", e);
		} finally {
			this.release();
		}
		return userList;
	}
	
	public List<UserVo> getUserExpiredList(String expireLimitMonths) {
		List<UserVo> userList = null;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			userList = userMapper.getUserExpiredList(expireLimitMonths);
		} catch (Exception e) {
			logger.error("getUserExpiredList error:", e);
		} finally {
			this.release();
		}
		return userList;
	}
	
	public int clearUsersLastChgPwdDate(String userId) {
		int cut = 0;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			cut = userMapper.clearUsersLastChgPwdDate(userId);
			this.commit();
		} catch (Exception e) {
			logger.error("clearUsersLastChgPwdDate error:", e);
		} finally {
			this.release();
		}
		return cut;
	}
	
	public UserVo getMailPhoneByPolicyNo(String policyNo) {
		UserVo user = null;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			user = userMapper.getMailPhoneByPolicyNo(policyNo);
			logger.debug("policyNo:{}, mail:{}, phone:{}", policyNo, user.getEmail(), user.getMobile());
		} catch (Exception e) {
			logger.error("getMailPhoneByPolicyNo error:", e);
		} finally {
			this.release();
		}
		return user;
	}

	public UserVo getMailPhoneByTransNum(String transNum) {
		UserVo user = null;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			user = userMapper.getMailPhoneByTransNum(transNum);
			logger.debug("transNum:{}, mail:{}, phone:{}", transNum, user.getEmail(), user.getMobile());
		} catch (Exception e) {
			logger.error("getMailPhoneByTransNum error:", e);
		} finally {
			this.release();
		}
		return user;
	}
	
	public List<UserVo> getUserLastLoginOverYears(String lastLoginLimitYears) {
		List<UserVo> userList = null;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			userList = userMapper.getUserLastLoginOverYears(lastLoginLimitYears);
		} catch (Exception e) {
			logger.error("getUserLastLoginOverYears error:", e);
		} finally {
			this.release();
		}
		return userList;
	}
	
	public List<UserVo> getUserLastLoginOverYearsSendMail(String lastLoginLimitYears , String lastLoginMonth) {
		List<UserVo> userList = null;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			userList = userMapper.getUserLastLoginOverYearsSendMail(lastLoginLimitYears , lastLoginMonth);
		} catch (Exception e) {
			logger.error("getUserLastLoginOverYearsSendMail error:", e);
		} finally {
			this.release();
		}
		return userList;
	}
	
	public int lockUserStatus(String userId) {
		int cut = 0;
		try {
			UserMapper userMapper = this.getSqlSession().getMapper(UserMapper.class);
			cut = userMapper.lockUserStatus(userId);
			this.commit();
		} catch (Exception e) {
			logger.error("lockUserStatus error:", e);
		} finally {
			this.release();
		}
		return cut;
	}
	
}
