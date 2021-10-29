package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import com.twfhclife.eservice_batch.model.UserVo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
	public UserVo findById(String username);
	
	public UserVo findContactByUsername(String realm, String username);
	
	/**
	 * get notice user list
	 * 
	 * @param expireLimitMonths
	 * @param expireNoticeMonths
	 * @return
	 */
	public List<UserVo> getUserNoticeList(String expireLimitMonths, String expireNoticeMonths);
	
	/**
	 * get expired user list
	 * 
	 * @param expireLimitMonths
	 * @return
	 */
	public List<UserVo> getUserExpiredList(String expireLimitMonths);
	
	/**
	 * set users.lastChangPasswordDate = null
	 * 
	 * @param userId
	 * @return
	 */
	public int clearUsersLastChgPwdDate(String userId);
	
	public UserVo getMailPhoneByPolicyNo(String policyNo);
	
	public List<UserVo> getUserLastLoginOverYears(String lastLoginLimitYears);
	
	public int lockUserStatus(String userId);

    UserVo getMailPhoneByTransNum(@Param("transNum") String transNum);
}