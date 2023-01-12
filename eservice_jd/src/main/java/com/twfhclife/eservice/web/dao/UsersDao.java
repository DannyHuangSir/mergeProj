package com.twfhclife.eservice.web.dao;

import com.twfhclife.eservice.web.model.UserTermVo;
import com.twfhclife.eservice.web.model.UsersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersDao {

	public UsersVo getUserByRocId(@Param("rocId") String rocId);
	
	public UsersVo getUsers();
	
	public void createUser(@Param("vo") UsersVo user);
	
	public UsersVo getUserByAccount(@Param("account") String account);
	
	public void updatePassword(@Param("account") String account);
	
	/**
	 * 檢查輸入的保單號碼與身分證號碼對應的保單是否存在
	 * 
	 * @param rocId 身分證號碼
	 * @param policyNo 保單號碼
	 * 
	 * @return 存在回傳保單號碼
	 */
	String getPolicyByRocId(@Param("rocId") String rocId, @Param("policyNo") String policyNo);

	public UsersVo getUserByFbId(@Param("realm") String realm, @Param("fbId") String fbId);
	
	public UsersVo getUserByCardSn(@Param("realm") String realm, @Param("cardSn") String cardSn);
	
	public void createUserTerm(@Param("vo") UserTermVo userTerm);
	
	/**
	 * 變更基本資料.
	 * 
	 * @param usersVo UsersVo
	 * @return 回傳影響筆數
	 */
	int updateCustInfo(@Param("usersVo") UsersVo usersVo);
	
	/**
	 * 查找舊系統使用者資料
	 * @param rocId
	 * @param password
	 * @return 回傳資料筆數
	 */
	public int checkOldSystemUser(@Param("rocId") String rocId, @Param("password") String password);

	public List<UsersVo> getMailPhoneByRocid(@Param("rocId") String rocId);
	
	public UsersVo getMailPhoneByRocidPolicyNo(@Param("rocId") String rocId, @Param("policyNo") String policyNo);
	
	public void updateOnlineFlagY(@Param("userId") String userId);
	
}