package com.twfhclife.generic.dao.adm;

import com.twfhclife.eservice.api.jdzq.model.UserDetailReqVo;
import com.twfhclife.eservice.api.jdzq.model.UserDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.twfhclife.generic.model.UserRoleVo;
import com.twfhclife.generic.model.UserVo;
import com.twfhclife.generic.model.UsersVo;

import java.util.List;

@Repository("apiUsersDao")
public interface UsersDao {

	public UsersVo getUserByRocId(@Param("rocId") String rocId);
	
	public UsersVo getUsers();
	
	public void createUser(@Param("vo") UserVo user);
	
	public void createUserRole(@Param("vo") UserRoleVo userRole);
	
	public UsersVo getUserByAccount(@Param("account") String account);
	
	public void updatePasswordDate(@Param("username") String username);
	
	/**
	 * 檢查輸入的保單號碼與身分證號碼對應的保單是否存在
	 * 
	 * @param rocId 身分證號碼
	 * @param policyNo 保單號碼
	 * 
	 * @return 存在回傳保單號碼
	 */
	String getPolicyByRocId(@Param("rocId") String rocId, @Param("policyNo") String policyNo);
	
	/** 
	 * 取得此身分證下所有保單的貸款筆數.
	 * 
	 * @param rocId 身分證號碼
	 * @return Integer 貸款的保單數量
	 */
	Integer getPolicyExtraByRocId(@Param("rocId") String rocId);
	
	/**
	 * 取得此身分證下的要保人生日.
	 * 
	 * @param rocId 身分證號碼
	 * @return String 要保人生日 
	 */
	String getBirthDateByRocId(@Param("rocId") String rocId);
	
	/**
	 * 取得此身分證下所有被保人資料.
	 * 
	 * @param rocId 身分證號碼
	 * @return List<InsuredVo> 被保人資料
	 */
	//List<InsuredVo> getInsByProRocId(@Param("rocId") String rocId);
	
	/**
	 * 取得使用者的姓名
	 * @param keycloakUserId
	 * @return
	 */
	String getUserNameById(@Param("userId") String userId);
	
	/**
	 * 更新使用者身份類型
	 * @param userId
	 */
	void updateUserType(@Param("account") String userId, @Param("userType") String userType);
	
	public UsersVo getUserByFbId(@Param("realm") String realm, @Param("fbId") String fbId);
	
	public UsersVo getUserByCardSn(@Param("realm") String realm, @Param("cardSn") String cardSn);

	public List<UserDetailVo> getUserDetail(@Param("vo") UserDetailReqVo userVo, @Param("columnItem")String columnItem);
}
